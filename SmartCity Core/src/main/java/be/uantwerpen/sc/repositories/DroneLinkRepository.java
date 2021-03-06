package be.uantwerpen.sc.repositories;


import be.uantwerpen.sc.models.mapbuilder.DroneLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DroneLinkRepository extends CrudRepository<DroneLink, Long> {
    DroneLink findById(int id);
    List<DroneLink> findAll();
}
