package be.uantwerpen.sc.repositories;

import be.uantwerpen.sc.models.Delivery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * NV 2018
 */
@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {
    List<Delivery> findAll();
    Delivery findById(long id);
    Delivery findByOrderID(int OrderID);
}
