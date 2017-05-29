package be.uantwerpen.sc.models;

import javax.persistence.*;

/**
 * Created by Quentin Van Ravels on 26-Apr-17.
 */

@Entity
@DiscriminatorValue("drone")
public class Drone extends Bot{

    private float x;
    private float y;
    private float z;

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

}
