package be.uantwerpen.sc.services;

import be.uantwerpen.sc.controllers.MapController;
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
    private static final Logger logger = LoggerFactory.getLogger(MapController.class);
    @Value("${backends.enabled}")
    boolean backendsEnabled;

    public JSONObject requestJsonObject(String url){

        String responseLine;
        String response = "";

        // used to parse strings to json
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
                System.out.println("got: " + response + "\nfrom:" +  url);
                try {
                    JSONObject responseObject;
                    logger.info("raw response:" + response + response.getClass());
                    responseObject = (JSONObject) parser.parse(response);
                    return responseObject;
                }
                catch(ParseException e){
                    System.out.println("could not parse folowing string: " + response);
                    System.out.println(e.getStackTrace());
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


    public float getWeight(BackendInfo mapinfo, int startPid, int stopPid){
        // Request weight between points from backend
        float weight ;
        if(backendsEnabled) {
            String url = "http://" + mapinfo.getHostname() + ":" + mapinfo.getPort() + "/" + startPid + "/" + stopPid;
            logger.info("--requesting from cost from:+" + url);
            JSONObject response = this.requestJsonObject(url);
            if(response != null) {
                logger.info("response: " + response.toString());
                weight = Float.parseFloat(response.get("cost").toString());
            }else{
                weight = 0;
                logger.warn("Got incompattible weight from backend: using weight " + weight );
            }
        }else {
            weight = (float)(Math.random() * 10); // to test wo/ backends
            logger.info("testing w/ random weight " + weight);
        }
        logger.info("--got weight: " + weight);
        return weight;
    }


}
