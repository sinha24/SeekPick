package com.solipsism.seekpick.Dash;

import java.io.Serializable;

class Product implements Serializable {

    private String _id;
    private String ProName;
    private String ProPrice;
    private String ProTags;
    private String ProStatus;
    private String ProUpdate;

    String getProUpdate() {
        return ProUpdate;
    }

    void setProUpdate(String proUpdate) {
        ProUpdate = proUpdate;
    }

    String getProStatus() {
        return ProStatus;
    }

    void setProStatus(String proStatus) {
        ProStatus = proStatus;
    }

    String getProTags() {
        return ProTags;
    }

    void setProTags(String proTags) {
        ProTags = proTags;
    }

    String getProPrice() {
        return ProPrice;
    }

    void setProPrice(String proPrice) {
        ProPrice = proPrice;
    }

    String getProName() {
        return ProName;
    }

    void setProName(String proName) {
        ProName = proName;
    }

    String get_id() {
        return _id;
    }

    void set_id(String _id) {
        this._id = _id;
    }
}
