package com.solipsism.seekpick.Dash;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi joshi on 5/14/2017.
 */

public class ShopkeeperJsonParser {

   public static List<Shopkeeper> parsefeed(String content) {
        List<Shopkeeper> dataList = new ArrayList<>();

        try {
            JSONArray ar = new JSONArray(content);
            for (int i = ar.length() - 1; i >= 0; i--) {
                JSONObject obj = ar.getJSONObject(i);
                Shopkeeper newShop = new Shopkeeper();

                newShop.setShopid(obj.getString("_id"));
                newShop.setShopname(obj.getString("name"));
                newShop.setAddress(obj.getString("location"));
                newShop.setPhone(obj.getString("phone"));
                newShop.setPincode(obj.getString("pincode"));
                JSONObject objects = obj.getJSONObject("loc");
                JSONArray obj2 = objects.getJSONArray("coordinates");
                newShop.setLat(obj2.getDouble(0));
                newShop.setLng(obj2.getDouble(1));
                Log.e("lat ", i + 1 + ":- " + newShop.getLat());
                Log.e("long ", i + 1 + ":- " + newShop.getLng());
                dataList.add(newShop);
            }
            return dataList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
