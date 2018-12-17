package be.uantwerpen.sc.repositories;

import be.uantwerpen.sc.models.TransitPoint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransitPointRepository  extends CrudRepository<TransitPoint, Long> {
    List<TransitPoint> findBy();
    TransitPoint findById(int id);
}
