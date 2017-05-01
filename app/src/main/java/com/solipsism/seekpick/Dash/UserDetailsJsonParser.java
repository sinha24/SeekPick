package com.solipsism.seekpick.Dash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserDetailsJsonParser {

    public static UserDetails parsefeed(String content) {
        UserDetails newUser = new UserDetails();
        try {
            JSONArray ar = new JSONArray(content);
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                newUser.setId(obj.getString("_id"));
                newUser.setEmail(obj.getString("email"));
                newUser.setPassword(obj.getString("password"));
                newUser.setUsername(obj.getString("username"));
                newUser.setLocation(obj.getString("location"));
                newUser.setName(obj.getString("name"));
                newUser.setPincode(obj.getString("pincode"));
                newUser.setPhone(obj.getString("phone"));
                return newUser;
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return newUser;
    }
}