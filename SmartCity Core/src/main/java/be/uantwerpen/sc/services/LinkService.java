package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.TransitLink;
import be.uantwerpen.sc.models.map.Link;
import be.uantwerpen.sc.repositories.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkService {
    @Autowired
    private LinkRepository linkRepository;

    public Link getLinkWithId(int id){
        return linkRepository.findById(id);
    }
    public Link getLinkWithStartidAndStopid(int pointA, int pointB){
        return linkRepository.findByPointAAndPointB(pointA, pointB);
    }
}
