package be.uantwerpen.sc.repositories;

import be.uantwerpen.sc.models.map.Map;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface MapRepository extends CrudRepository<Map, Long> {
    Map findById(int id);
}
