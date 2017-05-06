package com.solipsism.seekpick.Dash;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.solipsism.seekpick.Login.LoginActivity;
import com.solipsism.seekpick.R;
import com.solipsism.seekpick.utils.PrefsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends Fragment {

    public AddProductFragment() {
        // Required empty public constructor
    }

    final String ADD_URL = "https://seekpick.herokuapp.com/item/add";
    String itemName, itemPrice, itemTags;
    EditText mItemName, mItemPrice, mTags;
    Button mUpload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_product, container, false);

        mItemName = (EditText) rootView.findViewById(R.id.item_name);
        mItemPrice = (EditText) rootView.findViewById(R.id.item_price);
        mTags = (EditText) rootView.findViewById(R.id.tags);
        mUpload = (Button) rootView.findViewById(R.id.upload);

        mUpload.setOnClickListener(new View.OnClickListener() {

                                       public void onClick(View view) {
                                           itemName = mItemName.getText().toString();
                                           itemPrice = mItemPrice.getText().toString();
                                           itemTags = mTags.getText().toString();

                                           if (isOnline()) {
                                               if (itemName.length()>0) {
                                                   if (itemPrice.length()>0) {
                                                       if (itemTags.length()>0) {
                                                           try {
                                                               UploadAdd();
                                                           } catch (JSONException e) {
                                                               e.printStackTrace();
                                                           }
                                                       } else {
                                                           mTags.requestFocus();
                                                           mTags.setError("please enter at least one tag");
                                                       }
                                                   } else {
                                                       mItemPrice.requestFocus();
                                                       mItemPrice.setError("please enter product price");
                                                   }
                                               } else {
                                                   mItemName.requestFocus();
                                                   mItemName.setError("please enter product name");
                                               }
                                           }
                                       }
                                   }
        );

        return rootView;
    }



    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    private void UploadAdd() throws JSONException {
        itemName = mItemName.getText().toString().trim();
        itemPrice = mItemPrice.getText().toString().trim();
        itemTags = mTags.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_URL,
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
                        }
                        if (success.equals("true")) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            AfterResponse();
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Login again", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        getActivity().finish();
                        startActivity(i);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("itemname", itemName);
                params.put("itemprice", itemPrice);
                params.put("tags", itemTags);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", (String) PrefsHelper.getPrefsHelper(getActivity()).getPref(PrefsHelper.PREF_TOKEN));
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public void AfterResponse() {
        mItemName.setText("");
        mItemPrice.setText("");
        mTags.setText("");
    }
}