package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.*;
import be.uantwerpen.sc.models.jobs.Job;
import be.uantwerpen.sc.models.map.Link;
import be.uantwerpen.sc.models.map.Map;
import be.uantwerpen.sc.models.map.MapPoint;
import be.uantwerpen.sc.repositories.LinkRepository;
import be.uantwerpen.sc.repositories.MapPointRepository;
import be.uantwerpen.sc.repositories.TransitPointRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@Service
public class PathService {
    private static final Logger logger = LoggerFactory.getLogger(PathService.class);

    @Autowired
    LinkRepository linkRepository;
    @Autowired
    MapPointRepository mapPointRepository;
    @Autowired
    BackendInfoService backendInfoService;
    @Autowired
    BackendService backendService;
    @Autowired
    LinkService linkService;


    @Value("${backends.enabled}")
    boolean backendsEnabled;

    public PathService(){

    }


    public Path makePathFromPointPairs(Integer[] pointpairs, int userStartPid, int startMapId, int stopPid, int stopMapId){
        Path path = new Path();
        ArrayList<Link> transitPath = new ArrayList<Link>();
        logger.info("--- determining new path ---");

        // Check if the starting point of path is the same as the point where the user wants to depart from
        int userStartId = mapPointRepository.findByPointIdAndMap_Id(userStartPid,startMapId).getId();
        if(userStartId != pointpairs[0]){
            logger.info("Startpoint is on the same map: First Job is within the staring map");
//            int userPid = transitPointService.getPointWithId(userStartId).getPid(); // redundant call to get userStartPid

            BackendInfo mapInfo = backendInfoService.getInfoByMapId(startMapId);
            int botStartPid = mapPointRepository.findById(pointpairs[0]).getPointId();
            float weight = backendService.getWeight(mapInfo, userStartPid, botStartPid);
            //float weight = 10;
            path.addWeight(weight);
            path.addJob(userStartPid, botStartPid, startMapId,weight);
        }


        String linkLog = "TransitLink ids: ";
        // traverse points two by two and determine the weights between them
        for(int i = 0; i < pointpairs.length; i+=2){
            int startId = pointpairs[i];
            int stopId = pointpairs[i+1];

            MapPoint startPoint = mapPointRepository.findById(startId);
            MapPoint stopPoint = mapPointRepository.findById(stopId);


            if(startPoint.getMapId() != stopPoint.getMapId()){
                logger.info("current points " + startId + "," + stopId + ", part of a transit link" );
                Link link = linkService.getLinkWithStartidAndStopid(startId,stopId);
                // check if the transitlink exists with start and stopid in different order
                if(link == null) {
                    link = linkService.getLinkWithStartidAndStopid(stopId, startId);
                }

                linkLog+= link.getId() + "-";
                transitPath.add(link);

            }else{
                logger.info("current points " + startId + "," + stopId + ", part of a inner map link" );
                BackendInfo mapInfo = backendInfoService.getInfoByMapId(stopPoint.getMapId());
                float weight = backendService.getWeight(mapInfo, startPoint.getPointId(), stopPoint.getPointId());
//                // Request weight between points from backend
//                String url = "http://" + mapinfo.getHostname() + ":" + mapinfo.getPort() + "/" + startPoint.getPid() + "/" + stopPoint.getPid();
//                logger.info("--requesting from cost from:+" + url);
//
//                int weight = 1;
//                if(backendsEnabled) {
//                    JSONObject response = backendService.requestJsonObject(url);
//                    System.out.println("response: " + response.toString());
//                    weight = Integer.parseInt(response.get("cost").toString());
//                }else {
//                    weight = (int)(Math.random() * 10); // to test wo/ backends
//                }
                logger.info("--got weight: " + weight);
                // Add the inner weights that the map calculated
                path.addWeight(weight);
                // add a new job to the path
                Job job = new Job((long) startPoint.getPointId(), (long) stopPoint.getPointId(), mapInfo.getMapId(),weight);
                path.addJob(job);
            }


        }

        // TODO check if the endpoint is in the endpoint map
        int lastIndex = pointpairs.length -1;
        int destinationId = mapPointRepository.findByPointIdAndMap_Id(stopPid, stopMapId).getId();
        if(destinationId != pointpairs[lastIndex]){
            logger.info("EndPoint is on the same map: Last Job is within the end map");
//            int userPid = transitPointService.getPointWithId(userStartId).getPid(); // redundant call to get userStartPid

            BackendInfo mapInfo = backendInfoService.getInfoByMapId(stopMapId);
            int lastTransitPid = mapPointRepository.findById(pointpairs[lastIndex]).getPointId();

            // TODO get the internal cost by, identify by mapid
            float weight = backendService.getWeight(mapInfo, lastTransitPid, stopPid);
            path.addWeight(weight);
            path.addJob(lastTransitPid, stopPid, stopMapId ,weight);
        }


        path.setTransitPath(transitPath);
        logger.info(linkLog);
        path.addWeight(path.getTotalTransitWeight());
        return path;
    }

