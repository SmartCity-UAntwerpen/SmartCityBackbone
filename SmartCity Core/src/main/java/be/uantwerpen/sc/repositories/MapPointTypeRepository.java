package be.uantwerpen.sc.repositories;


import be.uantwerpen.sc.models.map.MapPointType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MapPointTypeRepository extends CrudRepository<MapPointType, Long> {
    MapPointType findById(int id);
}
