package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.Bot;
import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.repositories.BotRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import com.google.gson.JsonParser;
import org.springframework.web.util.UriComponentsBuilder;
import sun.rmi.runtime.Log;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.ProcessingException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

    @Autowired
    private LinkControlService linkControlService;

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

        String stringDrone = "[]";
        String stringCar = "[]";
        String stringRobot = "[]";

        try {

            URL urlDrone = new URL("http://143.129.39.151:8082/posAll");
            URL urlCar = new URL("http://143.129.39.151:8081/carmanager/posAll");
            URL urlRobot = new URL("http://143.129.39.151:8082/posAll");

            HttpURLConnection connDrone = (HttpURLConnection) urlDrone.openConnection();
            HttpURLConnection connCar = (HttpURLConnection) urlCar.openConnection();
            HttpURLConnection connRobot = (HttpURLConnection) urlRobot.openConnection();
            connDrone.setRequestMethod("GET");
            connCar.setRequestMethod("GET");
            connRobot.setRequestMethod("GET");

            connDrone.setRequestProperty("Accept", "application/json");
            connCar.setRequestProperty("Accept", "application/json");
            connRobot.setRequestProperty("Accept", "application/json");

            if (connDrone.getResponseCode() == 200) {
                BufferedReader brDrone = new BufferedReader(new InputStreamReader((connDrone.getInputStream())));
                while ((stringDrone = brDrone.readLine()) != null) {
                     //System.out.println(stringDrone + "\n");
                }
            }

            if (connCar.getResponseCode() == 200) {
                BufferedReader brCar = new BufferedReader(new InputStreamReader((connCar.getInputStream())));
                while ((stringCar = brCar.readLine()) != null) {
                    // System.out.println(stringCar + "\n");
                }
            }

            if (connRobot.getResponseCode() == 200) {
                BufferedReader brRobot= new BufferedReader(new InputStreamReader((connRobot.getInputStream())));
                while ((stringRobot = brRobot.readLine()) != null) {
                    //  System.out.println(stringRobot + "\n");
                }
            }

            connDrone.disconnect();
            connCar.disconnect();
            connRobot.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonParser parser = new JsonParser();

        if(stringDrone != null && stringDrone.length()>2){
            stringDrone = stringDrone.substring(0, stringDrone.length()-1) + ",";
        }else{
            stringDrone = "[ ";
        }

        if(stringCar != null && stringCar.length()>2) {
            stringCar = stringCar.substring(1);
            stringCar = stringCar.substring(0, stringCar.length() - 1) + ",";
        }else{
            stringCar = " ";
        }

        if(stringRobot != null && stringRobot.length()>2){
            stringRobot = stringRobot.substring(1);

        }else{
            stringRobot = "]";
        }

        String stringVehicles = stringDrone + stringCar + stringRobot;

        //System.out.println("string vehicles: " + stringVehicles);

        JsonElement jsonVehicles = parser.parse(stringVehicles);
        JsonArray vehicleArray = jsonVehicles.getAsJsonArray();

        Iterator<JsonElement> iterator = vehicleArray.iterator();

        while(iterator.hasNext())
        {
            JsonObject vehicle = (JsonObject) iterator.next();
            Long id = vehicle.get("idVehicle").getAsLong();
            Long start = vehicle.get("idStart").getAsLong();
            Long stop = vehicle.get("idEnd").getAsLong();
            int percentage = vehicle.get("percentage").getAsInt();

            Bot bot = botRepository.findOne(id);
            if(bot!=null){
                for(Link link : linkControlService.getAllLinks()) {
                    if (link.getStartPoint().getId().equals(start) && link.getStopPoint().getId().equals(stop)) {
                        bot.setLinkId(link);
                    }
                }

                bot.setPercentageCompleted(percentage);
                saveBot(bot);
            }
        }
    }

    public String getPosAll() {

        String str = "{\"vehicles\" : [ ";
        for(Bot bot : getAllBots()){
            str = str + bot.toString() + ",";
        }
        return str.substring(0, str.length()-1) + "]}";
    }

    public String getPosOne(Long id) {

        return getBot(id).toString();
    }
}
