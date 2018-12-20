package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.TransitLink;
import be.uantwerpen.sc.models.TransitPoint;
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
    private TransitPointRepository transitPointRepository;

    public AStarService() {
        this.graph = new MultiGraph("SmartCityGraph");
    }

    /**
     * MakeNode function
     * will make all the necessairy nodes in the Graph, using information provided by the Graphbuilder service.
     */
    private void makeNode() {
        List<TransitPoint> transitPoints = this.graphBuilder.getPointList();
        // provide all the nodes
        transitPoints.stream().map(TransitPoint::getMapid).distinct().forEach(mapId -> graph.addNode(String.valueOf(mapId)));
    }

    /**
     * Destroy nodes function
     * Will remove all the nodes in the Graph. This is needed when you want to destroy an old graph.
     */
    private void destroyNodes() {
        for (int i = this.graph.getNodeCount(); i > 0; i--) {
            this.graph.removeNode(i);
        }
    }

    /**
     * Make Edge function
     * will make all the necessary edges in the Graph, using the information provided by the GraphBuilder
     */
    private void makeEdge() {
        List<TransitLink> transitLinks = this.graphBuilder.getLinkList();
        for (TransitLink edge : transitLinks) {
            this.graph.addEdge(Integer.toString(edge.getId()),
                    Integer.toString(transitPointRepository.findById(edge.getStartId()).getMapid()),
                    Integer.toString(transitPointRepository.findById(edge.getStopId()).getMapid()), false)
                    .setAttribute("weight", edge.getWeight());
        }
    }

    /**
     * Destroy edges function
     * Will remove all the edges in the Graph. This is needed when you want to destroy an old graph.
     */
    private void destroyEdges() {
        for (int i = this.graph.getEdgeCount(); i > 0; i--) {
            this.graph.removeEdge(i);
        }
    }

    /**
     * Update Nodes and Edges function
     * this function will first destroy all the nodes and edges in a graph, request an update from the Graphbuilder service,
     * before rebuilding the Graph, hence updating the Graph
     */
    private void updateNodesAndEdges() {
        destroyNodes();
        destroyEdges();
        makeNode();
        makeEdge();
    }

    /**
     * Determines the n-best routes between two points on maps.
     *
     * @param startPid Starting position
     * @param endPid   End position
     * @param startMap Map ID of starting point
     * @param endMap   Map ID of end point
     * @return A set of possible routes. Each route is an array of TransitLink IDs.
     */
    public List<Integer[]> determinePath(int startPid, int startMap, int endPid, int endMap) throws IllegalArgumentException {
        String start = String.valueOf(startMap);
        String end = String.valueOf(endMap);

        List<Integer[]> paths = new ArrayList<>();

        if (start.equals(end)) {
            // no need for A* if start and end point are in the same map
            paths.add(new Integer[]{-1});
            return paths;
        }

        AStar astar = new AStar(this.graph);
        updateNodesAndEdges();
        final int numberOfRoutes = 5;
        for (int i = 0; i < numberOfRoutes; i++) {
            Optional<Path> shortestRoute = getShortestRoute(astar, start, end);
            if (shortestRoute.isPresent()) {
                paths.add(shortestRoute.get().getEdgePath().stream().map(edge -> Integer.parseInt(edge.getId())).toArray(Integer[]::new));
                Edge lowestCostEdge = shortestRoute.get().getEdgePath().stream().min(Comparator.comparingInt(o -> o.getAttribute("weight"))).orElseThrow(IllegalStateException::new);
                graph.removeEdge(lowestCostEdge);
                if (logger.isDebugEnabled()) {
                    StringBuilder path = new StringBuilder();
                    shortestRoute.get().getEdgePath().forEach(edge -> path.append(edge.getId()).append(" "));
                    logger.debug("Route for " + startPid + " to " + endPid + " found: " + path.toString());
                }
            } else {
                logger.debug("No more route for " + start + " to " + end + " found");
                break;
            }
        }
        return paths;
    }

    private Optional<Path> getShortestRoute(AStar astar, String start, String end) {
        astar.compute(start, end);
        if (astar.noPathFound())
            return Optional.empty();

        return Optional.of(astar.getShortestPath());
    }

}