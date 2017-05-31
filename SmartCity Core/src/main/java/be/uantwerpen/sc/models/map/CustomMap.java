package be.uantwerpen.sc.models.map;

import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.models.points.Point;
import be.uantwerpen.sc.tools.pathplanning.AbstractMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quent on 5/16/2017.
 */
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

    public String getTopMapLinks(){
        String str = "[";
        for(Link link : linkList){
            str = str + link.toStringTop() + ",";
        }

        str = str.substring(0, str.length()-1) + "]";

        return str;
    }

    public String getTopMapPoints(){
        String str = "[";

        for(Point point : pointList){
            str = str + point.toStringTop() + ",";
        }

        str = str.substring(0, str.length()-1) + "]";

        return str;
    }



    @Override
    public String toString() {
        return "{" +
                pointList +
                linkList +
                '}';
    }

    public String toString(String type) {

        if(type.equals("car")||type.equals("drone")) {
            return "" + pointList + "";
        }else{
            return "{" +
                    "\"pointList\" :" + pointList +
                    ", \"linkList\" :" + linkList +
                    '}';
        }
      }
}
