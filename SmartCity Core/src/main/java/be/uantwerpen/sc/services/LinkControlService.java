package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.repositories.LinkRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Deprecated
@Service
public class LinkControlService {
    @Autowired
    private LinkRepository linkRepository;

    public List<Link> getAllLinks() {
        return linkRepository.findAll();
    }

    public Link saveLink(Link link) {
        return linkRepository.save(link);
    }

    public Link getLink(Long id) {
        return linkRepository.findOne(id);
    }

}
