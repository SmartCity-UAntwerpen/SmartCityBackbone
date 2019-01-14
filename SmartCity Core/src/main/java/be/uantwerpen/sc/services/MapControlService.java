package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.models.map.CustomMap;
import be.uantwerpen.sc.models.points.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@Service
public class MapControlService {
    @Autowired
    private PointControlService pointControlService;

    @Autowired
    private LinkControlService linkControlService;

    private CustomMap topMap;
    private boolean topMapCreated = false;
    private ArrayList<Link> referenceList;


    /**
     * Builds map of given type
     *
     * @param type
     * @return the created map of type CustomMap
     */
    public CustomMap
    buildCustomMapJson(String type) {
        CustomMap map = new CustomMap();

        for (Link link : linkControlService.getAllLinks()) {
            if (type.equals("visual") && !link.getAccess().contains("Top")) {
                map.addLink(link);
            } else if (type.equals(link.getAccess())) {
                map.addLink(link);
            }
        }

        for (Point point : pointControlService.getAllPoints()) {
            if (type.equals("visual")) {
                map.addPoint(point);
            } else if (type.equals(point.getAccess())) {
                map.addPoint(point);
            }
        }

        return map;
    }

    /**
     * This function creates a top view of the hubs and links between individual hubs.
     * The MaaS works with a simplified version of the map, therefore 'virtual' links are generated and returned.
     * First links are drawn between points for different kind of vehicles within the same 'hub',
     * secondly links are made between endpoint that have access to each other
     *
     * @return the topMap if it already exists, if it doesn't exist when calling this method, it will be created
     */
    public CustomMap buildTopMapJson() {

        if (topMapCreated) {
            return topMap;
        } else {
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

                referenceList = new ArrayList<Link>();

                //create links
                map = createTopLinks(point, point, point.getHub(), tempList, map);


                //create interconnection within hub
                Long idHub = point.getHub();
                for (Point otherPoint : map.getPointList()) {
                    if (idHub.equals(otherPoint.getHub()) && !point.equals(otherPoint)) {

                        boolean testDuplicate = false;
                        for (Link refLink : linkControlService.getAllLinks()) {
                            if (otherPoint.equals(refLink.getStopPoint()) && point.equals(refLink.getStartPoint()) && refLink.getAccess().equals("wait")) {
                                testDuplicate = true;
                                if (!map.getLinkList().contains(refLink)) {
                                    map.addLink(refLink);
                                }
                                break;
                            }
                        }

                        if (!testDuplicate) {
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
            }

            topMap = map;
            topMapCreated = true;

            return map;
        }
    }

    /**
     * Recursive algorithm for finding links between endpoints
     * TODO: fix descriptor
     *
     * @param originalPoint
     * @param pointStart
     * @param hub
     * @param links
     * @param map
     * @return The map
     */
    private CustomMap createTopLinks(Point originalPoint, Point pointStart, Long hub, List<Link> links, CustomMap map) {
        Point otherPoint;

        for (Link link : links) {
            //get all links beginning from current point

            //check referenceList if the the link has been handled already
            if (!referenceList.contains(link)) {

                if (link.getStartPoint().getId().equals(pointStart.getId())) {

                    //check if the link ends in an endpoint, if it does, create toplink
                    if (link.getStopPoint().getPointType().equals("ENDPOINT")) {

                        //check if arrival point is same as start
                        if (link.getStopPoint().getHub().equals(hub)) {
                            //do nothing
                        } else {

                            boolean testDuplicate = false;
                            for (Link refLink : linkControlService.getAllLinks()) {

                                String refAccess = link.getAccess() + "Top";

                                if (link.getStopPoint().equals(refLink.getStopPoint()) && originalPoint.equals(refLink.getStartPoint()) && refAccess.equals(refLink.getAccess())) {
                                    testDuplicate = true;
                                    if (!map.getLinkList().contains(refLink)) {
                                        map.addLink(refLink);
                                    }
                                    break;
                                }
                            }

                            if (!testDuplicate) {
                                for (Point point : pointControlService.getAllPoints()) {
                                    if (point.equals(link.getStopPoint())) {
                                        otherPoint = point;
                                        Link topLink = new Link();
                                        topLink.setLength(new Long(1));
                                        topLink.setAccess(link.getAccess() + "Top");
                                        topLink.setWeight(1);
                                        topLink.setStartPoint(originalPoint);
                                        topLink.setStopPoint(otherPoint);
                                        linkControlService.saveLink(topLink);
                                        map.addLink(topLink);
                                    }
                                }
                            }
                        }
                    } else {
                        //add handled links to referenceList to prevent loops
                        referenceList.add(link);

                        createTopLinks(originalPoint, link.getStopPoint(), hub, links, map);
                    }
                }
            }
        }
        return map;
    }
}
