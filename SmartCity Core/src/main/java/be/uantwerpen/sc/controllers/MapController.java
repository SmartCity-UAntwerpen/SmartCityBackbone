package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.*;
import be.uantwerpen.sc.models.map.CustomMap;

import be.uantwerpen.sc.repositories.BackendInfoRepository;
import be.uantwerpen.sc.repositories.TransitPointRepository;
import be.uantwerpen.sc.services.*;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Defines functions that can be called to control different kinds of maps,
 * delegates mostly to MapControlService.
 *
 * @see MapControlService
 */
@RestController
@RequestMapping(value = "/map/")
public class MapController {

    private static final Logger logger = LoggerFactory.getLogger(MapController.class);

    @Autowired
    private MapControlService mapControlService;

    @Autowired
    private BackendInfoRepository backendInfoRepository;

    @Autowired
    private BackendInfoService backendInfoService;

    @Autowired
    private BackendService backendService;

    @Autowired
    private TransitPointRepository pointRepository;

    @Autowired
    private JobListService jobListService;

    @Autowired
    private TransitLinkService transitLinkService;

    @Autowired
    private AStarService aStarService;
    /**
     * Returns a map for the given type of vehicle
     * alternatively returns a map for the visualisation with variable 'visual'
     *
     * @param type Can be either for visualization in MaaS or for a drone, robot or F1Car
     * @return A map generated by the mapControlService
     */
    @Produces("application/json")
    @RequestMapping(value = "stringmapjson/{type}", method = RequestMethod.GET)
    public String customMapStringJson(@PathVariable("type") String type) {
        if (type.equals("visual")) {
            mapControlService.buildTopMapJson();
            return mapControlService.buildCustomMapJson("visual").getVisualPointsString();
        }
        //further handling in customMap when type is a car, bot, drone, ...
        return "U014";
//        return mapControlService.buildCustomMapJson(type).toString(type);
    }

    /**
     * Creates a JSON String of links for the MaaS backbone
     *
     * @return JSON String of links for the MaaS backbone
     */
    @Produces("application/json")
    @RequestMapping(value = "topmapjson/links", method = RequestMethod.GET)
    public String topLinksStringJson() {
        CustomMap map = mapControlService.buildTopMapJson();
        return map.getTopMapLinksString();
    }

    //creates a string of points for the MaaS backbone
    @Produces("application/json")
    @RequestMapping(value = "topmapjson/points", method = RequestMethod.GET)
    public String topPointsStringJson() {
        CustomMap map = mapControlService.buildTopMapJson();
        return map.getTopMapPointsString();
    }

