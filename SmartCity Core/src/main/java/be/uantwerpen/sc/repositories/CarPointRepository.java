package be.uantwerpen.sc.repositories;


import be.uantwerpen.sc.models.mapbuilder.CarPoint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarPointRepository extends CrudRepository<CarPoint, Long> {
    CarPoint findById(int id);
}
