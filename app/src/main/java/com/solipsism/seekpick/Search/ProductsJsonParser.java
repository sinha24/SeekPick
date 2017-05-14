package com.solipsism.seekpick.Search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class ProductsJsonParser {
    static List<Product> parseFeed(String content) {
        List<Product> dataList = new ArrayList<>();

        try {
            JSONArray ar = new JSONArray(content);
            for (int i = ar.length() - 1; i >= 0; i--) {
                JSONObject obj = ar.getJSONObject(i);
                Product newProduct = new Product();
                newProduct.set_id(obj.getString("_id"));
                newProduct.setProName(obj.getString("name"));
                newProduct.setProPrice(obj.getString("price"));
                newProduct.setProTags(obj.getString("tags"));
                newProduct.setProStatus(obj.getString("status"));
                newProduct.setProUpdate(obj.getString("lastUpdate"));
                dataList.add(newProduct);
            }
            return dataList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}