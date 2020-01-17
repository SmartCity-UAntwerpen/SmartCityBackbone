package be.uantwerpen.sc.repositories;


import be.uantwerpen.sc.models.mapbuilder.RobotLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RobotLinkRepository extends CrudRepository<RobotLink, Long> {
    RobotLink findById(int id);
}
