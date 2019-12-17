package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.Delivery;

import be.uantwerpen.sc.services.DeliveryService;
import org.apache.coyote.Response;

import be.uantwerpen.sc.models.Path;
import be.uantwerpen.sc.models.jobs.Job;
import be.uantwerpen.sc.models.jobs.JobList;
import be.uantwerpen.sc.services.*;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/deliveries/")
public class DeliveryController {

    private static final Logger logger = LoggerFactory.getLogger(MapController.class);

    @Autowired
    private DeliveryService deliveryService;

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
    @RequestMapping(value = "getall", method = RequestMethod.GET)
    public List<Delivery> findAllDeliveries()
    {
        return deliveryService.findAll();
    }
    @RequestMapping(value = "createDelivery", method = RequestMethod.POST)
    public JSONObject Create(@RequestBody Delivery delivery)
    {
       Delivery del = deliveryService.save(delivery);
      // Delivery del = delivery;
        JSONObject response =  planPath(del.getPointA(),del.getMapA(),del.getPointB(),del.getMapB(),1);
        return response;
    }
    @RequestMapping(value = "savedelivery", method = RequestMethod.POST)
    public Delivery save(@RequestBody Delivery delivery)
    {
        return deliveryService.save(delivery);
    }

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public Delivery getDelivery(@PathVariable("id") Long id)
    {
        return deliveryService.getJob(id);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") Long id) {
        deliveryService.delete(id);
    }

    @RequestMapping(value = "deleteall", method = RequestMethod.POST)
    public void deleteAll()
    {
        deliveryService.deleteAll();
    }

//    private JsonObject planPath(Delivery delivery){
//        JobList jobList;
//
//    }

    public JSONObject planPath(int startpid, int startmapid, int stoppid, int stopmapid, long deliveryId) {
        JobList jobList;
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
            jobList.setIdDelivery((int)deliveryId);
            jobListService.saveJobList(jobList);
            logger.info("start/stop point both in map:" + startmapid + ", dispatching job between [" + startpid + "-" + stoppid + "]on map ");
            dispatchToBackend();
            response.put("status", "dispatching");
            // send back the delivery id to the MaaS
            //response.put("deliveryid", jobList.getId());
            return response;
        }

        // Determine (5 for now) best TransitMap routes, returns a list of integer pairs
        List<Integer[]> possiblePaths = aStarService.determinePath(startpid, startmapid, stoppid, stopmapid);

        // no paths available, send back deliveryid -1 to let the MaaS know.
        if (possiblePaths == null || possiblePaths.isEmpty()) {
            response.put("status", "No paths found");
            // response.put("deliveryid", -1);
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
