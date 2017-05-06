package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.sim.SimBot;
import be.uantwerpen.sc.models.sim.SimWorker;
import be.uantwerpen.sc.services.sim.SimWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created by Thomas on 04/04/2016.
 */
@Controller
public class GlobalModelController
{
    @Autowired
    private SimWorkerService simWorkerService;

    @ModelAttribute("numOfWorkers")
    public int getNumberOfWorkers()
    {
        return simWorkerService.getNumberOfWorkers();
    }

    @ModelAttribute("allWorkers")
    public Iterable<SimWorker> getAllWorkers()
    {
        return simWorkerService.findAll();
    }

    @ModelAttribute("numOfBots")
    public int getNumberOfBots()
    {
        //return botService.getNumberOfBots();
        return 20;
    }

/*    @ModelAttribute("allBots")
    public Iterable<SimBot> getAllBots()
    {
        //return botService.findAll();
        return
    }*/
}
