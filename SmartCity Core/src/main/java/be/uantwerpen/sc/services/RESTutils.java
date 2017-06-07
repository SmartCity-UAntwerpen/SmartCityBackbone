package be.uantwerpen.sc.services;

/**
 * Created by quent on 6/7/2017.
 */

import javax.ws.rs.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

class RESTUtils {

    private WebTarget webTarget;


    public RESTUtils(String URL) {
        Client client = ClientBuilder.newClient();
        webTarget = client.target(URL);
    }

    public String getJSON(String URL){
        WebTarget resourceWebTarget = webTarget.path(URL);
        Invocation.Builder invocationBuilder = resourceWebTarget.request("application/json");
        Response response = null;
        System.out.println("REST Attempting GET(JSON) request with URL:" + resourceWebTarget.getUri());
        try {
            response = invocationBuilder.get();
        } catch (ProcessingException e) {
            System.out.println("REST Cannot connect to REST service: " + e);
            System.exit(0);
        }

        String responseString = response.readEntity(String.class);
        System.out.println("REST JSON Returned from request '"+ URL + "' is: " + responseString);
        return responseString;
    }
}