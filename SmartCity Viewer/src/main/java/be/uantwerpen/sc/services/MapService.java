package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.models.points.Point;
import be.uantwerpen.sc.tools.MapBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Created by Arthur on 3/04/2016.
 */
@Service
public class MapService
{
    @Value("${sc.core.ip:localhost}")
    private String serverCoreIP;

    @Value("#{new Integer(${sc.core.port}) ?: 1994}")
    private int serverCorePort;

    public MapBuilder mapBuilder;

    Link[] linkList;
    Point[] pointList;

    public MapService()
    {

    }

    @PostConstruct
    private void postConstruct()
    {
        //IP / port-values are initialised at the end of the constructor

        //Disabled for testing
        getMap();

        /*
        //Create Test Map
        PointEntity point1 = new PointEntity();
        point1.setPid(1);
        PointEntity point2 = new PointEntity();
        point2.setPid(2);
        PointEntity point3 = new PointEntity();
        point3.setPid(3);
        PointEntity point4 = new PointEntity();
        point4.setPid(4);
        
        pointList = new PointEntity[]{point1, point2, point3, point4};
        
        LinkEntity link = new LinkEntity();
        link.setLength(3);
        link.setLid(1);
        link.setStartDirection("E");
        link.setStopDirection("W");
        link.setStartId(point1);
        link.setStopId(point2);
        LinkEntity link2 = new LinkEntity();
        link2.setLength(2);
        link2.setLid(2);
        link2.setStartDirection("E");
        link2.setStopDirection("W");
        link2.setStartId(point2);
        link2.setStopId(point3);
        LinkEntity link3 = new LinkEntity();
        link3.setLength(4);
        link3.setLid(3);
        link3.setStartDirection("S");
        link3.setStopDirection("W");
        link3.setStartId(point2);
        link3.setStopId(point4);
        LinkEntity link4 = new LinkEntity();
        link4.setLength(2);
        link4.setLid(4);
        link4.setStartDirection("S");
        link4.setStopDirection("N");
        link4.setStartId(point3);
        link4.setStopId(point4);
        

        linkList = new LinkEntity[]{link, link2, link3, link4};
        */

        //build Sim Map
    //    mapBuilder = new MapBuilder(linkList, pointList);

        try
        {
            mapBuilder.buildMap();

            System.out.println("Map Created");
            mapBuilder.logMap();
        }
        catch(Exception e)
        {
            System.err.println("Could not generate map!");
            e.printStackTrace();
        }
    }

    private void getMap()
    {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Point[]> responseList;
        responseList = restTemplate.getForEntity("http://" + serverCoreIP + ":" + serverCorePort + "/point/", Point[].class);
        pointList = responseList.getBody();

        restTemplate = new RestTemplate();
        ResponseEntity<Link[]> responseList2;
        responseList2 = restTemplate.getForEntity("http://" + serverCoreIP + ":" + serverCorePort + "/link/", Link[].class);
        linkList = responseList2.getBody();
    }
}
