package be.uantwerpen.sc.models;

import javax.persistence.*;

/**
 * Created by Quentin Van Ravels on 26-Apr-17.
 */

@Entity
@DiscriminatorValue("robot")
public class Robot extends Bot{

    /**
     * {@inheritDoc}
     *
     * @return string of this vehicles' id, idStart, idEnd, percentageCompleted and its type (robot)
     */
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
                ", \"type\" : \"robot\"}";

        return str;
    }

}