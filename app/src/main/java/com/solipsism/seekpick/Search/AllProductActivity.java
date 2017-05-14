package com.solipsism.seekpick.Search;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.solipsism.seekpick.R;

import java.util.ArrayList;
import java.util.List;

public class AllProductActivity extends AppCompatActivity {

    ListView listView;
    String shopId;
    List<Product> dataList;
    ProductsAdapter adapter;
    Dialog progressDialog;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        shopId = intent.getStringExtra("shopId");

        listView = (ListView) findViewById(R.id.result_list);
        search = (EditText) findViewById(R.id.search_result_product);
        if (isOnline()) {
            progressDialog = new Dialog(AllProductActivity.this);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.custom_dialog_progress);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setCancelable(false);
            progressDialog.show();
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("seekpick.herokuapp.com")
                    .appendPath("search")
                    .appendPath("allproduct")
                    .appendQueryParameter("id",shopId);
            String urlQuery = builder.build().toString();
            requestData(urlQuery);
        } else {
            Toast.makeText(AllProductActivity.this, "Network isnt available ", Toast.LENGTH_SHORT).show();
            if (progressDialog != null) {
                progressDialog.cancel();
                progressDialog.hide();
            }
            AllProductActivity.this.finish();
        }
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void requestData(String uri){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.length()>2) {
                            dataList = ProductsJsonParser.parseFeed(response);
                            if (progressDialog != null) {
                                progressDialog.cancel();
                                progressDialog.hide();
                            }
                            adapter = new ProductsAdapter(AllProductActivity.this, R.layout.modal_my_products, dataList);
                            listView.setAdapter(adapter);
                        }
                        else{
                            if (progressDialog != null) {
                                progressDialog.cancel();
                                progressDialog.hide();
                            }
                            ArrayList<String> arrayList = new ArrayList<>();
                            arrayList.add("No Products Found");
                            ArrayAdapter adapter = new ArrayAdapter(AllProductActivity.this,android.R.layout.simple_list_item_1 , arrayList);
                            listView.setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null) {
                    progressDialog.cancel();
                    progressDialog.hide();
                }
                AllProductActivity.this.finish();
            }
        }){

        };
        RequestQueue requestQueue = Volley.newRequestQueue(AllProductActivity.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
