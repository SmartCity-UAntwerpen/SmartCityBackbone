package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.services.LinkControlService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/link/")
public class LinkController {
    @Autowired
    private LinkControlService linkControlService;

    @Autowired
    private ResourceLoader resourceLoader;

    // TODO get the values from application.properties
//    @Value("${bot.ip}")
    @Value("localhost")
    String ip;

    @Value("${drone.port}")
    String dronePort;

    @Value("${car.port}")
    String carPort;

    @Value("${robot.port}")
    String robotPort;





//    @Value("classpath:json/resource-data.txt")
//    Resource resourceFile;
    /**
     * @return A List of Link objects, acquired from LinkControlService
     */

    @RequestMapping(method = RequestMethod.GET)
    public List<Link> allLinks() {
        List<Link> linkEntityList = linkControlService.getAllLinks();
        return linkEntityList;
    }


/* getPathLinks
*  endpoint to get the entire transitmap, MaaS will call this
*  @param pidstart(int): startpoint id
*  @param pidened(int): endpoint id "destination point"
*  @return : an array of transit link objects
*
*/
    @RequestMapping(value = "pathlinks", method = RequestMethod.GET)
    public JSONArray getPathLinks(@RequestParam int pidstart, @RequestParam int pidend ) {
        JSONArray linkArray = new JSONArray();
        // activate when testing without database (reads data from jsonfiles)
        boolean TEST = true;
        // header for testing purposes
        JSONObject header = new JSONObject();
        header.put("pidstart", pidstart);
        header.put("pidend", pidend);
        linkArray.add(header);


        if(TEST){
            JSONParser parser = new JSONParser();
            String fileName = "graphtest.json";

//            Resource resource = applicationContext.getResource("classpath:"+ fileName);
//            InputStream is = resource.getInputStream();
            try {
//                JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(fileName));
                // get the json file from the resource folder

                Resource resource = resourceLoader.getResource("classpath:json/" + fileName);
                String absolutePath = resource.getURI().toString();
                absolutePath = absolutePath.substring(6,absolutePath.length());
                absolutePath = absolutePath.replace('/', '\\');    //
                absolutePath = absolutePath.replace("%20", " " ); // catch any spaces in filenames (get converted to %20 in getURI)
                System.out.println("path to json:" + absolutePath);

                JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(absolutePath));

                for(Object l : jsonArray){
                    JSONObject link = (JSONObject) l;
                    linkArray.add(link);
                }
            }
            catch(Exception e){
                System.out.println("Reading " + fileName + " failed" + e.getStackTrace());
            }
        }else{
            // TODO get linkArrays from the backends
                // TODO build calls and parsing

        }
        return linkArray ;

    }

    /* TEST METHOD: getDroneLinks calling this endpooint triggers a call to get a backennds transitlinks
     *  endpoint to get the entire transitmap, MaaS will call this
     *  @return an array of links of the drone transit map
     */
    @RequestMapping(value = "testLinks", method = RequestMethod.GET)
    public JSONArray gettestLinks( ) {
        return requestFromBackend("http://localhost", "8081","/link/transitmap" );
    }

    /* a generic method do do a request to a server and parse the response to a JSONArray
     *
     * @param int :
     *
     * @return JSONArray : backend response
     */
    private JSONArray requestFromBackend(String ip, String port, String endpoint){
        String responseLine;
        String response = "";

        // used to parse strings to json
        JSONParser parser = new JSONParser();
        JSONArray linkArray = new JSONArray();
        try {
            URL urlCar = new URL(ip + ":" + port + endpoint);
            HttpURLConnection conn = (HttpURLConnection) urlCar.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                while ((responseLine = br.readLine()) != null) {
                    response += responseLine;
                }
                System.out.println("got: " + response + "\nfrom:" + ip + ":" + port + endpoint);
                try {
                    JSONArray jsonArray = (JSONArray) parser.parse(response);
                    return jsonArray;
                }
                catch(ParseException e){
                    System.out.println("could not parse folowwing string: " + response);
                    System.out.println(e.getStackTrace());
                }
            } else {
                System.out.println("Request Failed, Responsecode returned: " + conn.getResponseCode());
            }

        }catch(IOException e){
            e.printStackTrace();
        }

        return linkArray ;
    }
}