    // Eerst gaf de A* service de paden door in de vorm van een array van linkids. Hier ontstand het probleem dat we onmogelijk de richting konden bepalen
    // daarom is er overgegaan op point ids op volgorde .
    @Deprecated
    public Path makePathFromLinkIds(Integer[] linkids, int startpid, int startmapid){
        String linkLog = "Links: ";
        Path path = new Path();
        ArrayList<Link> transitPath = new ArrayList<Link>();
        // Get a the full TranistLink Objects
        for (int i = 0; i < linkids.length; i++) {
            int linkId = linkids[i];
            linkLog += linkId+",";
            Link transitLink = linkService.getLinkWithId(linkId);
            transitPath.add(transitLink);
        }
        logger.info(linkLog);
        path.setTransitPath(transitPath);


        // check if there needs to be a job from the starting pid to a point on the same map.
        int firstStartid = transitPath.get(0).getPointA();
        MapPoint userStartPoint = mapPointRepository.findByPointIdAndMap_Id(startpid, startmapid);
        // if the starting id is not equal to the path's starting point id where the user wants to depart from:
        if(firstStartid != userStartPoint.getId()){
            logger.info("-startpoint on same map: TODO GET THE COST ");
            // TODO request cost of navigation to point on same map
            // TODO better naming
            int startLinkPid = mapPointRepository.findById(firstStartid).getPointId();
            path.addJob(startpid, startLinkPid, startmapid ,1);
        }

        // construct path object
        // Get the combined weight of the inner map links on the route
        // add these to the path object together with the topmap weights
        int length = transitPath.size() -1;
        logger.info("length " + length);
        for (int i = 0; i < length; i++) {
            // endpoint of one link and startpoint of second link should be on the same map


            int stopid = transitPath.get(i).getPointB();
            int startid = transitPath.get(i+1).getPointA();



            logger.info("-handling link" + stopid +"-"+ startid);

            // Check if the startpoint of the optimal path is another point on the same map,
            // -> if so dispatch first jobfrom the current point to the path


            // get points of link from database
            MapPoint stopPoint = mapPointRepository.findById(stopid);
            MapPoint startPoint = mapPointRepository.findById(startid);

            // TODO check if points belong to the same map
            // TODO build in check for flagpoints (destination) and handle accordingly

            // Get the connection info of the map where the points belong to
            BackendInfo mapInfo = backendInfoService.getInfoByMapId(stopPoint.getMapId());
            float weight = backendService.getWeight(mapInfo, stopPoint.getPointId(), startPoint.getPointId());
//            // Request weight between points from backend
//            String url = "http://" + mapinfo.getHostname() + ":" + mapinfo.getPort() + "/" + stopPoint.getPid() + "/" + startPoint.getPid();
//            logger.info("--requesting from cost from:+" + url);
//
//            int weight = 1;
//            if(backendsEnabled) {
//
//                JSONObject response = backendService.requestJsonObject(url);
//                System.out.println("response: " + response.toString());
//                 weight = Integer.parseInt(response.get("cost").toString());
//            }else {
//                 weight = (int)(Math.random() * 10); // to test wo/ backends
//            }
//            logger.info("--got weight: " + weight);

            // Add the inner weights that the map calculated
            path.addWeight(weight);

            // add a new job to the path
            Job job = new Job((long) startPoint.getPointId(), (long) stopPoint.getPointId(), mapInfo.getMapId(),weight);
            path.addJob(job);
        }
        // add the total weight of the transitlinks, now we have the complete weight of the path
        path.addWeight(path.getTotalTransitWeight());
        return path;
    }
}
