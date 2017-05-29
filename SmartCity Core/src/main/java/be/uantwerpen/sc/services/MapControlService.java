package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.models.map.*;
import be.uantwerpen.sc.models.points.Point;
import be.uantwerpen.sc.repositories.LinkRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Niels on 14/04/2016.
 */
@Service
public class MapControlService
{
    @Autowired
    private PointControlService pointControlService;

    @Autowired
    private LinkControlService linkControlService;

    @Autowired
    private BotControlService botControlService;

    @Autowired
    private TrafficLightControlService trafficLightControlService;

    private CustomMap topMap;
    private boolean topMapCreated = false;

    public Map buildMap()
    {
        Map map = new Map();

        List<Link> linkEntityList = linkControlService.getAllLinks();

        for(Point point : pointControlService.getAllPoints())
        {
            Node node = new Node(point);
            List<Link> targetLinks = linkEntityList.stream().filter(item -> Objects.equals(item.getStartPoint().getId(), node.getNodeId())).collect(Collectors.toList());

            node.setNeighbours(targetLinks);
            map.addNode(node);
        }

    //    map.setBotEntities(botControlService.getAllBots());
        map.setTrafficlightEntity(trafficLightControlService.getAllTrafficLights());

        return map;
    }

    public MapJson buildMapJson()
    {
        MapJson mapJson = new MapJson();

        List<Link> linkEntityList = linkControlService.getAllLinks();

        for(Point point : pointControlService.getAllPoints())
        {
            NodeJson nodeJson = new NodeJson(point);

            List<Neighbour> neighbourList = new ArrayList<Neighbour>();

            for(Link link: linkEntityList)
            {
                if((link.getStartPoint().getId()) == (nodeJson.getPointEntity().getId()))
                {
                    neighbourList.add(new Neighbour(link));
                }
            }

            nodeJson.setNeighbours(neighbourList);
            mapJson.addNodeJson(nodeJson);
        }

        mapJson.setSize(mapJson.getNodeJsons().size());

        return mapJson;
    }

    public CustomMap buildCustomMapJson(String type)
    {
        CustomMap map = new CustomMap();

        for(Link link : linkControlService.getAllLinks()){
            if(type.equals("top") && !link.getAccess().contains("Top")){
                map.addLink(link);
            }else if(type.equals(link.getAccess())){
                map.addLink(link);
            }
        }

        for(Point point : pointControlService.getAllPoints())
        {
            if(type.equals(point.getAccess())){
            map.addPoint(point);
            }
        }

        return map;
    }

    //this function creates a top view of the hubs and links between individual hubs
    public CustomMap buildTopMapJson(){

        if(topMapCreated){
            return topMap;
        }else {
            CustomMap map = new CustomMap();

            for (Point point : pointControlService.getAllPoints()) {
                if (point.getHub() != 0) {
                    map.addPoint(point);
                }
            }

            for (Point point : map.getPointList()) {

                List<Link> tempList = new ArrayList<Link>();

                ////create paths between hubs
                //narrow down the links to type
                for (Link link : linkControlService.getAllLinks()) {
                    if (point.getAccess().equals(link.getAccess())) {
                        tempList.add(link);
                    }
                }

                //create links
                map = createTopLinks(point, point.getHub(), tempList, map);


                //create interconnection within hub
                Long idHub = point.getId();
                for (Point otherPoint : map.getPointList()) {
                    if (idHub.equals(otherPoint.getHub()) && !point.equals(otherPoint)) {
                        Link topLink = new Link();
                        topLink.setLength(new Long(1));
                        topLink.setAccess("wait");
                        topLink.setWeight(1);
                        topLink.setStartPoint(point);
                        topLink.setStopPoint(otherPoint);
                        linkControlService.saveLink(topLink);
                        map.addLink(topLink);
                    }
                }
            }

            topMap = map;
            topMapCreated = true;

            return map;
        }
    }

    private CustomMap createTopLinks(Point pointStart, Long hub, List<Link> links, CustomMap map){
        Point otherPoint;

        for(Link link : links){
            //get all links beginning from current point

            if(link.getStartPoint().getId().equals(pointStart.getId())){
                //check if the link ends in an endpoint, if it does, create toplink

                if(link.getStopPoint().getType().equals("ENDPOINT")){
                    //check if arrival point is same as start
                    if(link.getStopPoint().getHub().equals(hub)) {
                        //do nothing
                    }else {
                        for (Point point : pointControlService.getAllPoints()) {
                            if (point.equals(link.getStopPoint())) {
                                otherPoint = point;
                                Link topLink = new Link();
                                topLink.setLength(new Long(1));
                                topLink.setAccess(link.getAccess() + "Top");
                                topLink.setWeight(1);
                                topLink.setStartPoint(pointStart);
                                topLink.setStopPoint(otherPoint);
                                linkControlService.saveLink(topLink);
                                map.addLink(topLink);
                            }
                        }
                    }
                }else{
                    //remove handled links from array to prevent loops
                    ArrayList<Link> newlinks = new ArrayList<Link>();
                    for(Link newlink : links){
                        if(!newlink.equals(link)){
                            newlinks.add(newlink);
                        }
                    }
                    createTopLinks(link.getStopPoint(), hub, newlinks, map);
                }
            }
        }
        return map;
    }
}
