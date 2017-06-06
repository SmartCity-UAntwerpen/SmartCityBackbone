package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.Bot;
import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.repositories.BotRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.JsonParser;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Thomas on 25/02/2016.
 */
@Service
public class BotControlService
{
    @Autowired
    private BotRepository botRepository;

    public Bot saveBot(Bot bot)
    {
        return botRepository.save(bot);
    }

    public Bot getBot(Long id)
    {
        return botRepository.findOne(id);
    }

    public List<Bot> getAllBots()
    {
        return botRepository.findAll();
    }

    public void updateBot(Bot bot)
    {
        Bot dbBot = botRepository.findOne(bot.getId());
        dbBot = bot;
        //dbBot.setLinkId(bot.getLinkId());
        botRepository.save(dbBot);
    }

    public boolean deleteBot(long rid)
    {
        if(this.getBot(rid) == null)
        {
            //Could not find bot with rid
            return false;
        }
        else
        {
            botRepository.delete(rid);
            return true;
        }
    }

    public boolean resetBots()
    {
        botRepository.deleteAll();

        return true;
    }

    public void setPosAll() {

        LinkControlService service =  new LinkControlService();

        JsonParser parser = new JsonParser();

        JsonElement json = parser.parse("[{\"idVehicle\":1,\"idStart\":1,\"idEnd\":2,\"percentage\":75},{\"idVehicle\":2,\"idStart\":1,\"idEnd\":2,\"percentage\":50}]");
        JsonArray vehicleArray = json.getAsJsonArray();

        Iterator<JsonElement> iterator = vehicleArray.iterator();
        while(iterator.hasNext())
        {
            JsonObject vehicle = (JsonObject) iterator.next();
            Long id = vehicle.get("idVehicle").getAsLong();
            Long start = vehicle.get("idStart").getAsLong();
            Long stop = vehicle.get("idStop").getAsLong();
            int percentage = vehicle.get("percentage").getAsInt();

            System.out.println(id + "\t" + start + "\t" + stop + "\t" + percentage);

            Bot bot = botRepository.findOne(id);
            if(bot!=null){
                for(Link link : service.getAllLinks()) {
                    if (link.getStartPoint().getId().equals(start) && link.getStopPoint().getId().equals(stop)) {
                        bot.setLinkId(link);
                    }
                }

                bot.setPercentageCompleted(percentage);
                saveBot(bot);
            }
        }
    }

    public void getPosAll() {
        //posall subcores

    }
}
