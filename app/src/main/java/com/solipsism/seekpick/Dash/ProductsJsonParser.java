package com.solipsism.seekpick.Dash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sakshi on 4/30/2017.
 */

public class ProductsJsonParser {
    public  static List<Product> parsefeed(String content) {
        List<Product> dataList = new ArrayList<>();

        try {
            JSONArray  ar = new JSONArray(content);


            for (int i = ar.length() - 1; i >= 0; i--) {
                JSONObject obj = ar.getJSONObject(i);
                Product newProduct = new Product();
                newProduct.set_id(obj.getString("_id"));
                newProduct.setProName(obj.getString("name"));
                newProduct.setProPrice(obj.getString("price"));
                newProduct.setProTags(obj.getString("tags"));
                dataList.add(newProduct);

            }
            return dataList;
        }catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }

    }}