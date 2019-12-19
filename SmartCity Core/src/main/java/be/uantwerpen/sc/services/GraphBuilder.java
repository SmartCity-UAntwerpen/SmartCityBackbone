package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.TransitLink;
import be.uantwerpen.sc.models.TransitPoint;
import be.uantwerpen.sc.models.map.Map;
import be.uantwerpen.sc.models.map.MapPoint;
import be.uantwerpen.sc.repositories.MapPointRepository;
import be.uantwerpen.sc.repositories.TransitLinkRepository;
import be.uantwerpen.sc.repositories.TransitPointRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * NV 2018
 * Made for getting all the information for the A* algorithm.
 */
@Service
public class GraphBuilder {

    private static final Logger logger = LogManager.getLogger(GraphBuilder.class);

    private MapPointRepository mapPointRepository;
    private TransitLinkRepository transitLinkRepository;

    @Autowired
    public GraphBuilder(MapPointRepository mapPointRepository, TransitLinkRepository transitLinkRepository) {
        this.mapPointRepository = mapPointRepository;
        this.transitLinkRepository = transitLinkRepository;
    }

    /**
     * Returns a list of all points in the top map
     */
    public List<MapPoint> getPointList() {
        return getLinkList().stream().flatMap(link -> Stream.of(link.getStartId(), link.getStopId())).distinct().map(integer -> mapPointRepository.findById(integer)).collect(Collectors.toList());
    }

    /**
     * Returns a list with all links of the top map
     */
    public List<TransitLink> getLinkList() {
        return transitLinkRepository.findAll();
    }

}
