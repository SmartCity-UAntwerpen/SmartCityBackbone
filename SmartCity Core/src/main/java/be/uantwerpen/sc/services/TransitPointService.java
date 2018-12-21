package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.TransitPoint;
import be.uantwerpen.sc.repositories.TransitPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransitPointService {
    @Autowired
    TransitPointRepository transitPointRepository;

    public TransitPoint getPointWithId(int id){
        return transitPointRepository.findById(id);
    }

    public TransitPoint getPointWithMapidAndPid(int pid, int mapid){
        return transitPointRepository.findByPidAndMapid(pid, mapid);
    }
}
