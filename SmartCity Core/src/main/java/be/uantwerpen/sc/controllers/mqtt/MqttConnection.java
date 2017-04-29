package be.uantwerpen.sc.controllers.mqtt;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * Created by Kevin on 26/04/2017.
 */
@Service
public class MqttConnection {

    private static final String broker = "tcp://143.129.39.118:1883";
    private static final String username = "root";
    private static final String password = "smartcity";
    private static final SimpleDateFormat SDF = new SimpleDateFormat("YYYYMMddHH:mm:ss");

    public MqttConnection()
    {

    }

    @PostConstruct
    private void postConstruct() {
        System.out.println("Service started");
        MemoryPersistence persistence = new MemoryPersistence();
        MqttClient client = null;
        try {
            client = new MqttClient(broker, "SmartCity_Core_Publisher_" + new Random().nextLong(), persistence);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);
            connectOptions.setUserName(username);
            connectOptions.setPassword(password.toCharArray());
            client.connect(connectOptions);
            System.out.println(SDF.format(new Date()) + " connected to MQTT");
            client.setCallback(new ConnectionCallback());
            client.subscribe("job/5");
            client.subscribe("pos/");
        } catch (MqttException ex) {
            System.out.println(SDF.format(new Date()) + " not connected to MQTT");
            if (!client.isConnected()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex1) {
                    Logger.getLogger(MqttConnection.class.getName()).log(Level.SEVERE, null, ex1);
                }
              //  connect();
            }
        }
    }


    static class ConnectionCallback implements MqttCallback {

        @Override
        public void connectionLost(Throwable thrwbl) {
            System.out.println(SDF.format(new Date()) + " Connection with MQTT lost");
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        String[] tab = topic.split("/", 2);
                        String dbName = tab[0];
                        String collName = (tab.length > 1) ? tab[1] : tab[0];
                        MongoClient mongoClient = new MongoClient("localhost", 27017);
                        DB db = mongoClient.getDB("messages");
                        DBCollection collection = db.getCollection("incoming");
                        BasicDBObject document = new BasicDBObject();
                        document.put("message:", message);
                        collection.insert(document);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken imdt) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}

