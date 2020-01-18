package be.uantwerpen.sc.repositories;


import be.uantwerpen.sc.models.mapbuilder.MBTransitLink;
import be.uantwerpen.sc.models.mapbuilder.RobotLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MBTransitLinkRepository extends CrudRepository<MBTransitLink, Long> {
    MBTransitLink findById(int id);
    List<MBTransitLink> findAll();
}
