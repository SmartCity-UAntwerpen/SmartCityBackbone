package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.BackendInfo;
import be.uantwerpen.sc.models.Path;
import be.uantwerpen.sc.models.map.Map;
import be.uantwerpen.sc.models.map.MapPoint;
import be.uantwerpen.sc.models.mapbuilder.*;
import be.uantwerpen.sc.services.*;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.*;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.io.StringReader;
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

    @Autowired
    private BackendInfoService backendInfoService;

    @Autowired
    private AStarService aStarService;

    @Autowired
    private PathService pathService;

    @Autowired
    private MapService mapService;

    @Autowired
    private MapbuilderService mapbuilderService;

    @Value("${backends.enabled}")
    boolean backendsEnabled;

    @Produces("application/json")
    @RequestMapping(value = "getmap", method = RequestMethod.GET)
    public String loadMap(){
        JsonObjectBuilder jsonRoot = Json.createObjectBuilder();
        JsonObjectBuilder jsonCar = Json.createObjectBuilder();
        JsonObjectBuilder jsonDrone = Json.createObjectBuilder();
        JsonObjectBuilder jsonRobot = Json.createObjectBuilder();
        JsonArrayBuilder jsonTransitLinks = Json.createArrayBuilder();

        List<CarPoint> carpoints = mapbuilderService.findAllCarpoints();
        List<CarLink> carlinks = mapbuilderService.findAllCarLinks();
        List<DronePoint> dronepoints = mapbuilderService.findAllDronePoints();
        List<DroneLink> dronelinks = mapbuilderService.findAllDroneLinks();
        List<RobotLink> robotlinks = mapbuilderService.findAllRobotLinks();
        List<RobotTile> robottiles = mapbuilderService.findAllRobotTiles();
        List<LinkLock> robotlinklocks = mapbuilderService.findAllLinkLocks();
        List<MBTransitLink> transitLinks = mapbuilderService.findAllTransitLinks();

        JsonArrayBuilder jsonCarPoints = Json.createArrayBuilder();
        for(CarPoint cp : carpoints){
            JsonObjectBuilder jsonCarPoint = Json.createObjectBuilder();
            jsonCarPoint.add("x", cp.getX());
            jsonCarPoint.add("y", cp.getY());
            jsonCarPoint.add("id", cp.getName());
            jsonCarPoint.add("pointName", cp.getId());
            jsonCarPoints.add(jsonCarPoint.build());
        }
        jsonCar.add("points", jsonCarPoints.build());
        JsonArrayBuilder jsonCarLinks = Json.createArrayBuilder();
        for(CarLink cl : carlinks){
            //System.out.println("hey");
            JsonObjectBuilder jsonCarLink = Json.createObjectBuilder();
            jsonCarLink.add("from", cl.getLstart());
            jsonCarLink.add("to", cl.getLend());
            jsonCarLink.add("id", cl.getName());
            jsonCarLinks.add(jsonCarLink.build());
        }
        jsonCar.add("links", jsonCarLinks.build());
        jsonCar.add("mapId", 1);
        jsonRoot.add("racecar", jsonCar);

        JsonArrayBuilder jsonDronePoints = Json.createArrayBuilder();
        for(DronePoint dp : dronepoints){
            JsonObjectBuilder jsonDronePoint = Json.createObjectBuilder();
            jsonDronePoint.add("x", dp.getX());
            jsonDronePoint.add("y", dp.getY());
            jsonDronePoint.add("id", dp.getName());
            jsonDronePoint.add("pointName", dp.getId());
            jsonDronePoints.add(jsonDronePoint.build());
        }
        jsonDrone.add("points", jsonDronePoints.build());
        JsonArrayBuilder jsonDroneLinks = Json.createArrayBuilder();
        for(DroneLink dl : dronelinks){
            JsonObjectBuilder jsonDroneLink = Json.createObjectBuilder();
            jsonDroneLink.add("from", dl.getLstart());
            jsonDroneLink.add("to", dl.getLend());
            jsonDroneLink.add("id", dl.getName());
            jsonDroneLinks.add(jsonDroneLink.build());
        }
        jsonDrone.add("links", jsonDroneLinks.build());
        jsonDrone.add("mapId", 2);
        jsonRoot.add("drone", jsonDrone);

        JsonArrayBuilder jsonRobotTiles = Json.createArrayBuilder();
        for(RobotTile rt : robottiles){
            JsonObjectBuilder jsonRobotTile = Json.createObjectBuilder();
            jsonRobotTile.add("type", rt.getType());
            jsonRobotTile.add("x", rt.getX());
            jsonRobotTile.add("y", rt.getY());
            jsonRobotTile.add("id", rt.getName());
            jsonRobotTile.add("pointName", rt.getId());
            jsonRobotTiles.add(jsonRobotTile.build());
        }
        jsonRobot.add("tiles", jsonRobotTiles.build());
        JsonArrayBuilder jsonRobotLinks = Json.createArrayBuilder();
        for(RobotLink rl : robotlinks){
            JsonObjectBuilder jsonRobotLink = Json.createObjectBuilder();
            jsonRobotLink.add("_startNode", rl.getStartnode());
            jsonRobotLink.add("_startHeading", rl.getStartheading());
            jsonRobotLink.add("_destinationNode", rl.getDestinationnode());
            jsonRobotLink.add("_destinationHeading", rl.getDestinationheading());
            jsonRobotLink.add("_distance", rl.getDistance());
            jsonRobotLink.add("_angle", rl.getAngle());
            jsonRobotLink.add("_isLocal", rl.isIslocal());
            jsonRobotLink.add("_loopback", rl.isLoopback());
            jsonRobotLink.add("_lockId", rl.getLockid());
            jsonRobotLinks.add(jsonRobotLink.build());
        }
        jsonRobot.add("links", jsonRobotLinks.build());
        JsonArrayBuilder jsonRobotLinkLocks = Json.createArrayBuilder();
        for(LinkLock ll: robotlinklocks){
            JsonObjectBuilder jsonLinkLock = Json.createObjectBuilder();
            jsonLinkLock.add("id", ll.getId());
            jsonRobotLinkLocks.add(jsonLinkLock.build());
        }
        jsonRobot.add("locks", jsonRobotLinkLocks.build());
        jsonRobot.add("mapId", 3);
        jsonRoot.add("robot", jsonRobot);

        for(MBTransitLink tl : transitLinks){
            JsonObjectBuilder jsonTransitLink = Json.createObjectBuilder();
            jsonTransitLink.add("from", tl.getTlstart());
            jsonTransitLink.add("to", tl.getTlend());
            jsonTransitLink.add("id", tl.getId());
            jsonTransitLinks.add(jsonTransitLink.build());
        }
        jsonRoot.add("transitlinks", jsonTransitLinks);
        return jsonRoot.build().toString();
    }

    /**
     * Save the map from the mapbuilder to the database.
     * t.o.d.o: deze functie enablen.
     * @param httpEntity
     * @return
     */
    @RequestMapping(value = "savemap", method = RequestMethod.POST)
    public ResponseEntity saveMap(HttpEntity<String> httpEntity){
        boolean persist = false;
        String rawJson = httpEntity.getBody();
        JsonReader jsonReader = Json.createReader(new StringReader(rawJson));
        JsonObject root = jsonReader.readObject();
        JsonArray droneLinks = root.getJsonObject("drone").getJsonArray("links");
        JsonArray carLinks = root.getJsonObject("racecar").getJsonArray("links");
        JsonArray dronePoints = root.getJsonObject("drone").getJsonArray("points");
        JsonArray carPoints = root.getJsonObject("racecar").getJsonArray("points");
        JsonArray robotTiles = root.getJsonObject("robot").getJsonArray("tiles");
        JsonArray robotLinks = root.getJsonObject("robot").getJsonArray("links");
        JsonArray robotLocks = root.getJsonObject("robot").getJsonArray("locks");
        JsonArray transitLinks = root.getJsonArray("transitlinks");

        if(persist) mapbuilderService.eraseMapdata();

        // Parse drone points
        for(int i = 0; i< dronePoints.size(); i++){
            JsonObject dronePoint = dronePoints.getJsonObject(i);
            int xPos = dronePoint.getInt("x");
            int yPos = dronePoint.getInt("y");
            String name = dronePoint.getString("id");
            DronePoint dp = new DronePoint(i, xPos, yPos, name);
            if(persist)  mapbuilderService.save(dp);
        }
        // Parse drone links
        for(int i = 0; i< droneLinks.size(); i++){
            JsonObject droneLink = droneLinks.getJsonObject(i);
            String from  = droneLink.getString("from");
            String to = droneLink.getString("to");
            String name = droneLink.getString("id");
            DroneLink dl = new DroneLink(i, from, to, name);
            if(persist)  mapbuilderService.save(dl);
        }

        // Parse car points
        for(int i = 0; i< carPoints.size(); i++){
            JsonObject carPoint = carPoints.getJsonObject(i);
            int xPos = carPoint.getInt("x");
            int yPos = carPoint.getInt("y");
            String name = carPoint.getString("id");
            CarPoint cp = new CarPoint(i, xPos, yPos, name);
            if(persist) mapbuilderService.save(cp);
        }

        // Parse car links
        for(int i = 0; i< carLinks.size(); i++){
            JsonObject carLink = carLinks.getJsonObject(i);
            String from  = carLink.getString("from");
            String to = carLink.getString("to");
            String name = carLink.getString("id");
            CarLink cl = new CarLink(i, from, to, name);
            if(persist) mapbuilderService.save(cl);
        }

        // Parse robot tiles
        for(int i = 0; i<robotTiles.size(); i++){
            JsonObject robotTile = robotTiles.getJsonObject(i);
            int xPos = robotTile.getInt("x");
            int yPos = robotTile.getInt("y");
            int type = robotTile.getInt("type");
            String name = robotTile.getString("id");
            RobotTile rt = new RobotTile(i, type, name, xPos, yPos);
            if(persist) mapbuilderService.save(rt);
        }

        // Parse robot links
        for(int i = 0; i<robotLinks.size(); i++){
            JsonObject robotLink = robotLinks.getJsonObject(i);
            int id = i;
            int angle = robotLink.getInt("_angle");
            String destinationheading = robotLink.getString("_destinationHeading");
            String destinationnode = robotLink.getString("_destinationNode");
            int distance = robotLink.getInt("_distance");
            boolean islocal = robotLink.getBoolean("_isLocal");
            int lockid = robotLink.getInt("_lockId");
            boolean loopback = robotLink.getBoolean("_loopback");
            String startheading = robotLink.getString("_startHeading");
            String startnode = robotLink.getString("_startNode");
            String status = robotLink.getString("_status");
            RobotLink rl = new RobotLink(id, angle, destinationheading, destinationnode, distance, islocal, lockid, loopback, startheading, startnode, status);
            if(persist) mapbuilderService.save(rl);
        }

        // Parse linklocks
        for(int i = 0; i<robotLocks.size(); i++){
            JsonObject linkLock = robotLocks.getJsonObject(i);
            int id = linkLock.getInt("id");
            LinkLock ll = new LinkLock(id);
            if(persist) mapbuilderService.save(ll);
        }

        // Parse transitlinks
        for(int i = 0; i<transitLinks.size(); i++){
            JsonObject transitLink = transitLinks.getJsonObject(i);
            String id = transitLink.getString("id");
            String from = transitLink.getString("from");
            String to = transitLink.getString("to");
            MBTransitLink tl = new MBTransitLink(i, id, from, to);
            if(persist) mapbuilderService.save(tl);
        }


        jsonReader.close();
        if(persist) return new ResponseEntity(HttpStatus.CREATED) ;
        return new ResponseEntity(HttpStatus.NOT_MODIFIED);

    }

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

}