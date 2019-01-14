package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.BackendInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class BackendService {
    private static final Logger logger = LoggerFactory.getLogger(BackendService.class);
    @Value("${backends.enabled}")
    boolean backendsEnabled;

    public JSONObject requestJsonObject(String url){

        String responseLine;
        String response = "";

        //Used to parse strings to json
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = new JSONObject();
        try {
            URL urlCar = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlCar.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                while ((responseLine = br.readLine()) != null) {
                    response += responseLine;
                }
                logger.debug("Got: " + response + "\nFrom:" +  url);
                try {
                    JSONObject responseObject;
                    logger.debug("Raw response:" + response + response.getClass());
                    responseObject = (JSONObject) parser.parse(response);
                    return responseObject;
                }
                catch(ParseException e){
                    logger.warn("could not parse folowing string: " + response);
                    logger.warn(e.getStackTrace().toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            } else {
                System.out.println("Request Failed, Responsecode returned: " + conn.getResponseCode());
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        return jsonResponse;
    }
    /* a wrapper method to call requestFromBackend wo/ parameters (see method below)
     *
     *
     */
    public JSONArray requestFromBackend(String ip, String port, String endpoint) {
        return requestFromBackend(ip, port, endpoint, "");
    }

    // TODO define get/post in String method parameter
    /* a generic method do do a request to a server and parse the response to a JSONArray
     *
     * @param String ip: server ip
     * @param String port: server port
     * @param endpoint : REST endpoint
     * @param parameters : optional parameters "val1=foo&val2=bar"
     * @return JSONArray : backend response
     */
    public JSONArray requestFromBackend(String ip, String port, String endpoint, String parameters){
        String responseLine;
        String response = "";

        // used to parse strings to json
        JSONParser parser = new JSONParser();
        JSONArray linkArray = new JSONArray();
        try {
            URL urlCar = new URL(ip + ":" + port + endpoint + "?" + parameters);
            HttpURLConnection conn = (HttpURLConnection) urlCar.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                while ((responseLine = br.readLine()) != null) {
                    response += responseLine;
                }
                logger.debug("Got: " + response + "\nFrom:" + ip + ":" + port + endpoint);
                try {
                    JSONArray jsonArray = (JSONArray) parser.parse(response);
                    return jsonArray;
                }
                catch(ParseException e){
                    logger.warn("Could not parse folowing string: " + response);
                    logger.warn(e.getStackTrace().toString());
                }
            } else {
                logger.error("Request Failed! Responsecode returned: " + conn.getResponseCode());
            }

        }catch(IOException e){
            e.printStackTrace();
        }

        return linkArray ;
    }


    public float getWeight(BackendInfo mapinfo, int startPid, int stopPid){
        // Request weight between points from backend
        float weight ;
        if(backendsEnabled) {
            String url = "http://" + mapinfo.getHostname() + ":" + mapinfo.getPort() + "/" + startPid + "/" + stopPid;
            logger.info("--Requesting cost from: " + url);
            JSONObject response = this.requestJsonObject(url);
            if(response != null) {
                logger.debug("Response: " + response.toString());
                try {
                    weight = Float.parseFloat(response.get("cost").toString());
                }catch(Exception e){
                    logger.warn("Could not parse ''cost'' from response!");
                    weight = 0;
                }
            }else{
                weight = 0;
                logger.warn("Got incompatible weight from backend: Using weight " + weight );
            }
        }else {
            weight = (float)(Math.random() * 10); // to test wo/ backends
            logger.debug("Testing w/ random weight: " + weight);
        }
        logger.info("--Got weight: " + weight);
        return weight;
    }


}
