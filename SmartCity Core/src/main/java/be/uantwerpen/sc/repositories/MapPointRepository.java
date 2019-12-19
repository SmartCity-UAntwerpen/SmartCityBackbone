package be.uantwerpen.sc.repositories;


import be.uantwerpen.sc.models.map.Map;
import be.uantwerpen.sc.models.map.MapPoint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MapPointRepository extends CrudRepository<MapPoint, Long> {
    MapPoint findById(long id);
    MapPoint findByIdAndMap(int id, Map map);
    MapPoint findByPointIdAndMap_Id(int pointId, int mapId);
    List<MapPoint> findAllByMap_Id(int mapId);
}
