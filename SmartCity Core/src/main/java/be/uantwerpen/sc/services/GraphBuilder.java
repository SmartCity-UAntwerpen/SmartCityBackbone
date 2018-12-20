package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.TransitLink;
import be.uantwerpen.sc.models.TransitPoint;
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

    private TransitPointRepository pointRepository;
    private TransitLinkRepository linkRepository;

    @Autowired
    public GraphBuilder(TransitPointRepository pointRepository, TransitLinkRepository linkRepository) {
        this.pointRepository = pointRepository;
        this.linkRepository = linkRepository;
    }

    /**
     * Returns a list of all points in the top map
     */
    public List<TransitPoint> getPointList() {
        return getLinkList().stream().flatMap(link -> Stream.of(link.getStartId(), link.getStopId())).distinct().map(integer -> pointRepository.findById(integer)).collect(Collectors.toList());
    }

    /**
     * Returns a list with all links of the top map
     */
    public List<TransitLink> getLinkList() {
        return linkRepository.findAll();
    }

}
