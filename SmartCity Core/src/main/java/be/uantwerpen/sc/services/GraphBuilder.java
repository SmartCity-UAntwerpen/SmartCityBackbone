package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.TransitLink;
import be.uantwerpen.sc.models.TransitPoint;
import be.uantwerpen.sc.repositories.TransitPointRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * NV 2018
 * Made for getting all the information for the A* algorithm.
 */
@Service
public class GraphBuilder {

    private static final Logger logger = LogManager.getLogger(GraphBuilder.class);

    private List<TransitLink> linkList;
    private TransitPointRepository pointRepository;

    @Autowired
    public GraphBuilder(TransitPointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public void addLinks(Collection<TransitLink> links) {
        linkList.addAll(links);
    }

    public void clear() {
        linkList.clear();
    }

    /**
     * Returns a list of all points in the top map
     */
    public List<TransitPoint> getPointList() {
        return linkList.stream().flatMap(link -> Stream.of(link.getStartId(), link.getStopId())).distinct().map(integer -> pointRepository.findById(integer)).collect(Collectors.toList());
    }

    /**
     * Returns a list with all links of the top map
     */
    public List<TransitLink> getLinkList() {
        return linkList;
    }

    /**
     * Returns the link with given start and end point, or an empty optional if none is found.
     */
    public Optional<TransitLink> getCertainLink(Long startPoint, Long endPoint) {
        return Optional.empty();
//        return linkList.stream().filter(link -> link.getStartPoint().getId().equals(startPoint) && link.getStopPoint().getId().equals(endPoint)).findFirst();
    }

}
