package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.map.Map;
import be.uantwerpen.sc.models.map.MapPoint;
import be.uantwerpen.sc.repositories.MapAccessTypeRepository;
import be.uantwerpen.sc.repositories.MapPointRepository;
import be.uantwerpen.sc.repositories.MapPointTypeRepository;
import be.uantwerpen.sc.repositories.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapService {
    @Autowired
    private MapAccessTypeRepository mapAccessTypeRepository;
    @Autowired
    private MapPointRepository mapPointRepository;
    @Autowired
    private MapPointTypeRepository mapPointTypeRepository;
    @Autowired
    private MapRepository mapRepository;

    public Map getMapById(int id){
        return mapRepository.findById(id);
    }

    public List<MapPoint> getPointsByMapId(int id){
        return mapPointRepository.findAllByMap_Id(id);
    }



}
