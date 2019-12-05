package be.uantwerpen.sc.repositories;


import be.uantwerpen.sc.models.map.Map;
import be.uantwerpen.sc.models.map.MapPoint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MapPointRepository extends CrudRepository<MapPoint, Long> {
    MapPoint findByIdAndMap(int id, Map map);
    List<MapPoint> findAllByMap_Id(int mapId);
}
