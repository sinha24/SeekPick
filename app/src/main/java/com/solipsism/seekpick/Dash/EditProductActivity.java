package com.solipsism.seekpick.Dash;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.solipsism.seekpick.R;
import com.solipsism.seekpick.utils.PrefsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity {

    EditText productName, productPrice, productTags;
    String mProductName, mProductPrice, mProductTags;
    Button uploadProduct;

    Product obj;
    int val;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        productName = (EditText) findViewById(R.id.edit_product_name);
        productPrice = (EditText) findViewById(R.id.edit_product_price);
        productTags = (EditText) findViewById(R.id.edit_product_tags);
        uploadProduct = (Button) findViewById(R.id.edit_upload_button);

        val = getIntent().getIntExtra("value", 0);

        if (val == 3) {
            obj = (Product) getIntent().getSerializableExtra("product_object");
            productName.setText(obj.getProName());
            productPrice.setText(obj.getProPrice());
            productTags.setText(obj.getProTags());
        }


        uploadProduct.setOnClickListener(new View.OnClickListener() {

                                             public void onClick(View view) {

                                                 mProductName = productName.getText().toString();
                                                 mProductPrice = productPrice.getText().toString();
                                                 mProductTags = productTags.getText().toString();

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
                            afterResponse();

                        } else {
                            Toast.makeText(EditProductActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("itemname", mProductName);
                params.put("itemprice", mProductPrice);
                params.put("tags", mProductTags);
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
        Intent intent = new Intent(EditProductActivity.this, DashActivity.class);
        EditProductActivity.this.finish();
        startActivity(intent);
    }

}