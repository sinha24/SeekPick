package com.solipsism.seekpick.Login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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
import com.solipsism.seekpick.Search.SearchActivity;
import com.solipsism.seekpick.utils.PrefsHelper;

import java.util.Hashtable;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    AutoCompleteTextView email, password;
    Button login;
    Login log;
    String sEmail, sPassword;
    ImageView loginUp;
    Animation arrowShake;
    String device;
    GestureDetectorCompat gestureDetectorCompat;
    Dialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (AutoCompleteTextView) findViewById(R.id.login_email);
        password = (AutoCompleteTextView) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login_button);
        loginUp = (ImageView) findViewById(R.id.login_up);
        arrowShake = AnimationUtils.loadAnimation(this, R.anim.arrorw_shake);
        device= PrefsHelper.getPrefsHelper(LoginActivity.this).getPref(PrefsHelper.FCM_TOKEN);
        Log.e("Device in login :-- ",device);
        this.gestureDetectorCompat = new GestureDetectorCompat(this, this);

        loginUp.startAnimation(arrowShake);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    sEmail = email.getText().toString();
                    sPassword = password.getText().toString();
                    if (sEmail.length() > 0) {
                        if (sPassword.length() > 0) {
                            progressDialog = new Dialog(LoginActivity.this);
                            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            progressDialog.setContentView(R.layout.custom_dialog_progress);
                            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            login("https://seekpick.herokuapp.com/login");
                        } else {
                            password.requestFocus();
                            password.setError("Check Password");
                        }
                    } else {
                        email.requestFocus();
                        email.setError("Check Email");
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Network isnt avialable", Toast.LENGTH_LONG).show();
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
        this.finish();
        startActivity(i);
    }


    public void login(String uri) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        log = LoginJsonParser.parsefeed(response);
                        Log.e("Login", log.getSuccess());
                        if (progressDialog != null) {
                            progressDialog.cancel();
                            progressDialog.hide();
                        }
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
                if (progressDialog != null) {
                    progressDialog.cancel();
                    progressDialog.hide();
                }
                Toast.makeText(LoginActivity.this, "Error in login ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<>();
                params.put("username", sEmail);
                params.put("password", sPassword);
                params.put("device",device);
                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH) {
                return false;
            }
            // down to up swipe
            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//                Toast.makeText(SearchActivity.this,"UP",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.up_in, R.anim.fade_out);
            }
            // up to down swipe
            else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//                Toast.makeText(SearchActivity.this,"Down",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, SearchActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.down_in, R.anim.fade_out);
            }
        } catch (Exception ignored) {

        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}