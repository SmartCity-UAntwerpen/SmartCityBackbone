package be.uantwerpen.sc.repositories;


import be.uantwerpen.sc.models.mapbuilder.CarLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CarLinkRepository extends CrudRepository<CarLink, Long> {
    CarLink findById(int id);
    List<CarLink> findAll();
}
