package com.solipsism.seekpick.Search;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchJsonParser {
    public  static List<ListItem> parsefeed(String content){
        try{
            JSONArray ar =new JSONArray(content);
            List<ListItem> dataList = new ArrayList<>();
            for(int i=0; i<ar.length();i++){
                JSONObject obj = ar.getJSONObject(i);
                ListItem newItem = new ListItem();

                newItem.setOid(obj.getString("_id"));
                newItem.setName(obj.getString("name"));
                newItem.setPrice(obj.getString("price"));
                Log.e("id ",i+1+":- "+newItem.getOid());
                Log.e("name ",i+1+":- "+newItem.getName());
                Log.e("price ",i+1+":- "+newItem.getPrice());
                JSONArray obj1= obj.getJSONArray("shopkeeper_docs");
                JSONObject arrObject=obj1.getJSONObject(0);

                newItem.setShopid(arrObject.getString("_id"));
                newItem.setShopname(arrObject.getString("name"));
                newItem.setPhone(arrObject.getString("phone"));
                newItem.setAddress(arrObject.getString("location"));
                Log.e("shopid ",i+1+":- "+newItem.getShopid());
                Log.e("shopname ",i+1+":- "+newItem.getShopname());
                Log.e("phone ",i+1+":- "+newItem.getPhone());
                Log.e("location ",i+1+":- "+newItem.getAddress());
                JSONObject objects=arrObject.getJSONObject("loc");
                JSONArray obj2= objects.getJSONArray("coordinates");
                newItem.setLat(obj2.getString(0));
                newItem.setLng(obj2.getString(1));
                Log.e("lat ",i+1+":- "+newItem.getLat());
                Log.e("long ",i+1+":- "+newItem.getLng());
                dataList.add(newItem);
            }
            return dataList;
        }catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }

    }
}
