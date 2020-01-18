package be.uantwerpen.sc.repositories;


import be.uantwerpen.sc.models.mapbuilder.RobotLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RobotLinkRepository extends CrudRepository<RobotLink, Long> {
    RobotLink findById(int id);
    List<RobotLink> findAll();
}
