package be.uantwerpen.sc.repositories;


import be.uantwerpen.sc.models.mapbuilder.RobotTile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RobotTileRepository extends CrudRepository<RobotTile, Long> {
    RobotTile findById(int id);
    List<RobotTile> findAll();
}
