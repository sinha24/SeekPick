package com.solipsism.seekpick.Dash;

/**
 * Created by sakshi on 4/30/2017.
 */

public class Product {

    private String _id;
    private   String ProName;
    private   String ProPrice;
    private  String ProTags;

    public String getProTags() {
        return ProTags;
    }

    public void setProTags(String proTags) {
        ProTags = proTags;
    }

    public  String getProPrice() {
        return ProPrice;
    }

    public void setProPrice(String proPrice) {
        ProPrice = proPrice;
    }

    public  String getProName() {
        return ProName;
    }

    public void setProName(String proName) {
        ProName = proName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

   /* public  String getName() {
        return ProName;
    }

    public String getPrice() {
        return ProPrice;    }

    public String getTag() {
        return ProTags;
    }

    public String getUniqueid() {
        return _id;
    }*/
}
