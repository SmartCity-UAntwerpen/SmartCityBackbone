package be.uantwerpen.sc.models.map;

import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.models.points.Point;
import be.uantwerpen.sc.tools.pathplanning.AbstractMap;

import java.util.ArrayList;
import java.util.List;

public class CustomMap implements AbstractMap
{
    private List<Link> linkList;
    private List<Point> pointList;

    public CustomMap(){
        linkList = new ArrayList<>();
        pointList = new ArrayList<>();
    }

    public void addLink(Link link){
        linkList.add(link);
    }

    public void setLinkList(List<Link> nodeList) {
        this.linkList = linkList;
    }

    public List<Link> getLinkList()
    {
        return linkList;
    }

    public void addPoint(Point point){ pointList.add(point);}

    public void setPointList(List<Point> pointList){this.pointList = pointList;}

    public List<Point> getPointList(){return pointList;}

    /**
     * Iterates through all links and adds their strings
     *
     * @return JSON String of links for the MaaS backbone
     */
    public String getTopMapLinksString(){
        String str = "[ ";
        for(Link link : linkList){
            str = str + link.toStringTop() + ",";
        }

        str = str.substring(0, str.length()-1) + "]";

        return str;
    }

    /**
     * Iterates through all points and adds their strings
     *
     * @return JSON string of points for the MaaS backbone
     */
    public String getTopMapPointsString(){
        String str = "[ ";

        for(Point point : pointList){
            str = str + point.toStringTop() + ",";
        }

        str = str.substring(0, str.length()-1) + "]";

        return str;
    }

    /**
     * Iterates through all points and adds their information to the string
     *
     * @return JSON string of points for the visualisation
     */
    public String getVisualPointsString(){
        String str = "{\"pointList\" :[ ";
        for(Point point : pointList){
            str = str + "{\"id\" : " + point.getId()
                    + ", \"x\" : " + point.getxCoord()
                    + ", \"y\" : " + point.getyCoord()
                    + ", \"access\" : \"" + point.getAccess()
                    + "\", \"type\" : \"" + point.getPointType()
                    + "\", \"neighbours\" : [ ";
            for(Link link : linkList){ //TODO: Possibly make more efficient by defining neighbours of a link instead of iterating through all
                if(link.getStartPoint().equals(point)){
                    str = str + "{\"neighbour\" : " + link.getStopPoint().getId() + "},";
                }
            }
            str = str.substring(0, str.length()-1) + "]},";
        }
        str = str.substring(0, str.length()-1) + "]}";
        return str;
    }


    /**
     * @return String of all points and links
     */
    @Override
    public String toString() {
        return "{" +
                pointList +
                linkList +
                '}';
    }


    /**
     * @param type The type to be put to string, e.g. carV
     * @return If type is car or drone, a list of points. Else a list of points and links
     */
    public String toString(String type) {
        System.out.println("got here: "+type);
        if(type.equals("drone")) {
            return "" + pointList + "";
        }else if(type.equals("car")) {
            System.out.println(pointList);
            return "" + pointList + "";
        }else{
            return "{" +
                    "\"pointList\" :" + pointList +
                    ", \"linkList\" :" + linkList +
                    '}';
        }
      }
}
