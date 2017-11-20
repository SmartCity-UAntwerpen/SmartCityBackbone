package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.*;
import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.services.BotControlService;
import be.uantwerpen.sc.services.LinkControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * Created by Thomas on 25/02/2016.
 */
@RestController
@RequestMapping("/bot/")
public class BotController
{
    @Autowired
    private BotControlService botControlService;

    @Autowired
    private LinkControlService linkControlService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Bot> allBots()
    {
        List<Bot> robotEntityList = botControlService.getAllBots();

        return robotEntityList;
    }

    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public Bot getBot(@PathVariable("id") Long id)
    {
        return botControlService.getBot(id);
    }

    @RequestMapping(value = "{id}",method = RequestMethod.POST)
    public void saveBot(@PathVariable("id") Long id, @RequestBody Bot bot)
    {
        botControlService.saveBot(bot);
    }

    @RequestMapping(value = "{id}",method = RequestMethod.PUT)
    public void updateBot(@PathVariable("id") Long id, @RequestBody Bot bot)
    {
        botControlService.updateBot(bot);
    }

    //not used in current design
    @RequestMapping(value = "updateBotTest/{id}",method = RequestMethod.GET)
    public void updateBotTest(@PathVariable("id") Long id)
    {
        Bot botEntity = new Bot();
        botEntity.setId(id);
        botEntity.setState("Updated");
        botControlService.updateBot(botEntity);
    }

    //not used in current design
    @RequestMapping(value = "test",method = RequestMethod.GET)
    public Bot testRestBot()
    {
        return new Bot();
    }

    //not used in current design
    @RequestMapping(value = "savetest",method = RequestMethod.GET)
    public void saveBotTest()
    {
        Bot bot = new Bot();
        bot.setState("test");
        botControlService.saveBot(bot);
    }

    //not used in current design
    @RequestMapping(value = "goto/{id}/{rid}",method = RequestMethod.GET)
    public String goTo(@PathVariable("id") Long id, @PathVariable("rid") Long rid)
    {
        Bot botEntity = botControlService.getBot(rid);
        /*if (!pointEntities.contains(botEntity.getLinkId().getStopId())){
            pointEntities.add(botEntity.getLinkId().getStopId());
        }*/
        if (botEntity != null)
        {
            if (botEntity.getPercentageCompleted() >= 50)
            {
               // stack.push(botEntity.getLinkId().getStopId());
            }
        }
        else
        {
            System.out.println("Robot does not exist");
        }

        return "Something";
    }

    //when new bot subscribes, it sends a request with its type and gets an ID in return
    @Produces("application/json")
    @RequestMapping(value = "newBot/{type}", method = RequestMethod.GET)
    public Long newRobot(@PathVariable("type") String type)
    {
        Bot bot = null;
           //check if requesting bot is car, drone, light or robot and return ID.
        if(type.equals("car")){
            bot = new Car();
        }else if(type.equals("drone")){
            bot = new Drone();
        }else if(type.equals("light")) {
            bot = new TrafficLight();
        }else if(type.equals("robot")) {
            bot = new Robot();
        }else{
            System.out.println(type + " is not a valid type");
            return Long.valueOf(-1);
        }

        //Save bot in database and get bot new rid

        bot.setPercentageCompleted(0);
        bot = botControlService.saveBot(bot);

        Date date = new Date();
        System.out.println("New bot created with id: " + bot.getId() + " " + type + " - " + date.toString());

        return bot.getId();
    }

    //not used in current design
    @RequestMapping(value = "{id}/lid/{lid}", method = RequestMethod.GET)
    public void locationLink(@PathVariable("id") Long id, @PathVariable("lid") Long lid)
    {
        Bot bot = this.getBot(id);
        Link link;

        if(bot != null)
        {
            link = linkControlService.getLink(lid);

            if(link != null)
            {
                bot.setLinkId(link);
                botControlService.updateBot(bot);
            }
            else
            {
                System.out.println("Link with id: " + lid + " not found!");
            }
        }
        else
        {
            System.out.println("Bot with id:" + id + " not found!");
        }
    }

    //not used in current design
    public void updateLocation(Long id, int mm)
    {
        Bot bot = this.getBot(id);

        if(bot != null)
        {
            bot.setPercentageCompleted(mm);
            bot.setState("Updated");
            botControlService.saveBot(bot);
        }
    }

    @RequestMapping(value = "/delete/{rid}",method = RequestMethod.GET)
    public void deleteBot(@PathVariable("rid") Long rid)
    {
        botControlService.deleteBot(rid);
        System.out.println("Bot with id: " + rid + " deleted from DB");
    }

    @RequestMapping(value = "/resetbots",method = RequestMethod.GET)
    public void resetBots()
    {
        botControlService.resetBots();
    }

    @RequestMapping(value = "/getAllVehicles", method = RequestMethod.GET)
    public String posAll()
    {
        botControlService.setPosAll();
        return botControlService.getPosAll();
    }

    @RequestMapping(value = "/getOneVehicle/{id}", method = RequestMethod.GET)
    public String posOne(@PathVariable("id") Long id)
    {
        botControlService.setPosAll();
        return botControlService.getPosOne(id);
    }

    //duplicate method
    @RequestMapping(value = "/clearBots", method = RequestMethod.GET)
    public String deleteAll()
    {
        botControlService.resetBots();
        System.out.println("Bots Destroyed...");
        return "Bots Destroyed...";
    }
}
