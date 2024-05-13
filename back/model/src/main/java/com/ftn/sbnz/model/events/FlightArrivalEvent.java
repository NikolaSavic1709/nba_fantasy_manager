package com.ftn.sbnz.model.events;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.io.Serializable;
import java.util.Date;

@Role(Role.Type.EVENT)
@Timestamp("arrivalTime")
@Expires("2h30m")
public class FlightArrivalEvent implements Serializable {
    private static final long serialVersionUID=1L;
    private Date arrivalTime;
    private Long flightId;
    public FlightArrivalEvent(){
        super();
    }
    public FlightArrivalEvent(Long flightId, Long timestamp){
        super();
        this.arrivalTime=new Date();
        this.arrivalTime.setTime(this.arrivalTime.getTime()+timestamp);
        this.flightId=flightId;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }
}
