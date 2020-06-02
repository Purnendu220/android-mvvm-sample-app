package com.ausadhi.mvvm.data.network.model;

import java.io.Serializable;

public class AddressModel implements Serializable {
    String id;
    String name ;
    String phone;
    String pincode ;
    String houseno ;
    String roadname;
    String city ;
    String state;
    String landmark ;

    public AddressModel(String id,String name, String phone, String pincode, String houseno, String roadname, String city, String state, String landmark) {
       this.id=id;
        this.name = name;
        this.phone = phone;
        this.pincode = pincode;
        this.houseno = houseno;
        this.roadname = roadname;
        this.city = city;
        this.state = state;
        this.landmark = landmark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getHouseno() {
        return houseno;
    }

    public void setHouseno(String houseno) {
        this.houseno = houseno;
    }

    public String getRoadname() {
        return roadname;
    }

    public void setRoadname(String roadname) {
        this.roadname = roadname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
