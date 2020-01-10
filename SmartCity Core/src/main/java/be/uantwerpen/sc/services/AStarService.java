package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.map.Link;
import be.uantwerpen.sc.models.map.MapPoint;
import be.uantwerpen.sc.repositories.MapPointRepository;
import be.uantwerpen.sc.repositories.TransitPointRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.MultiGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A Star Class (will be used as a Service)
 * This class will provide a path, while building up a graph in which the path will be calculated from input
 * it gets from the Graphbuilder service. Due to last minute additions, there are 2 determinePath method,s of
 * one was a quickfix to add a Delivery paramter so that the code could be integrated in the entire project
 * <p>
 * VN 2018
 */
@Service
public class AStarService {

    private static final Logger logger = LogManager.getLogger(AStarService.class);

    private Graph graph;
    @Autowired
    private GraphBuilder graphBuilder;
    @Autowired
    private MapPointRepository mapPointRepository;

    public AStarService() {
        this.graph = new MultiGraph("SmartCityGraph");
    }

    /**
     * MakeNode function
     * will make all the necessairy nodes in the Graph, using information provided by the Graphbuilder service.
     */
    private void makeNode() {
        List<MapPoint> transitPoints = this.graphBuilder.getPointList();
        // provide all the nodes
        transitPoints.stream().map(MapPoint::getMapId).distinct().forEach(mapId -> graph.addNode(String.valueOf(mapId)));
    }

    /**
     * Make Edge function
     * will make all the necessary edges in the Graph, using the information provided by the GraphBuilder
     */
    private void makeEdge() {
        List<Link> transitLinks = this.graphBuilder.getLinkList();
        for (Link edge : transitLinks) {
            // adds forward link
            this.graph.addEdge(edge.getId() + "[" + edge.getPointA() + "-" + edge.getPointB() + "]",
                    Integer.toString(mapPointRepository.findById(edge.getPointA()).getMap().getId()),
                    Integer.toString(mapPointRepository.findById(edge.getPointB()).getMap().getId()), true) // directed
                    .setAttribute("weight", edge.getWeight() + 0.0);
            // adds reverse link
            this.graph.addEdge(edge.getId() + "[" + edge.getPointB() + "-" + edge.getPointA() + "]",
                    Integer.toString(mapPointRepository.findById(edge.getPointB()).getMap().getId()),
                    Integer.toString(mapPointRepository.findById(edge.getPointA()).getMap().getId()), true) // directed
                    .setAttribute("weight", edge.getWeight() + 0.0);
        }
    }

    /**
     * Update Nodes and Edges function
     * this function will first destroy all the nodes and edges in a graph, request an update from the Graphbuilder service,
     * before rebuilding the Graph, hence updating the Graph
     */
    private void updateNodesAndEdges() {
        graph.clear(); // remove all edges and nodes
        makeNode();
        makeEdge();
    }

    /**
     * Determines the n-best routes between two points on maps.
     * Returns the list of transit points of the links that need to be traversed.
     *
     * @param startPid Starting position
     * @param endPid   End position
     * @param startMap Map ID of starting point
     * @param endMap   Map ID of end point
     * @return A set of possible routes. Each route is an array of TransitPoint IDs.
     */
    public List<Integer[]> determinePath(int startPid, int startMap, int endPid, int endMap) throws IllegalArgumentException {
        String start = String.valueOf(startMap);
        String end = String.valueOf(endMap);

        List<Integer[]> paths = new ArrayList<>();

        if (start.equals(end)) {
            // no need for A* if start and end point are in the same map
            Integer startId = mapPointRepository.findByPointIdAndMap_Id(startPid, startMap).getId();
            Integer endId = mapPointRepository.findByPointIdAndMap_Id(endPid, endMap).getId();
            paths.add(new Integer[]{startId, endId});
            logger.debug("Route for " + startPid + " to " + endPid + " found (map " + startMap + " to " + endMap + "): " + startId + ", " + endId);
            return paths;
        }

        AStar astar = new AStar(this.graph);
        updateNodesAndEdges();
        final int numberOfRoutes = 1;
        for (int i = 0; i < numberOfRoutes; i++) {
            Optional<Path> shortestRoute = getShortestRoute(astar, start, end);
            if (shortestRoute.isPresent()) {
                List<Integer> pointList = new ArrayList<>();
                for (int i1 = 0; i1 < shortestRoute.get().getEdgePath().size(); i1++) {
                    Edge edge = shortestRoute.get().getEdgePath().get(i1);
                    String[] points = edge.getId().substring(edge.getId().indexOf("[") + 1, edge.getId().length() - 1).split("-");
                    if (i1 > 0) {
                        pointList.add(Integer.parseInt(points[0]));
                    }
                    pointList.add(Integer.parseInt(points[0]));
                    pointList.add(Integer.parseInt(points[1]));
                    if (i1 < shortestRoute.get().getEdgePath().size() - 1) {
                        pointList.add(Integer.parseInt(points[1]));
                    }
                }
                Integer[] pathArray = new Integer[pointList.size()];
                pathArray = pointList.toArray(pathArray);
                paths.add(pathArray);
                Edge lowestCostEdge = shortestRoute.get().getEdgePath().stream().min(Comparator.comparingDouble(o -> o.getAttribute("weight"))).orElseThrow(IllegalStateException::new);
                graph.removeEdge(lowestCostEdge);
                if (logger.isDebugEnabled()) {
                    StringBuilder path = new StringBuilder();
                    shortestRoute.get().getEdgePath().forEach(edge -> path.append(edge.getId()).append(" "));
                    logger.debug("Route for " + startPid + " to " + endPid + " (map " + startMap + " to " + endMap + "): " + path.toString());
                }
            } else {
                logger.debug("No more route for " + start + " to " + end + " found");
                break;
            }
        }
        return paths;
    }

    /**
     * Returns an A* path for the current shortest route.
     *
     * @param astar the AStar object
     * @param start start point as string
     * @param end   end point as string
     * @return
     */
    private Optional<Path> getShortestRoute(AStar astar, String start, String end) {
        astar.compute(start, end);
        if (astar.noPathFound())
            return Optional.empty();

        return Optional.of(astar.getShortestPath());
    }

}