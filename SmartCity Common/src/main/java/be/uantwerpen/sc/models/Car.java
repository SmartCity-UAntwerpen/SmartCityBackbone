package be.uantwerpen.sc.models;

import javax.persistence.*;

/**
 * Created by Quentin Van Ravels on 26-Apr-17.
 */

@Entity
@DiscriminatorValue("car")
public class Car extends Bot{

    private float x;
    private float y;
    private float z;
    private float w;

    @Basic
    @Column(name = "x")
    public float getX(){return x;}
    public void setX(float x){this.x = x;}

    @Basic
    @Column(name = "y")
    public float getY(){return y;}
    public void setY(float y){this.y = y;}

    @Basic
    @Column(name = "z")
    public float getZ(){return z;}
    public void setZ(float z){this.z = z;}

    @Basic
    @Column(name = "w")
    public float getW(){return w;}
    public void setW(float w){this.w = w;}

    @Override
    public String toString() {


        Long idStart = 0L;
        Long idStop = 0L;

        if(getLinkId()!=null){
            idStart = getLinkId().getStartPoint().getId();
        }

        if(getLinkId()!=null){
            idStop = getLinkId().getStopPoint().getId();
        }

        String str = "{" +
                "\"idVehicle\" :" + getId() +
                ", \"idStart\" : " + idStart +
                ", \"idEnd\" : " + idStop +
                ", \"percentage\" : " + getPercentageCompleted() +
                ", \"type\" : \"car\"}";
        return str;
    }

}