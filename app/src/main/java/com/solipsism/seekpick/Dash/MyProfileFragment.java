package com.solipsism.seekpick.Dash;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.solipsism.seekpick.Login.LoginActivity;
import com.solipsism.seekpick.Login.SignUpActivity;
import com.solipsism.seekpick.R;
import com.solipsism.seekpick.SearchActivity;
import com.solipsism.seekpick.utils.PrefsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {
    SearchFragment msearchFragment;
    TextView myProfileLogo;
    AutoCompleteTextView email, name, address, pinCode, phone, username, password, cPassword;
    Button saveChanges;
    ImageButton location;
    UserDetails userDetails;
    String sEmail, sName, sAddress, sPinCode, sPhone, sUsername, sPassword, sCPassword, sLocation;


    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        myProfileLogo = (TextView) view.findViewById(R.id.myprofilelogo);
        email = (AutoCompleteTextView) view.findViewById(R.id.my_profile_email);
        name = (AutoCompleteTextView) view.findViewById(R.id.my_profile_shopname);
        address = (AutoCompleteTextView) view.findViewById(R.id.my_profile_shopaddress);
        pinCode = (AutoCompleteTextView) view.findViewById(R.id.my_profile_pincode);
        phone = (AutoCompleteTextView) view.findViewById(R.id.my_profile_phone);
        username = (AutoCompleteTextView) view.findViewById(R.id.my_profile_username);
        password = (AutoCompleteTextView) view.findViewById(R.id.my_profile_password);
        cPassword = (AutoCompleteTextView) view.findViewById(R.id.my_profile_cpassword);
        saveChanges = (Button) view.findViewById(R.id.my_profile_button);
        location = (ImageButton) view.findViewById(R.id.my_profile_location);
        msearchFragment = new SearchFragment();

        Typeface custom_font = Typeface.createFromAsset( getActivity().getAssets() , "alex.ttf");
        myProfileLogo.setTypeface(custom_font);



        // Inflate the layout for this fragment

        if (isOnline()) {
            requestUser("https://seekpick.herokuapp.com/getuser");
        } else {
            Toast.makeText(getActivity(), "Network isnt available ", Toast.LENGTH_SHORT).show();
        }
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                                    requestData("https://seekpick.herokuapp.com/edit");
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
            }

        });

        return view;
    }

    private void onSavedChanges() {
    getFragmentManager().beginTransaction().replace(R.id.content, msearchFragment).commit();
 }

    public void requestData(String uri){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String success="";
                        String message="";
                        try {
                            JSONObject object= new JSONObject(response);
                            success=object.getString("success");
                            message=object.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(success.equals("true")){
                            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                            onSavedChanges();
                        }
                        else{
                            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Login Again", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new Hashtable<String, String>();
                params.put("email", sEmail);
                params.put("password",sPassword);
                params.put("name",sName);
                params.put("address",sAddress);
                params.put("phone",sPhone);
                params.put("pincode", sPinCode);
                params.put("username", sUsername);
                params.put("cpassword", sCPassword);
                params.put("location", sLocation);
                //returning parameters
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization",(String) PrefsHelper.getPrefsHelper(getActivity()).getPref(PrefsHelper.PREF_TOKEN));
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public void requestUser(String uri)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
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
                        password.setText(userDetails.getPassword());
                        cPassword.setText(userDetails.getPassword());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Login Again ", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization",(String) PrefsHelper.getPrefsHelper(getActivity()).getPref(PrefsHelper.PREF_TOKEN));
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);



    }
    public  boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo!=null && netInfo.isConnectedOrConnecting()){
            return true;
        }
        else
        {
            return false;
        }


    }
}