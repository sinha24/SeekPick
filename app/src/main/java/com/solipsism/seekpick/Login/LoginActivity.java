package com.solipsism.seekpick.Login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.solipsism.seekpick.Dash.DashActivity;
import com.solipsism.seekpick.R;
import com.solipsism.seekpick.utils.PrefsHelper;

import java.util.Hashtable;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    AutoCompleteTextView email,password;
    Button login;
    Login log;
    String sEmail, sPassword;
    ImageView loginUp;
    Animation arrowShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (AutoCompleteTextView) findViewById(R.id.login_email);
        password = (AutoCompleteTextView) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login_button);
        loginUp = (ImageView) findViewById(R.id.login_up);
        arrowShake = AnimationUtils.loadAnimation(this, R.anim.arrorw_shake);
        loginUp.startAnimation(arrowShake);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    sEmail = email.getText().toString();
                    sPassword = password.getText().toString();
                    if (sEmail.length() > 0) {
                        if (sPassword.length() > 0) {
                            login("https://seekpick.herokuapp.com/login");
                            Intent i = new Intent(LoginActivity.this, DashActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            password.requestFocus();
                            password.setError("Check Password");
                        }
                    } else {
                        email.requestFocus();
                        email.setError("Check Email");
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Network isnt available", Toast.LENGTH_LONG).show();
                }
            }
        });

        ImageView loginUp = (ImageView) findViewById(R.id.login_up);
        TextView signUp = (TextView) findViewById(R.id.login_sign_up);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.up_in, R.anim.fade_out);
            }
        });
        loginUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.up_in, R.anim.fade_out);
            }
        });

    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void onGettingResponse() {
        Intent i = new Intent(LoginActivity.this, DashActivity.class);
        startActivity(i);
        finish();
    }


    public void login(String uri) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        log = LoginJsonParser.parsefeed(response);
                        if (log.getSuccess().equals("true")) {
                            PrefsHelper.getPrefsHelper(LoginActivity.this).savePref(PrefsHelper.PREF_TOKEN, log.getToken());
                            onGettingResponse();
                        } else {
                            Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Error in login ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<>();
                params.put("email", sEmail);
                params.put("password", sPassword);

                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
}
