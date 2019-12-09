package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.BackendInfo;
import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.models.JobList;
import be.uantwerpen.sc.models.Path;
import be.uantwerpen.sc.models.map.Map;
import be.uantwerpen.sc.models.map.MapPoint;
import be.uantwerpen.sc.services.*;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Defines functions that can be called to control different kinds of maps,
 * delegates mostly to MapControlService.
 *
 */
@RestController
@RequestMapping(value = "/map/")
public class MapController {

    private static final Logger logger = LoggerFactory.getLogger(MapController.class);

//    @Autowired
//    private BackendInfoRepository backendInfoRepository;

    @Autowired
    private BackendInfoService backendInfoService;

//    @Autowired
//    private BackendService backendService;
//
//    @Autowired
//    private TransitPointRepository pointRepository;

    @Autowired
    private JobListService jobListService;

    @Autowired
    private AStarService aStarService;

    @Autowired
    private PathService pathService;

    @Autowired
    private MapService mapService;

    @Value("${backends.enabled}")
    boolean backendsEnabled;

    /**
     * Returns a map for the given type of vehicle
     * alternatively returns a map for the visualisation with variable 'visual'
     * <p>
     * Rework 2019-2020
     * Before this rework, the map data was loaded from a textfile for which
     * the path was hardcoded in this function. We reworked this to a map that is
     * stored in the MySQL>Backbone database. See documentation for database layout.
     * We kept the type==visual check from our predecessors because I honestly
     * don't know what it is all about. The 'return "U014"' has something to do with
     * a map of room U.014 for the racecar. Not pretty clear. [Wouter]
     *
     * @param type Can be either for visualization in MaaS or for a drone, robot or F1Car
     * @return A map generated by the mapControlService
     */
    @Produces("application/json")
    @RequestMapping(value = "stringmapjson/{type}", method = RequestMethod.GET)
    public String customMapStringJson(@PathVariable("type") String type) {
        if (type.equals("visual")) {
            // Load maps
            // Currently restricted to the maps that were present in the oldskool Json-file
            // This are maps {1,2,3}
            List<Map> maps = new ArrayList<>();
            maps.add(mapService.getMapById(1));
            maps.add(mapService.getMapById(2));
            maps.add(mapService.getMapById(3));

            // Root object of Json
            JsonObjectBuilder jsonRoot = Json.createObjectBuilder();
            // Map array of root object
            JsonArrayBuilder jsonMaps = Json.createArrayBuilder();

            for (Map map : maps) {
                // Generate Json structure for this map
                // Set properties
                JsonObjectBuilder jsonMap = Json.createObjectBuilder();
                jsonMap.add("mapId", map.getId());
                jsonMap.add("offsetX", map.getOffsetX());
                jsonMap.add("offsetY", map.getOffsetY());
                jsonMap.add("access", map.getAccess().getName());

                // Do stuff with the points:
                // 1) Generate pointslist
                // 2) for each point, generate neighbours
                List<MapPoint> points = mapService.getPointsByMapId(map.getId());
                JsonArrayBuilder jsonPoints = Json.createArrayBuilder();
                for (MapPoint point : points) {
                    // Parse a point
                    // Construct neighboursproperty for this point
                    // All other points for this map are neighbours
                    List<MapPoint> neighbours = getNeighbours(point, points);
                    JsonArrayBuilder neighboursBuilder = Json.createArrayBuilder();
                    neighbours.forEach(item -> {
                        // Create a neighbour-entry
                        neighboursBuilder.add(Json.createObjectBuilder().add("neighbour", item.getPointId()).build());
                    });

                    // Generate Json structure for this single point
                    JsonObjectBuilder jsonPoint = Json.createObjectBuilder()
                            .add("id", point.getPointId())
                            .add("x", point.getX())
                            .add("y", point.getY());
                    if (!(point.getMapPointType() == null)) {
                        jsonPoint.add("type", point.getMapPointType().getType());
                    }
                    jsonPoint.add("neighbours", neighboursBuilder.build());

                    // Add this point to the point collection of the map
                    jsonPoints.add(jsonPoint.build());
                }
                // Create pointsarray for map
                jsonMap.add("pointList", jsonPoints.build());
                // Add map to map collection
                jsonMaps.add(jsonMap.build());
            }
            // Add map collection to root element
            jsonRoot.add("maplist", jsonMaps.build());

            // Return the root object
            return jsonRoot.build().toString();
        }
        //further handling in customMap when type is a car, bot, drone, ...
        return "U014";
    }

