package com.solipsism.seekpick.Dash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.solipsism.seekpick.Search.SearchActivity;
import com.solipsism.seekpick.Search.SearchFragment;
import com.solipsism.seekpick.utils.PrefsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DashActivity extends AppCompatActivity {
    AddProductFragment addProductFragment;
    SearchFragment searchFragment;
    MyProductsFragment myProductsFragment;
    MyProfileFragment myProfileFragment;
    String ans, name;
    AlertDialog alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        addProductFragment = new AddProductFragment();
        searchFragment = new SearchFragment();
        myProductsFragment = new MyProductsFragment();
        myProfileFragment = new MyProfileFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, searchFragment).commit();

        BottomNavigationView navigation =
                (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleBottomNavigationItemSelected(item);
                return true;
            }

            private void handleBottomNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_search:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, searchFragment).commit();
                        break;
                    case R.id.navigation_add_product:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, addProductFragment).commit();
                        break;
                    case R.id.navigation_my_product:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, myProductsFragment).commit();
                        break;
                    case R.id.navigation_my_profile:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, myProfileFragment).commit();
                        break;
                }
            }
        });

        Intent intent = getIntent();
        int check = intent.getIntExtra("flag", 0);
        Log.e("Check value ", check + "");
        if (check == 7) {
            String title = intent.getStringExtra("tt");
            name = intent.getStringExtra("nm");
            String message = intent.getStringExtra("mssg");
            AlertDialog.Builder builder = new AlertDialog.Builder(DashActivity.this);
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ans = "Available";
                            dialogInterface.cancel();
                            statusupdate("https://seekpick.herokuapp.com/item/status");
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ans = "Not Available";
                            dialogInterface.cancel();
                            statusupdate("https://seekpick.herokuapp.com/item/status");
                        }
                    });
            alert = builder.create();
            alert.setTitle("Customer Enquiry");
            alert.show();
            Log.e("Build success", alert + "");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // show dash_menu when dash_menu button is pressed
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dash_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            PrefsHelper.getPrefsHelper(DashActivity.this).savePref(PrefsHelper.PREF_TOKEN, "token");
            Intent i = new Intent(DashActivity.this, LoginActivity.class);
            this.finish();
            startActivity(i);
        }
        return true;
    }

    boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            System.gc();
            System.exit(0);
        } else {
            Toast.makeText(this, "Press Back again to Exit",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (alert != null)
            alert.dismiss();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alert != null && alert.isShowing()) {
            alert.cancel();
        }
    }

    public void statusupdate(String uri) {
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
                            Toast.makeText(DashActivity.this, message, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DashActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error:--- ",error.getMessage());
                        Toast.makeText(DashActivity.this, "Login again", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(DashActivity.this, LoginActivity.class);
                        finish();
                        startActivity(i);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("itemname", name);
                params.put("ans", ans);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", (String) PrefsHelper.getPrefsHelper(DashActivity.this).getPref(PrefsHelper.PREF_TOKEN));
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(DashActivity.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);


    }
}