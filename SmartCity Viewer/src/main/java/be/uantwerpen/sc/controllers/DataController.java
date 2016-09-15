package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.sim.SimBot;
import be.uantwerpen.sc.models.sim.SimMap;
import be.uantwerpen.sc.models.sim.SimPath;
import be.uantwerpen.sc.services.CarService;
import be.uantwerpen.sc.services.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Arthur on 23/03/2016.
 */
@RestController
public class DataController
{
    @Autowired
    CarService carService;

    @Autowired
    MapService mapService;

    @RequestMapping("/map")
    public SimMap returnSimMap()
    {
        return mapService.mapBuilder.getSimMap();
    }

    @RequestMapping("/paths")
    public List<SimPath> returnPaths()
    {
        return mapService.mapBuilder.getSimPaths();
    }

    @RequestMapping("/update")
    public List<SimBot> returnSimBot()
    {
        carService.getBots();
        return carService.simBots;
    }

    @RequestMapping("/testcar")
    public SimBot updateSimBot()
    {
        SimPath path = mapService.mapBuilder.getSimPaths().get(3);
        SimBot bot = carService.simBots.get(0);
        bot.setLinkID(path.getLinkID());
        bot.add10percent();
        bot.update(path);
        return bot;
    }

    /*@RequestMapping("/update")
    public SimBot returnSimBot(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }*/
}