package com.solipsism.seekpick.Dash;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.solipsism.seekpick.Login.LoginActivity;
import com.solipsism.seekpick.R;
import com.solipsism.seekpick.utils.PrefsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {


    public MyProfileFragment() {
        // Required empty public constructor
    }

    ProgressDialog progressDialog;
    AutoCompleteTextView email, name, address, pinCode, phone, username, password, cPassword;
    Button saveChanges;
    ImageButton location;
    UserDetails userDetails;
    String sEmail, sName, sAddress, sPinCode, sPhone, sUsername, sPassword, sCPassword, sLocation, sLat, sLong;
    int PLACE_PICKER_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        email = (AutoCompleteTextView) rootView.findViewById(R.id.my_profile_email);
        name = (AutoCompleteTextView) rootView.findViewById(R.id.my_profile_shopname);
        address = (AutoCompleteTextView) rootView.findViewById(R.id.my_profile_shopaddress);
        pinCode = (AutoCompleteTextView) rootView.findViewById(R.id.my_profile_pincode);
        phone = (AutoCompleteTextView) rootView.findViewById(R.id.my_profile_phone);
        username = (AutoCompleteTextView) rootView.findViewById(R.id.my_profile_username);
        password = (AutoCompleteTextView) rootView.findViewById(R.id.my_profile_password);
        cPassword = (AutoCompleteTextView) rootView.findViewById(R.id.my_profile_cpassword);
        saveChanges = (Button) rootView.findViewById(R.id.my_profile_button);
        location = (ImageButton) rootView.findViewById(R.id.my_profile_location);

        if (isOnline()) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Fetching Details...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            getUser("https://seekpick.herokuapp.com/getuser");
        } else {
            Toast.makeText(getActivity(), "Network isnt available ", Toast.LENGTH_SHORT).show();
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLACE_PICKER_REQUEST = 1;
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                                        progressDialog = new ProgressDialog(getActivity());
                                                        progressDialog.setTitle("Saving details..");
                                                        progressDialog.setCancelable(false);
                                                        progressDialog.show();
                                                        editProfile("https://seekpick.herokuapp.com/edit");
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
                                        Toast.makeText(getActivity(), "Please add Location", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getActivity(), "Network isn't available", Toast.LENGTH_LONG).show();
                }
            }

        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getContext(), data);
                LatLng latLng = place.getLatLng();
                sLat = String.valueOf(latLng.latitude);
                sLong = String.valueOf(latLng.longitude);
                sLocation = String.format("%s", place.getAddress());
                sAddress = sLocation;
                address.setText(sAddress);
                Toast.makeText(getContext(), sLocation, Toast.LENGTH_LONG).show();
                location.setBackgroundColor(Color.parseColor("#00aa00"));
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void getUser(String uri) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        userDetails = UserDetailsJsonParser.parsefeed(response);
                        name.setText(userDetails.getName());
                        email.setText(userDetails.getEmail());
                        address.setText(userDetails.getLocation());
                        pinCode.setText(userDetails.getPincode());
                        phone.setText(userDetails.getPhone());
                        username.setText(userDetails.getUsername());
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Login Again ", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", (String) PrefsHelper.getPrefsHelper(getActivity()).getPref(PrefsHelper.PREF_TOKEN));
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void onSavedChanges() {
        progressDialog.dismiss();
    }

    public void editProfile(String uri) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String success = "";
                        String message = "";
                        try {
                            JSONObject object = new JSONObject(response);
                            success = object.getString("success");
                            message = object.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        if (success.equals("true")) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            onSavedChanges();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Login Again", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();
                params.put("email", sEmail);
                params.put("password", sPassword);
                params.put("name", sName);
                params.put("phone", sPhone);
                params.put("pincode", sPinCode);
                params.put("username", sUsername);
                params.put("location", sAddress);
                params.put("lat", sLat);
                params.put("long", sLong);
                //returning parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", (String) PrefsHelper.getPrefsHelper(getActivity()).getPref(PrefsHelper.PREF_TOKEN));
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}