package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.sim.SimBot;
import be.uantwerpen.sc.models.sim.SimCar;
//import be.uantwerpen.sc.models.sim.SimF1;
import be.uantwerpen.sc.models.sim.SimDrone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

/**
 * Created by Thomas on 25/02/2016.
 */
@Service
public class SimDispatchService
{
    @Autowired
    SimSupervisorService supervisorService;

    @Value("${ds.core.ip}")
    private String droneCoreIp;

    @Value("#{new Integer(${ds.core.port})}")
    private int droneCorePort;

    public SimDispatchService()
    {

    }

    public SimBot instantiateBot(String type)
    {
        SimBot bot = this.parseBot(type);

        if(bot == null)
        {
            return null;
        }

        if(supervisorService.addNewBot(bot))
        {
            return bot;
        }
        else
        {
            return null;
        }
    }

    private SimBot parseBot(String botType)
    {
        SimBot simBot;

        switch(botType.toLowerCase().trim())
        {
            case "car":
                simBot = new SimCar();
                break;
            case "drone":
                simBot = new SimDrone(droneCoreIp, droneCorePort);
                break;
            /*case "F1":
                simBot = new SimF1();
                break;*/
            default:
                simBot = null;
                break;
        }

        return simBot;
    }
}
