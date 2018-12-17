package be.uantwerpen.sc.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BackendInfo {
    @Id
    private int mapId;
    private String hostname;
    private int port;
    //TODO maybe allow multiple maps
    private String name;

    public BackendInfo() {
    }

    public BackendInfo(String hostname, int port, int mapId, String name) {
        this.hostname = hostname;
        this.port = port;
        this.mapId = mapId;
        this.name = name;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Backend "+name+" at "+hostname+":"+port+" for map "+mapId;
    }

}
