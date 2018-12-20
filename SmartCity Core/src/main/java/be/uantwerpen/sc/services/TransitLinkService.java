package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.TransitLink;
import be.uantwerpen.sc.repositories.TransitLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransitLinkService {
    @Autowired
    private TransitLinkRepository transitLinkRepository;

    public TransitLink getLinkWithId(int id){
        return transitLinkRepository.findById(id);
    }
}