    /**
     * Constructs a list with neighbourpoints for the central point.
     *
     * @param centralPoint point which needs neighbours
     * @param allPoints    list in which neighbours are searched
     * @return neighbour list
     */
    List<MapPoint> getNeighbours(MapPoint centralPoint, List<MapPoint> allPoints) {
        return allPoints.stream().filter(mapPoint -> !mapPoint.equals(centralPoint)).collect(Collectors.toList());
    }


    /**
     * Endpoint for a pathplanning from {startpoint:{pid, mapid}} to {stoppoint:{pid, mapid}}
     */
    @RequestMapping(value = "planpath", method = RequestMethod.GET)
    public JSONObject planPath(@RequestParam int startpid, @RequestParam int startmapid, @RequestParam int stoppid, @RequestParam int stopmapid) {
        JobList jobList = new JobList();
        JSONObject response = new JSONObject();
        ArrayList<Path> pathRank = new ArrayList<Path>();

        // get list of possible paths (link ids) from A*

        if (startmapid == stopmapid) {
            // build job
            Path onlyPath = new Path();
            Job job = new Job((long) startpid, (long) stoppid, stopmapid);
            onlyPath.addJob(job);
            // save and execute job
            jobList = onlyPath.getJobList();
            jobListService.saveJobList(jobList);
            logger.info("start/stop point both in map:" + startmapid + ", dispatching job between [" + startpid + "-" + stoppid + "]on map ");
            dispatchToBackend();
            response.put("status", "dispatching");
            // send back the delivery id to the MaaS
            response.put("deliveryid", jobList.getId());
            return response;
        }

        // Determine (5 for now) best TransitMap routes, returns a list of integer pairs
        List<Integer[]> possiblePaths = aStarService.determinePath(startpid, startmapid, stoppid, stopmapid);

        // no paths available, send back deliveryid -1 to let the MaaS know.
        if (possiblePaths == null || possiblePaths.isEmpty()) {
            response.put("status", "No paths found");
            response.put("deliveryid", -1);
            return response;
        }

        // check if size is 1 en linkid is -1 => ligt op dezelfde map, 1 job uitsturen naar die map
        if ((possiblePaths.size() == 1) && (possiblePaths.get(0)[0] == -1)) {
            // TODO catch if something went wrong in the aStar pathplanning
        }

        // Process all paths given by AStar,
        for (Integer[] pointPairs : possiblePaths) {
            // create a path (full transitlinks, jobs, requested weights) from the array of linkIds
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

        // Convert the chosen path to a jobs and save them as a job list.
        // TODO Future Work: Relay the 5 best possible paths to the user, make him/her choose
        int chosenPath = 0; // here we just choose the cheapest path;
        jobList = pathRank.get(chosenPath).getJobList();
        logger.info("dispatching jobList w/ rank: " + chosenPath);
        jobListService.saveJobList(jobList);
        dispatchToBackend();
        response.put("status", "dispatching");
        response.put("deliveryid", jobList.getId());
        return response;
    }

    @RequestMapping(value = "getTrafficLightStats", method = RequestMethod.GET)
    @ResponseBody
    public String getTrafficLightStats() throws IOException {
        BackendInfo backendInfo = backendInfoService.getByName("Robot");
        String stringUrl = "http://";
        stringUrl += backendInfo.getHostname() + ":" + backendInfo.getPort() + "/tlight/getAll";

        return readStringFromURL(stringUrl);
    }

    private String readStringFromURL(String requestURL) throws IOException {
        try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    private void dispatchToBackend() {
        if (backendsEnabled) {
            try {
                jobListService.dispatchToBackend();
            } catch (Exception e) {
                logger.warn("Dispatching failed");
                e.printStackTrace();
            }
        }
    }
}