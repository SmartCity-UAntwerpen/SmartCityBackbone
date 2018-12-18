package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.*;
import be.uantwerpen.sc.models.map.CustomMap;
import be.uantwerpen.sc.repositories.BackendInfoRepository;
import be.uantwerpen.sc.repositories.PointRepository;
import be.uantwerpen.sc.repositories.TransitPointRepository;
import be.uantwerpen.sc.services.BackendInfoService;
import be.uantwerpen.sc.services.BackendService;
import be.uantwerpen.sc.services.JobListService;
import be.uantwerpen.sc.services.MapControlService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Defines functions that can be called to control different kinds of maps,
 * delegates mostly to MapControlService.
 *
 * @see MapControlService
 */
@RestController
@RequestMapping(value = "/map/")
public class MapController {
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
        return mapControlService.buildCustomMapJson(type).toString(type);
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
        ArrayList<TransitLink> TransitMapRoute = new ArrayList<TransitLink>();
        JobList jobList = new JobList();
        JSONObject response = new JSONObject();
        JSONObject fullResponse = new JSONObject();
        HashMap<Integer, ArrayList<TransitLink> > routesHashMap;

        int[] weights = {};

        // TODO determin n routes ( A* ?)
        // TODO DUMMY ROUTE
        TransitMapRoute.add(new TransitLink(1,1,2,2));
        TransitMapRoute.add(new TransitLink(2,3,1,4));
        TransitMapRoute.add(new TransitLink(3,2,3,4));


        // -2 to stop on the last route and handle the last destination separtate

        int length = TransitMapRoute.size() - 1;
        for(int i = 0; i < length; i++){
            int stopid = TransitMapRoute.get(i).getStopId();
            int startid = TransitMapRoute.get(i+1).getStartId();

            // get local points from database
            TransitPoint stopPoint = pointRepository.findById(stopid);
            TransitPoint startPoint = pointRepository.findById(startid);
            System.out.println("stoppoint Transitmap Id " + stopPoint.getMapid());
            System.out.println("startpoint Transitmap Id " + startPoint.getMapid());

            // TODO check if points belong to the same map

            // Get the connection info of the map where the points belong to
            BackendInfo mapinfo = backendInfoService.getInfoByMapId(stopPoint.getMapid());
            System.out.println(mapinfo.getHostname());

            // Request weight between points from backend
            String url = mapinfo.getHostname() + ":" + mapinfo.getPort() + "/" + startPoint.getPid() + "/" + stopPoint.getPid();
            System.out.println(url);

            response = backendService.requestJsonObject(url);
//            response = backendService.requestJsonObject("http://localhost:9000/link/testweight");

            System.out.println("Weight: " + i + ": " + response.get("weight"));

            // TODO add link to jobList
            // TODO move this to after top hasmap sort
            Job job = new Job((long)startPoint.getPid(), (long)stopPoint.getPid() , mapinfo.getMapId());
            jobList.addJob(job);
            System.out.println(job.toString());
        }

        response.put("jobist", jobList.toString() );
        System.out.println(jobList.getJobs().get(0).toString());

        // TODO use hashmap to <totalweigth, joblist>

        // TODO } end for each route


        // TODO sort hashmap to get best route

        // TODO construct joblist out of chosen route ( best route)

        // save the chosen job list.
        jobListService.saveOrder(jobList);
        jobListService.dispatchToCore();

        return fullResponse;
    }

}
