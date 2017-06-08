package be.uantwerpen.sc.models;

import javax.persistence.*;

/**
 * Created by Quentin Van Ravels on 26-Apr-17.
 */

@Entity
@DiscriminatorValue("robot")
public class Robot extends Bot{

    @Override
    public String toString() {

        Long idStart = 0L;
        Long idStop = 0L;
        Integer percentage = 0;

        if(getLinkId()!=null){
            idStart = getLinkId().getStartPoint().getId();
        }

        if(getLinkId()!=null){
            idStop = getLinkId().getStopPoint().getId();
        }

        if(getPercentageCompleted()!=null){
            percentage = getPercentageCompleted();
        }
        return "{" +
                "\"idVehicle\" :" + getId() +
                ", \"idStart\" : " + idStart +
                ", \"idEnd\" : " + idStop +
                ", \"percentage\" : " + percentage +
                ", \"type\" : \"robot\"}";
    }

}