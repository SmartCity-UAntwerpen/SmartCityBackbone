package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.*;
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
    TransitLinkService transitLinkService;
    @Autowired
    TransitPointRepository pointRepository;
    @Autowired
    TransitPointService transitPointService;
    @Autowired
    BackendInfoService backendInfoService;
    @Autowired
    BackendService backendService;
    @Value("${enable.dispatching}")
    boolean dispatchingEnabled;

    public PathService(){

    }


    public Path makePathFromPointPairs(Integer[] pointpairs, int startpid, int startmapid){
        Path path = new Path();
        ArrayList<TransitLink> transitPath = new ArrayList<TransitLink>();

        //TODO check for a job on the starting map


        String linkLog = "TransitLink ids: ";
        for(int i = 0; i < pointpairs.length; i+=2){
            int startId = pointpairs[i];
            int stopId = pointpairs[i+1];
            TransitPoint startPoint = transitPointService.getPointWithId(startId);
            TransitPoint stopPoint = transitPointService.getPointWithId(stopId);


            if(startPoint.getMapid() != stopPoint.getMapid()){
                TransitLink transitLink = transitLinkService.getLinkWithStartidAndStopid(startId,stopId);
                linkLog+= transitLink.getId() + "-";
                transitPath.add(transitLink);

            }else{
                BackendInfo mapinfo = backendInfoService.getInfoByMapId(stopPoint.getMapid());
                // Request weight between points from backend
                String url = "http://" + mapinfo.getHostname() + ":" + mapinfo.getPort() + "/" + startPoint.getPid() + "/" + stopPoint.getPid();
                logger.info("--requesting from cost from:+" + url);

                int weight = 1;
                if(dispatchingEnabled) {
                    JSONObject response = backendService.requestJsonObject(url);
                    System.out.println("response: " + response.toString());
                    weight = Integer.parseInt(response.get("cost").toString());
                }else {
                    weight = (int)(Math.random() * 10); // to test wo/ backends
                }
                logger.info("--got weight: " + weight);
                // Add the inner weights that the map calculated
                path.addWeight(weight);
                // add a new job to the path
                Job job = new Job((long) startPoint.getPid(), (long) stopPoint.getPid(), mapinfo.getMapId());
                path.addJob(job);
            }
            path.setTransitPath(transitPath);

        }
        logger.info(linkLog);
        path.addWeight(path.getTotalTransitWeight());
        return path;
    }
    public Path makePathFromLinkIds(Integer[] linkids, int startpid, int startmapid){
        String linkLog = "Links: ";
        Path path = new Path();
        ArrayList<TransitLink> transitPath = new ArrayList<TransitLink>();
        // Get a the full TranistLink Objects
        for (int i = 0; i < linkids.length; i++) {
            int linkId = linkids[i];
            linkLog += linkId+",";
            TransitLink transitLink = transitLinkService.getLinkWithId(linkId);
            transitPath.add(transitLink);
        }
        logger.info(linkLog);
        path.setTransitPath(transitPath);


        // check if there needs to be a job from the starting pid to a point on the same map.
        int firstStartid = transitPath.get(0).getStartId();
        TransitPoint userStartPoint = transitPointService.getPointWithMapidAndPid(startpid, startmapid);
        // if the starting id is not equal to the path's starting point id where the user wants to depart from:
        if(firstStartid != userStartPoint.getId()){
            logger.info("-startpoint on same map: TODO GET THE COST ");
            // TODO request cost of navigation to point on same map
            // TODO better naming
            int startLinkPid = transitPointService.getPointWithId(firstStartid).getPid();
            path.addJob(startpid, startLinkPid, startmapid );
        }

        // construct path object
        // Get the combined weight of the inner map links on the route
        // add these to the path object together with the topmap weights
        int length = transitPath.size() -1;
        logger.info("length " + length);
        for (int i = 0; i < length; i++) {
            // endpoint of one link and startpoint of second link should be on the same map


            int stopid = transitPath.get(i).getStopId();
            int startid = transitPath.get(i+1).getStartId();



            logger.info("-handling link" + stopid +"-"+ startid);

            // Check if the startpoint of the optimal path is another point on the same map,
            // -> if so dispatch first jobfrom the current point to the path


            // get points of link from database
            TransitPoint stopPoint = pointRepository.findById(stopid);
            TransitPoint startPoint = pointRepository.findById(startid);

            // TODO check if points belong to the same map
            // TODO build in check for flagpoints (destination) and handle accordingly

            // Get the connection info of the map where the points belong to
            BackendInfo mapinfo = backendInfoService.getInfoByMapId(stopPoint.getMapid());

            // Request weight between points from backend
            String url = "http://" + mapinfo.getHostname() + ":" + mapinfo.getPort() + "/" + stopPoint.getPid() + "/" + startPoint.getPid();
            logger.info("--requesting from cost from:+" + url);

            int weight = 1;
            if(dispatchingEnabled) {

                JSONObject response = backendService.requestJsonObject(url);
                System.out.println("response: " + response.toString());
                 weight = Integer.parseInt(response.get("cost").toString());
            }else {
                 weight = (int)(Math.random() * 10); // to test wo/ backends
            }
            logger.info("--got weight: " + weight);

            // Add the inner weights that the map calculated
            path.addWeight(weight);

            // add a new job to the path
            Job job = new Job((long) startPoint.getPid(), (long) stopPoint.getPid(), mapinfo.getMapId());
            path.addJob(job);
        }
        // add the total weight of the transitlinks, now we have the complete weight of the path
        path.addWeight(path.getTotalTransitWeight());
        return path;
    }
}
