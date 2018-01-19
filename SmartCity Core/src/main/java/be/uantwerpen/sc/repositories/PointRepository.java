package be.uantwerpen.sc.repositories;

import be.uantwerpen.sc.models.points.Point;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends CrudRepository<Point, Long> {
    List<Point> findAll();
}
