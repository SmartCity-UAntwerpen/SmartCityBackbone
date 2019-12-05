package be.uantwerpen.sc.repositories;

import be.uantwerpen.sc.models.Delivery;
import be.uantwerpen.sc.models.Job;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * NV 2018
 */
@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {
    List<Delivery> findAll();
}
