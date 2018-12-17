package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.BackendInfo;
import be.uantwerpen.sc.models.TransitLink;
import be.uantwerpen.sc.models.TransitPoint;
import be.uantwerpen.sc.models.map.CustomMap;
import be.uantwerpen.sc.repositories.BackendInfoRepository;
import be.uantwerpen.sc.repositories.PointRepository;
import be.uantwerpen.sc.repositories.TransitPointRepository;
import be.uantwerpen.sc.services.MapControlService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.util.ArrayList;

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
    private TransitPointRepository pointRepository;
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
        JSONObject response = new JSONObject();
        ArrayList<TransitLink> TransitMapRoute = new ArrayList<TransitLink>();
        ArrayList<TransitLink> fullRoute = new ArrayList<TransitLink>();


        // TODO determin n routes ( A* ?)

        // DUMMY ROUTE
        TransitMapRoute.add(new TransitLink(1,2,3,2));
        TransitMapRoute.add(new TransitLink(2,4,5,4));

        // TODO
        // -2 to stop on the last route and handle the last destination separtate
        int length = TransitMapRoute.size() - 1;
        for(int i = 0; i < length; i++){
            int stopid = TransitMapRoute.get(i).getStopId();
            int startid = TransitMapRoute.get(i+1).getStartId();

            // TODO get Points from database
            TransitPoint stopPoint = pointRepository.findById(stopid);
            TransitPoint startPoint = pointRepository.findById(startid);

            System.out.println("stoppoint " + stopPoint.getId());
            System.out.println("startpoint " + startPoint.getMapid());

            // TODO check if points belong to the same map

            // TODO
            BackendInfo mapinfo = backendInfoRepository.findByMapId(stopPoint.getMapid());
            System.out.println(mapinfo.getHostname());
        }
        // get cost to endpoint


        return response;
    }

}
