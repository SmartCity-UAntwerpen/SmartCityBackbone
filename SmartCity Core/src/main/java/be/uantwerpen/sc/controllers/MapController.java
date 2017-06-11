package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.map.CustomMap;
import be.uantwerpen.sc.services.*;
import be.uantwerpen.sc.tools.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by Niels on 3/04/2016.
 */
@RestController
@RequestMapping(value = "/map/")
public class MapController
{
    @Autowired
    private MapControlService mapControlService;

    //returns a map for the given type of vehicle
    //alternatively returns a map for the visualisation with variable 'visual'
    @Produces("application/json")
    @RequestMapping(value = "stringmapjson/{type}", method = RequestMethod.GET)
    public String customMapStringJson(@PathVariable("type") String type)
    {
        if(type.equals("visual")){
            mapControlService.buildTopMapJson();
            return mapControlService.buildCustomMapJson("visual").getVisualPointsString();
        }
        return mapControlService.buildCustomMapJson(type).toString(type);
    }

    //creates a string of links for the MaaS backbone
    @Produces("application/json")
    @RequestMapping(value = "topmapjson/links", method = RequestMethod.GET)
    public String topLinksStringJson()
    {
        CustomMap map = mapControlService.buildTopMapJson();
        return map.getTopMapLinksString();
    }

    //creates a string of points for the MaaS backbone
    @Produces("application/json")
    @RequestMapping(value = "topmapjson/points", method = RequestMethod.GET)
    public String topPointsStringJson()
    {
        CustomMap map = mapControlService.buildTopMapJson();
        return map.getTopMapPointsString();
    }
    
}
