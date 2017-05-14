package com.solipsism.seekpick.Search;

import java.io.Serializable;

/**
 * Created by ravi joshi on 5/14/2017.
 */

public class Shopkeeper implements Serializable {

    private String Shopid;
    private String shopname, address, pincode, phone;
    private double lat, lng;

    public String getShopid() {
        return Shopid;
    }

    public void setShopid(String shopid) {
        Shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}