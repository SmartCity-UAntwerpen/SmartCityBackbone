package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.BackendInfo;
import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.models.JobList;
import be.uantwerpen.sc.models.Path;
import be.uantwerpen.sc.models.map.CustomMap;
import be.uantwerpen.sc.repositories.BackendInfoRepository;
import be.uantwerpen.sc.repositories.TransitPointRepository;
import be.uantwerpen.sc.services.*;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    private TransitPointService transitPointService;

    @Autowired
    private AStarService aStarService;

    @Autowired
    private PathService pathService;

    @Value("${enable.dispatching}")
    boolean dispatchingEnabled;
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
            try {
                byte[] encoded = Files.readAllBytes(Paths.get("stringmapjsonNEW.txt"));
                return new String(encoded, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public JSONObject planPath(@RequestParam int startpid, @RequestParam int startmapid, @RequestParam int stoppid, @RequestParam int stopmapid) {
        JobList jobList = new JobList();
        JSONObject response = new JSONObject();
        JSONObject fullResponse = new JSONObject();
        ArrayList<Path> pathRank = new ArrayList<Path>();

        // get list of possible paths (link ids) from A*
        List<Integer[]> possiblePaths = new ArrayList<>();
        possiblePaths = aStarService.determinePath(startpid, startmapid, stoppid, stopmapid);
        // check if size is 1 en linkid is -1 => ligt op dezelfde map, 1 job uitsturen naar die map
        if ((possiblePaths.size() == 1) && (possiblePaths.get(0)[0] == -1)) {
            Path onlyPath = new Path();
            Job job = new Job((long) startpid, (long) stoppid, stopmapid);
            onlyPath.addJob(job);
            jobList = onlyPath.getJobList();
            jobListService.saveJobList(jobList);
            if (dispatchingEnabled) {
                logger.info("Dispatching...");
                try {
                    jobListService.dispatchToBackend();
                } catch (Exception e) {
                    logger.warn("Dispatching failed");
                    e.printStackTrace();
                }
            }

            logger.info("message", "points lie in same map, dispatching job between [" + startpid + "-" + stoppid + "]on map " + startmapid);
            return fullResponse;
        }

        // Process all paths given by AStar,
        for (Integer[] pointPairs : possiblePaths) {
            // create a path (full transitlinks, jobs, requested weights) from the array of linkIds
//            Path path = pathService.makePathFromLinkIds(links, startpid, startmapid);
            Path path = pathService.makePathFromPointPairs(pointPairs, startpid, startmapid, stoppid, stopmapid);
            // rank the path based on it's weight, if paths have the same weight, increment the key
            // set the default rank as the last index of the ranking
            int rank = pathRank.size();
            for (int i = 0; i < pathRank.size(); i++) {
                if (path.getWeight() <= pathRank.get(i).getWeight()) {
                    rank = i;
                    break;
                }
            }
            pathRank.add(rank, path);
        }

        // print the pathranking
        for (int i = 0; i < pathRank.size(); i++) {
            System.out.println(i + ") weight of path: " + pathRank.get(i).getWeight());
            System.out.println(pathRank.get(i).toString());
        }

        // convert the chosen path to a jobs and save them as a job list.
        // TODO Future Work: Relay the 5 best possible paths to the user, make him/her choose
        int chosenPath = 0; // here we just choose the cheapest path;
        jobList = pathRank.get(chosenPath).getJobList();
        System.out.println("dispatching jobList w/ rank: " + chosenPath);

        jobListService.saveJobList(jobList);

        if (dispatchingEnabled) {
            logger.info("Dispatching...");
            try {
                jobListService.dispatchToBackend();
            } catch (Exception e) {
                logger.warn("Dispatching failed");
                e.printStackTrace();
            }
        }

        return fullResponse;
    }

    //TODO Check if we receive a JSONobject from the robot backend
    @RequestMapping(value = "getTrafficLightStats", method = RequestMethod.GET)
    public JSONObject getTrafficLightStats()
    {
        /*  Get the list of traffic lights and their status from the Robot backend end send it back to the MaaS
            Get the backendInfo object from the backendinfo service of the robot backend
        */

        BackendInfo backendInfo = backendInfoService.getByName("Robot");
        String stringUrl = "http://";
        stringUrl += backendInfo.getHostname() + ":" + backendInfo.getPort() + "/tlight/getAll"; // TODO Check with the Robot team which endpoint they have made

        return backendService.requestJsonObject(stringUrl);
    }
}