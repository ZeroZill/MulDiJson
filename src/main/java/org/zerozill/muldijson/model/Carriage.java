package org.zerozill.muldijson.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class Carriage {

    @JsonField
    public Carriage nextCarriage;

    @JsonField
    public String carriageId;

    @JsonField
    public int passengerNo;

    @JsonField
    public String carriageType;

    @JsonField
    public boolean available;

    public Carriage()
    {

    }

    public Carriage(String carriageId, int passengerNo, String carriageType, boolean available) {
        this.carriageId = carriageId;
        this.passengerNo = passengerNo;
        this.carriageType = carriageType;
        this.available = available;
        nextCarriage = null;
    }

    public Carriage(Carriage nextCarriage, String carriageId, int passengerNo, String carriageType, boolean available) {
        this.nextCarriage = nextCarriage;
        this.carriageId = carriageId;
        this.passengerNo = passengerNo;
        this.carriageType = carriageType;
        this.available = available;
    }
}
