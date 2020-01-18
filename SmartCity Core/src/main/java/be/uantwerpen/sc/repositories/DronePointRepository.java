package be.uantwerpen.sc.repositories;


import be.uantwerpen.sc.models.mapbuilder.DronePoint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DronePointRepository extends CrudRepository<DronePoint, Long> {
    DronePoint findById(int id);
    List<DronePoint> findAll();
}
