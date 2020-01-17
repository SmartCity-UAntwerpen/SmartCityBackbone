package be.uantwerpen.sc.repositories;


import be.uantwerpen.sc.models.mapbuilder.LinkLock;
import be.uantwerpen.sc.models.mapbuilder.RobotLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkLockRepository extends CrudRepository<LinkLock, Long> {
    LinkLock findById(int id);
    List<LinkLock> findAll();
}
