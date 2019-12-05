package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.Delivery;
import be.uantwerpen.sc.services.DeliveryService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deliveries/")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @RequestMapping(value = "getall", method = RequestMethod.GET)
    public List<Delivery> findAllDeliveries()
    {
        return deliveryService.findAll();
    }

    @RequestMapping(value = "savedelivery", method = RequestMethod.POST)
    public Delivery save(@RequestBody Delivery delivery)
    {
        return deliveryService.save(delivery);
    }

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    public Delivery getDelivery(@PathVariable("id") Long id)
    {
        return deliveryService.getJob(id);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    public void delete(@PathVariable("id") Long id) {
        deliveryService.delete(id);
    }

    @RequestMapping(value = "deleteall", method = RequestMethod.POST)
    public void deleteAll()
    {
        deliveryService.deleteAll();
    }
}
