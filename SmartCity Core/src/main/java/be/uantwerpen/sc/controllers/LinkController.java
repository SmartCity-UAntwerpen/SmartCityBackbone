package be.uantwerpen.sc.controllers;

import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.services.BackendService;
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
    private BackendService backendService;

    @Autowired
    private ResourceLoader resourceLoader;

    // TODO get the values from application.properties
    @Value("${dummy.json.path}")
    String dummyJsonFilePath;

    String ip;

    @Value("${drone.ip}")
    String droneip;

    @Value("${car.ip}")
    String carip;

    @Value("${robot.ip}")
    String robotip;

    @Value("${drone.port}")
    String dronePort;

    @Value("${car.port}")
    String carPort;

    @Value("${robot.port}")
    String robotPort;

    @Value("${local.files}")
    boolean localfiles;





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
        boolean LOCALDATA = localfiles;
        // header for testing purposes


        if(LOCALDATA){
            JSONParser parser = new JSONParser();


            String filePath = dummyJsonFilePath;

//            Resource resource = applicationContext.getResource("classpath:"+ fileName);
//            InputStream is = resource.getInputStream();
            try {
//                JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(fileName));
                // get the json file from the resource folder

                Resource resource = resourceLoader.getResource(filePath);
                String absolutePath = resource.getURI().toString();
                absolutePath = absolutePath.substring(6,absolutePath.length());
                absolutePath = absolutePath.replace('/', '\\');    //
                absolutePath = absolutePath.replace("%20", " " ); // catch any spaces in filenames (get converted to %20 in getURI)
                System.out.println("path to json:" + absolutePath);

                JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(filePath));

                for(Object l : jsonArray){
                    JSONObject link = (JSONObject) l;
                    linkArray.add(link);
                }
            }
            catch(Exception e){
                System.out.println("Reading " + filePath + " failed" + e.getStackTrace());
            }
        }else{
            // TODO get linkArrays from the backends localhost)
            JSONArray transitLinkArray = new JSONArray();
            JSONArray responseArray = new JSONArray();
            // specifying port to spoof different backends on one single localhost server ( responds json accordingly
            // request carlinks
            responseArray = backendService.requestFromBackend(carip, carPort,"/link/transitmap", "port=8081" );
            transitLinkArray.addAll(responseArray);

            // request robotlinks
            responseArray = backendService.requestFromBackend(droneip, dronePort,"/link/transitmap", "port=8082" );
            transitLinkArray.addAll(responseArray);

            // request dronelinks
            responseArray = backendService.requestFromBackend(robotip, robotPort,"/link/transitmap", "port=8083" );
            transitLinkArray.addAll(responseArray);


            return transitLinkArray;
            // TODO build calls and parsing

        }
        return linkArray ;

    }






}

