package com.example.googlemaptest;

public class Ev {
    public String manufacturer;
    public String model;
    public String year;
    public double range;

    public Ev() {

    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        // Convert miles to km
        this.range = range * 1.60934;
    }

    @Override
    public String toString() {
        return model;
    }
}
