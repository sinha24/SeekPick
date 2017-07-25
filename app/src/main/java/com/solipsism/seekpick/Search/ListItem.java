package com.solipsism.seekpick.Search;

import java.io.Serializable;

class ListItem implements Serializable {


    private String oid, name, price, shopid,status,lastupdate;

    String getStatus() {
        return status;
    }

    void setStatus(String status) {
        this.status = status;
    }

    String getLastupdate() {
        return lastupdate;
    }

    void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    String getShopid() {
        return shopid;
    }

    void setShopid(String shopid) {
        this.shopid = shopid;
    }

    private String shopname, address, pincode, phone;
    private double lat, lng;


    String getAddress() {
        return address;
    }

    String getShopname() {
        return shopname;
    }

    void setShopname(String shopname) {
        this.shopname = shopname;
    }

    String getPrice() {
        return price;
    }

    void setPrice(String price) {
        this.price = price;
    }

    String getOid() {
        return oid;
    }

    void setOid(String oid) {
        this.oid = oid;
    }

    void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getPincode() {
        return pincode;
    }

    void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    double getLat() {
        return lat;
    }

    void setLat(double lat) {
        this.lat = lat;
    }

    double getLng() {
        return lng;
    }

    void setLng(double lng) {
        this.lng = lng;
    }
}
