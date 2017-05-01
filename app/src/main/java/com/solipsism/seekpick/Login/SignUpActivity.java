package com.solipsism.seekpick.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.solipsism.seekpick.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    TextView signupLogo;
    AutoCompleteTextView email, name, address, pinCode, phone, username, password, cPassword;
    Button signUp;
    ImageButton location;
    String sEmail, sName, sAddress, sPinCode, sPhone, sUsername, sPassword, sCPassword, sLocation, sLat, sLong;
    ProgressDialog progressDialog;
    int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    signupLogo= (TextView) findViewById(R.id.signuplogo);
        email = (AutoCompleteTextView) findViewById(R.id.signup_email);
        name = (AutoCompleteTextView) findViewById(R.id.signup_name);
        address = (AutoCompleteTextView) findViewById(R.id.signup_address);
        pinCode = (AutoCompleteTextView) findViewById(R.id.signup_pincode);
        phone = (AutoCompleteTextView) findViewById(R.id.signup_phone);
        username = (AutoCompleteTextView) findViewById(R.id.signup_username);
        password = (AutoCompleteTextView) findViewById(R.id.signup_password);
        cPassword = (AutoCompleteTextView) findViewById(R.id.signup_cpassword);

        signUp = (Button) findViewById(R.id.signup_button);
        location = (ImageButton) findViewById(R.id.signup_location);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "alex.ttf");
        signupLogo.setTypeface(custom_font);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PLACE_PICKER_REQUEST = 1;
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(SignUpActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline()) {
                    sEmail = email.getText().toString();
                    sName = name.getText().toString();
                    sAddress = address.getText().toString();
                    sPinCode = pinCode.getText().toString();
                    sPhone = phone.getText().toString();
                    sUsername = username.getText().toString();
                    sPassword = password.getText().toString();
                    sCPassword = cPassword.getText().toString();

                    if (sEmail.length() > 0 && sEmail.contains("@")) {
                        if (sName.length() > 0) {
                            if (sAddress.length() > 0) {
                                if (sPinCode.length() == 6) {
                                    if (sLocation.length() > 0) {
                                        if (sPhone.length() == 10) {
                                            if (sUsername.length() > 0) {
                                                if (sPassword.length() > 5) {
                                                    if (sCPassword.equals(sPassword)) {
                                                        progressDialog = new ProgressDialog(SignUpActivity.this);
                                                        progressDialog.setTitle("Registering..");
                                                        progressDialog.setCancelable(false);
                                                        progressDialog.show();
                                                        SignUp("https://seekpick.herokuapp.com/register");
                                                    } else {
                                                        cPassword.requestFocus();
                                                        cPassword.setError("Passwords differ");
                                                    }
                                                } else {
                                                    password.requestFocus();
                                                    password.setError("Password should be greater than 6 letters");
                                                }
                                            } else {
                                                username.requestFocus();
                                                username.setError("Please enter Username");
                                            }
                                        } else {
                                            phone.requestFocus();
                                            phone.setError("Please enter Phone number");
                                        }
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Please add Location", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    pinCode.requestFocus();
                                    pinCode.setError("Please enter valid PINCODE");
                                }
                            } else {
                                address.requestFocus();
                                address.setError("Please enter Address");
                            }
                        } else {
                            name.requestFocus();
                            name.setError("Please enter Name");
                        }
                    } else {
                        email.requestFocus();
                        email.setError("Please enter email id");
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Network isn't available", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                LatLng latLng = place.getLatLng();
                sLat = String.valueOf(latLng.latitude);
                sLong = String.valueOf(latLng.longitude);
                sLocation = String.format("%s", place.getAddress());
                sAddress = sLocation;
                address.setText(sAddress);
                Toast.makeText(this, sLocation, Toast.LENGTH_LONG).show();
                location.setBackgroundColor(Color.parseColor("#00aa00"));
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void onGettingResponse() {
        progressDialog.dismiss();
        Toast.makeText(SignUpActivity.this, " login to continue ", Toast.LENGTH_LONG).show();
        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void SignUp(String uri) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String success = "";
                        String message = "";
                        try {
                            JSONObject obje = new JSONObject(response);
                            success = obje.getString("success");
                            message = obje.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        if (success.equals("true")) {
                            onGettingResponse();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, "Error in sign up ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<>();
                params.put("name", sName);
                params.put("email", sEmail);
                params.put("phone", sPhone);
                params.put("pincode", sPinCode);
                params.put("location", sAddress);
                params.put("username", sUsername);
                params.put("password", sPassword);
                params.put("lat", sLat);
                params.put("long", sLong);


                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);


    }
}
