package be.uantwerpen.sc.repositories;

import be.uantwerpen.sc.models.map.MapAccessType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MapAccessTypeRepository extends CrudRepository<MapAccessType, Long> {
    MapAccessType findById(int id);
}
