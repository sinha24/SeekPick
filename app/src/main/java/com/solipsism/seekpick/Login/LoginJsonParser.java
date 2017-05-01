package com.solipsism.seekpick.Login;

import org.json.JSONException;
import org.json.JSONObject;

class LoginJsonParser {

    static Login parsefeed(String content) {
        Login log = new Login();
        try {

            JSONObject obje = new JSONObject(content);
            log.setSuccess(obje.getString("success"));
            log.setToken(obje.getString("token"));
            return log;

        } catch (JSONException e1) {
            e1.printStackTrace();
        }


        return log;
    }
}