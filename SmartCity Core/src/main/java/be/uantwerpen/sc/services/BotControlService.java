package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.Bot;
import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.repositories.BotRepository;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    //sets up a connection to retrieve the start and stop point of bots from the vehicle cores
    //every received bot that is present in the database is updated with the given information
    //if the start an stop point given do not match wit a legal link, idStart and idEnd is set to 0
    //if the percentageCompleted of the vehicle is 100, the vehicle is set to a link that ends with the given idEnd
    public void setPosAll() {

        String received;
        String stringDrone = "";
        String stringCar = "";
        String stringRobot = "";

        try {

            URL urlDrone = new URL("http://143.129.39.151:8082/posAll");
            URL urlCar = new URL("http://143.129.39.151:8081/carmanager/posAll");
            URL urlRobot = new URL("http://143.129.39.151:8083/bot/posAll");

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
                while ((received = brDrone.readLine()) != null) {
                     System.out.println("Response from dronecore: " + received);
                     stringDrone = received;
                }
            }else {
                System.out.println("Responsecode Drone returned: " + connDrone.getResponseCode());
            }

            if (connCar.getResponseCode() == 200) {
                BufferedReader brCar = new BufferedReader(new InputStreamReader((connCar.getInputStream())));
                while ((received= brCar.readLine()) != null) {
                     System.out.println("Response from carcore: " + received);
                     stringCar = received;
                }
            }else {
                System.out.println("Responsecode Car returned: " + connCar.getResponseCode());
            }

            if (connRobot.getResponseCode() == 200) {
                BufferedReader brRobot= new BufferedReader(new InputStreamReader((connRobot.getInputStream())));
                while ((received = brRobot.readLine()) != null) {
                      System.out.println("Response from robotcore: " + received);
                      stringRobot = received;
                }
            }else {
                System.out.println("Responsecode Robot returned: " + connRobot.getResponseCode());
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

        if(!stringDrone.isEmpty() && stringDrone.length() > 3){
            stringDrone = stringDrone.substring(0, stringDrone.length()-1) + ",";
        }else{
            stringDrone = "[ ";
        }

        if(!stringCar.isEmpty() && stringCar.length() > 3) {
            stringCar = stringCar.substring(1);
            stringCar = stringCar.substring(0, stringCar.length() - 1) + ",";
        }else{
            stringCar = "";
        }

        if(!stringRobot.isEmpty() && stringRobot.length() > 3){
            stringRobot = stringRobot.substring(1);

        }else{
            stringRobot = "]";
            if(!stringCar.isEmpty() && stringCar.length() > 3){
                stringCar = stringCar.substring(0, stringCar.length() - 1);
            }
        }

        String stringVehicles = stringDrone + stringCar + stringRobot;
        System.out.println("vehicleString: " + stringVehicles);


        JsonElement jsonVehicles = parser.parse(stringVehicles);
        JsonArray vehicleArray = jsonVehicles.getAsJsonArray();

        Iterator<JsonElement> iterator = vehicleArray.iterator();

        while(iterator.hasNext())
        {
            JsonElement element = iterator.next();

            if (!(element instanceof JsonNull)) {
                JsonObject vehicle = (JsonObject) element;
                Long id = vehicle.get("idVehicle").getAsLong();
                Long start = vehicle.get("idStart").getAsLong();
                Long stop = vehicle.get("idEnd").getAsLong();
                int percentage = vehicle.get("percentage").getAsInt();


                Bot bot = botRepository.findOne(id);
                if(bot!=null){

                    System.out.println("idVehicle" + id + ", idStart: " + start + ", idEnd: " + stop + ", percentage: " + percentage);


                    bot.setPercentageCompleted(percentage);

                    if(percentage==100){
                        for(Link link : linkControlService.getAllLinks()) {
                            if (link.getStopPoint().getId().equals(stop)) {
                                bot.setLinkId(link);
                                System.out.println("parked: " +id);
                                break;
                            }
                        }
                    }

                    for(Link link : linkControlService.getAllLinks()) {
                        if (link.getStartPoint().getId().equals(start) && link.getStopPoint().getId().equals(stop)) {
                            bot.setLinkId(link);
                            break;
                        }
                    }

                    bot.setPercentageCompleted(percentage);
                    saveBot(bot);
                }else{
                    System.out.println("bot with id \'" + id + "\' not found");
                }
            }
        }
    }

    //returns jsonString with the location of every available vehicle
    public String getPosAll() {

        String str = "{\"vehicles\" : [ ";
        for(Bot bot : getAllBots()){
            str = str + bot.toString() + ",";
        }
        return str.substring(0, str.length()-1) + "]}";
    }

    //returns jsonString with the location of a vehicle
    public String getPosOne(Long id) {

        return getBot(id).toString();
    }
}
