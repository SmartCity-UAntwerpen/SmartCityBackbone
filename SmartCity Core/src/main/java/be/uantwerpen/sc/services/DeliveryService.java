package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.Delivery;
import be.uantwerpen.sc.models.jobs.Job;
import be.uantwerpen.sc.repositories.DeliveryRepository;
import be.uantwerpen.sc.repositories.JobRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Service for the Job class. All interactions will be defined here required to perform the methods for the Job class
 * NV 2018
 */
@Service
public class DeliveryService {

    private static final Logger logger = LogManager.getLogger(DeliveryService.class);

    @Autowired
    private DeliveryRepository deliveryRepository;

    public List<Delivery> findAll() {
        return this.deliveryRepository.findAll();
    }

    public Delivery save(final Delivery delivery)
    {
        return this.deliveryRepository.save(delivery);
    }

    public Delivery getDelivery(Long id)
    {
        return this.deliveryRepository.findById(id);
    }

    public void delete(Long id)
    {
        logger.debug("Deleting Delivert with id: " + id);
        this.deliveryRepository.delete(id);
    }

    public void deleteAll()
    {
        logger.info("Deleting all Deliveries!");
        this.deliveryRepository.deleteAll();
    }
}
