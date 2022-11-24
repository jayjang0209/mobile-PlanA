package com.example.googlemaptest;

import java.util.Objects;

public class EvStation {
    public Double latitude;
    public Double longitude;
    public String address;

    public EvStation(Double lat, Double lng, String addr) {
        latitude = lat;
        longitude = lng;
        address = addr;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvStation evStation = (EvStation) o;
        return Objects.equals(latitude, evStation.latitude) && Objects.equals(longitude, evStation.longitude) && Objects.equals(address, evStation.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, address);
    }

    @Override
    public String toString() {
        return "EvStation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                '}';
    }
}
