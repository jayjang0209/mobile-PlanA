package com.example.googlemaptest;

import java.util.Objects;

public class EvStation {
    public Double latitude;
    public Double longitude;
    public String address;



    public String type;

    public EvStation(Double lat, Double lng, String addr, String chargerType) {
        latitude = lat;
        longitude = lng;
        address = addr;
        type = chargerType;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

}