    /**
     * Endpoint for a pathplanning from {startpoint:{pid, mapid}} to {stoppoint:{pid, mapid}}
     */
    @RequestMapping(value = "planpath", method = RequestMethod.GET)
    public JSONObject planPath(@RequestParam int startpid, @RequestParam int startmapid, @RequestParam int stoppid, @RequestParam int stopmapid){
        JobList jobList = new JobList();
        JSONObject response = new JSONObject();
        JSONObject fullResponse = new JSONObject();
        HashMap<Integer, Path> pathsHashMap = new HashMap<Integer, Path>();
        ArrayList<Path> pathRank = new ArrayList<Path>();

        // get list of possible paths (link ids) from A*
        List<Integer[]> possiblePaths = new ArrayList<>();
//        Integer[] path1 = {10,13,16}; // dummy route
//        possiblePaths.add(path1); // dummy route
        possiblePaths = aStarService.determinePath(startpid, startmapid, stoppid, stopmapid);
        // TODO check if lenght = en linkid = -1 => ligt op dezelfde map, 1 job uitsturen naar die map

        // go over all possible paths
        for(Integer[] links : possiblePaths) {
            Path path = new Path();
            ArrayList<TransitLink> transitPath = new ArrayList<TransitLink>();
            // Get a the full TranistLink Objects
            for (int i = 0; i < links.length; i++) {
                int linkId = links[i];
                TransitLink transitLink = transitLinkService.getLinkWithId(linkId);
                transitPath.add(transitLink);
                System.out.println(transitLink.getStartId());
            }
            path.setTransitPath(transitPath);

            // Get the combined weight of the inner map links on the route
            // -1 to stop on the last route and handle the last destination separtate
            int length = transitPath.size() - 1;
            for (int i = 0; i < length; i++) {

                // endpoint of one link and startpoint of second link should be on the same map
                int stopid = transitPath.get(i).getStopId();
                int startid = transitPath.get(i + 1).getStartId();
                // get points of link from database
                TransitPoint stopPoint = pointRepository.findById(stopid);
                TransitPoint startPoint = pointRepository.findById(startid);
                System.out.println("stoppoint Transitmap Id " + stopPoint.getMapid());
                System.out.println("startpoint Transitmap Id " + startPoint.getMapid());

                // TODO check if points belong to the same map
                // TODO build in check for flagpoints (destination) and handle accordingly

                // Get the connection info of the map where the points belong to
                BackendInfo mapinfo = backendInfoService.getInfoByMapId(stopPoint.getMapid());
                System.out.println(mapinfo.getHostname());

                // Request weight between points from backend
                String url = mapinfo.getHostname() + ":" + mapinfo.getPort() + "/" + startPoint.getPid() + "/" + stopPoint.getPid();
                System.out.println(url);
                response = backendService.requestJsonObject(url);
                int weight =  (int)response.get("weight"); // TODO better way to make it int
//                int weight = (int)(Math.random() * 10); // to test wo/ backends
                // Add the inner weights that the map calculated
                path.addWeight(weight);
                System.out.println("Weight: " + i + ": " + weight);

                // TODO add link to jobList
                // TODO move this to after top hasmap sort
                Job job = new Job((long) startPoint.getPid(), (long) stopPoint.getPid(), mapinfo.getMapId());
                System.out.println(job.toString());

                path.addJob(job);
//                jobList.addJob(job);
            }
            // add the total weight of the transitlinks, now we have the complete weight of the path
            path.addWeight(path.getTotalTransitWeight());
            fullResponse.put("weight", path.getWeight());

            // rank the path based on it's weight, if paths have the same wieght increment the key
            // set the default rank as the last index of the ranking
            int rank = pathRank.size();
            for(int i = 0; i < pathRank.size(); i++){
                if(path.getWeight() <= pathRank.get(i).getWeight() ){
                   rank = i;
                   break;
                }

            }
            pathRank.add(rank, path);

//            while(pathsHashMap.containsKey(pathWeightScore)){
//                pathWeightScore +=1;
//            }
        }

        // Sort the paths according to weight
        for(int i = 0; i < pathRank.size(); i++){
            System.out.println(i + ") weight of link: " + pathRank.get(i).getWeight());
            System.out.println(pathRank.get(i).toString() );
        }

        // convert the chosen path to a jobs and save them as a job list.
        int chosenPath = 0;
        jobList = pathRank.get(chosenPath).getJobList();
        System.out.println("dispatching jobList w/ rank: " + chosenPath);
        jobListService.saveOrder(jobList);
//        jobListService.dispatchToCore();

        return fullResponse;
    }

    //TODO Check if we receive a JSONobject from the robot backend

    @RequestMapping(value = "getTrafficLightStats", method = RequestMethod.GET)
    public JSONObject getTrafficLightStats()
    {
        // Get the list of traffic lights and their status from the Robot backend end send it back to the MaaS

        // Get the backendInfo object from the backendinfo service of the robot backend
        BackendInfo backendInfo = backendInfoService.getByName("Robot");

        String stringUrl = "http://";
        stringUrl += backendInfo.getHostname() + ":" + backendInfo.getPort() + ""; // TODO Check with the Robot team which endpoint they have made

        return backendService.requestJsonObject(stringUrl);
    }
}
