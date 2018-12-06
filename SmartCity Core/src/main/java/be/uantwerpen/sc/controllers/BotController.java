package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.Robot;
import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.services.BotControlService;
import be.uantwerpen.sc.services.LinkControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bot/")
public class BotController {
    @Autowired
    private BotControlService botControlService;

    @Autowired
    private LinkControlService linkControlService;

    /**
     * Asks BotControlService to search the database for all bots and returns them as a List of type Bot
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<be.uantwerpen.sc.models.Bot> allBots() {
        List<be.uantwerpen.sc.models.Bot> robotEntityList = botControlService.getAllBots();

        return robotEntityList;
    }

    /**
     * @param id The id of the bot that has to be returned
     * @return The specified Bot
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public be.uantwerpen.sc.models.Bot getBot(@PathVariable("id") Long id) {
        return botControlService.getBot(id);
    }

    /**
     * @param id  The id of the bot that you want to save
     * @param bot The Bot object to save
     */
    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public void saveBot(@PathVariable("id") Long id, @RequestBody be.uantwerpen.sc.models.Bot bot) {
        botControlService.saveBot(bot);
    }

    /**
     * @param id  The id of the bot that you want to update
     * @param bot The Bot object you want to update
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void updateBot(@PathVariable("id") Long id, @RequestBody be.uantwerpen.sc.models.Bot bot) {
        botControlService.updateBot(bot);
    }


    /**
     * Deprecated/unused
     *
     * @param id Id of the bot
     */
    @RequestMapping(value = "updateBotTest/{id}", method = RequestMethod.GET)
    public void updateBotTest(@PathVariable("id") Long id) {
        be.uantwerpen.sc.models.Bot botEntity = new be.uantwerpen.sc.models.Bot();
        botEntity.setId(id);
        botEntity.setState("Updated");
        botControlService.updateBot(botEntity);
    }


    /**
     * Deprecated/Unused
     *
     * @return Bot object
     */
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public be.uantwerpen.sc.models.Bot testRestBot() {
        return new be.uantwerpen.sc.models.Bot();
    }


    /**
     * Deprecated/Unused
     */
    @RequestMapping(value = "savetest", method = RequestMethod.GET)
    public void saveBotTest() {
        be.uantwerpen.sc.models.Bot bot = new be.uantwerpen.sc.models.Bot();
        bot.setState("test");
        botControlService.saveBot(bot);
    }

    /**
     * Deprecated/Unused
     */
    @RequestMapping(value = "goto/{id}/{rid}", method = RequestMethod.GET)
    public String goTo(@PathVariable("id") Long id, @PathVariable("rid") Long rid) {
        be.uantwerpen.sc.models.Bot botEntity = botControlService.getBot(rid);
        /*if (!pointEntities.contains(botEntity.getLinkId().getStopId())){
            pointEntities.add(botEntity.getLinkId().getStopId());
        }*/
        if (botEntity != null) {
            if (botEntity.getPercentageCompleted() >= 50) {
                // stack.push(botEntity.getLinkId().getStopId());
            }
        } else {
            System.out.println("Robot does not exist");
        }

        return "Something";
    }

    /**
     * When a new bot subscribes, it sends a request with its type and in return gets an ID
     *
     * @param type The type of the bot performing the request
     * @return An ID for the bot
     */
    //when new bot subscribes, it sends a request with its type and gets an ID in return
    @Produces("application/json")
    @RequestMapping(value = "newBot/{type}", method = RequestMethod.GET)
    public Long newRobot(@PathVariable("type") String type) {
        be.uantwerpen.sc.models.Bot bot = null;
        //check if requesting bot is car, drone, light or robot and return ID.
        if (type.equals("car")) {
            bot = new be.uantwerpen.sc.models.Car();
        } else if (type.equals("drone")) {
            bot = new be.uantwerpen.sc.models.Drone();
        } else if (type.equals("light")) {
//            bot = new be.uantwerpen.sc.models.TrafficLight();
        } else if (type.equals("robot")) {
            bot = new Robot();
        } else {
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

    /**
     * Deprecated/Unused
     */
    @RequestMapping(value = "{id}/lid/{lid}", method = RequestMethod.GET)
    public void locationLink(@PathVariable("id") Long id, @PathVariable("lid") Long lid) {
        be.uantwerpen.sc.models.Bot bot = this.getBot(id);
        Link link;

        if (bot != null) {
            link = linkControlService.getLink(lid);

            if (link != null) {
                bot.setLinkId(link);
                botControlService.updateBot(bot);
            } else {
                System.out.println("Link with id: " + lid + " not found!");
            }
        } else {
            System.out.println("Bot with id:" + id + " not found!");
        }
    }

    /**
     * Deprecated/Unused
     */
    public void updateLocation(Long id, int mm) {
        be.uantwerpen.sc.models.Bot bot = this.getBot(id);

        if (bot != null) {
            bot.setPercentageCompleted(mm);
            bot.setState("Updated");
            botControlService.saveBot(bot);
        }
    }

    /**
     * @param rid Deletes bot with this rid
     */
    @RequestMapping(value = "/delete/{rid}", method = RequestMethod.GET)
    public void deleteBot(@PathVariable("rid") Long rid) {
        botControlService.deleteBot(rid);
        System.out.println("Bot with id: " + rid + " deleted from DB");
    }

    /**
     * WARNING: Deletes all bots
     * Identical to deleteAll()
     */
    @RequestMapping(value = "/resetbots", method = RequestMethod.GET)
    public void resetBots() {
        botControlService.resetBots();
    }

    /**
     * @return JSON string with the location, id, idStart, idEnd and percentage completed of every available bot
     */
    @RequestMapping(value = "/getAllVehicles", method = RequestMethod.GET)
    public String posAll() {
        botControlService.setPosAll();
        return botControlService.getPosAll();
    }

    /**
     * @param id Id of the bot you want data from
     * @return JSON string with the location, id, idStart, idEnd and percentage completed of one bot
     */
    @RequestMapping(value = "/getOneVehicle/{id}", method = RequestMethod.GET)
    public String posOne(@PathVariable("id") Long id) {
        botControlService.setPosAll();
        return botControlService.getPosOne(id);
    }

    /**
     * WARNING: Deletes all bots
     * Identical to resetBots()
     *
     * @return String "Bots Destroyed..."
     */
    @RequestMapping(value = "/clearBots", method = RequestMethod.GET)
    public String deleteAll() {
        botControlService.resetBots();
        System.out.println("Bots Destroyed...");
        return "Bots Destroyed...";
    }
}
