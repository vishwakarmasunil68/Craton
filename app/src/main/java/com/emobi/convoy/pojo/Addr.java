package com.emobi.convoy.pojo;

/**
 * Created by sunil on 18-05-2017.
 */

public class Addr {
    double lattitude;
    double longitude;

    public Addr(double lattitude, double longitude) {
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public Addr() {
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Addr{" +
                "lattitude=" + lattitude +
                ", longitude=" + longitude +
                '}';
    }
}