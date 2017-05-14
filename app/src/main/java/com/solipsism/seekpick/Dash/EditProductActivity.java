package com.solipsism.seekpick.Dash;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity {

    EditText productName, productPrice, productTags;
    Spinner productAvailable;
    String mProductName, mProductPrice, mProductTags, mProductStatus;
    Button uploadProduct;
    Dialog progressDialog;

    Product obj;
    int val;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        productName = (EditText) findViewById(R.id.edit_product_name);
        productPrice = (EditText) findViewById(R.id.edit_product_price);
        productTags = (EditText) findViewById(R.id.edit_product_tags);
        productAvailable = (Spinner) findViewById(R.id.edit_product_available);
        uploadProduct = (Button) findViewById(R.id.edit_upload_button);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Available");
        arrayList.add("Not Available");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProductActivity.this, android.R.layout.simple_dropdown_item_1line, arrayList);
        productAvailable.setAdapter(adapter);


        val = getIntent().getIntExtra("value", 0);

        if (val == 3) {
            obj = (Product) getIntent().getSerializableExtra("product_object");
            productName.setText(obj.getProName());
            productPrice.setText(obj.getProPrice());
            productTags.setText(obj.getProTags());
            if (obj.getProStatus().equals("Available")) {
                productAvailable.setSelection(0);
            } else {
                productAvailable.setSelection(1);
            }
        }
        uploadProduct.setOnClickListener(new View.OnClickListener() {

                                             public void onClick(View view) {

                                                 mProductName = productName.getText().toString();
                                                 mProductPrice = productPrice.getText().toString();
                                                 mProductTags = productTags.getText().toString();
                                                 mProductStatus = productAvailable.getSelectedItem().toString();
                                                 if (isOnline()) {
                                                     if (mProductName.length() > 0) {
                                                         if (mProductPrice.length() > 0) {
                                                             if (mProductTags.length() > 0) {
                                                                 Uri.Builder builder = new Uri.Builder();
                                                                 builder.scheme("https")
                                                                         .authority("seekpick.herokuapp.com")
                                                                         .appendPath("item")
                                                                         .appendPath("edit")
                                                                         .appendQueryParameter("id", obj.get_id());
                                                                 String urlQuery = builder.build().toString();
                                                                 progressDialog = new Dialog(EditProductActivity.this);
                                                                 progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                 progressDialog.setContentView(R.layout.custom_dialog_progress);
                                                                 progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                                 progressDialog.setCancelable(false);
                                                                 progressDialog.show();
                                                                 requestEdit(urlQuery);
                                                             } else {
                                                                 productTags.requestFocus();
                                                                 productTags.setError("please enter at least one tag");
                                                             }
                                                         } else {
                                                             productPrice.requestFocus();
                                                             productPrice.setError("please enter product price");
                                                         }
                                                     } else {
                                                         productName.requestFocus();
                                                         productName.setError("please enter product name");
                                                     }
                                                 }
                                             }
                                         }
        );
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void requestEdit(String uri) {
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
                        }
                        if (success.equals("true")) {
                            Toast.makeText(EditProductActivity.this, message, Toast.LENGTH_SHORT).show();
                            if (progressDialog != null) {
                                progressDialog.cancel();
                                progressDialog.hide();
                            }
                            afterResponse();

                        } else {
                            if (progressDialog != null) {
                                progressDialog.cancel();
                                progressDialog.hide();
                            }
                            Toast.makeText(EditProductActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProductActivity.this, "Login again", Toast.LENGTH_SHORT).show();
                if (progressDialog != null) {
                    progressDialog.cancel();
                    progressDialog.hide();
                }
                Intent i = new Intent(EditProductActivity.this, LoginActivity.class);
                EditProductActivity.this.finish();
                startActivity(i);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("itemname", mProductName);
                params.put("itemprice", mProductPrice);
                params.put("tags", mProductTags);
                params.put("status", mProductStatus);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", (String) PrefsHelper.getPrefsHelper(EditProductActivity.this).getPref(PrefsHelper.PREF_TOKEN));
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditProductActivity.this);
        requestQueue.add(stringRequest);
    }

    public void afterResponse() {
        Intent i = new Intent(EditProductActivity.this, DashActivity.class);
        this.finish();
        startActivity(i);
        /*
        MyProductsFragment myProductsFragment = new MyProductsFragment();
        ((DashActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.content, myProductsFragment).commit();*/
    }

}