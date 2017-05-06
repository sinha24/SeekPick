package com.solipsism.seekpick.Search;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.solipsism.seekpick.R;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }

    List<ListItem> datalist;
    EditText searchView;
    Button searchButton;
    ImageButton range1, range2, range3;
    String searchText = "", sLat = "", sLong = "", range = "5";
    float zoom = (float) 13.0;
    GPSTracker gps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs);
        final int backgroundResource = typedArray.getResourceId(0, 0);

        searchView = (EditText) rootView.findViewById(R.id.search_text);
        searchButton = (Button) rootView.findViewById(R.id.search_btn);
        range1 = (ImageButton) rootView.findViewById(R.id.range1);
        range2 = (ImageButton) rootView.findViewById(R.id.range2);
        range3 = (ImageButton) rootView.findViewById(R.id.range3);
        searchView.requestFocus();


        range1.setBackground(getResources().getDrawable(R.drawable.border, null));
        range2.setBackgroundResource(backgroundResource);
        range3.setBackgroundResource(backgroundResource);

        range1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                range = "5";
                range1.setBackground(getResources().getDrawable(R.drawable.border, null));
                range2.setBackgroundResource(backgroundResource);
                range3.setBackgroundResource(backgroundResource);
                zoom = (float) 13.0;
            }
        });
        range2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                range = "25";
                range2.setBackground(getResources().getDrawable(R.drawable.border, null));
                range1.setBackgroundResource(backgroundResource);
                range3.setBackgroundResource(backgroundResource);
                zoom = (float) 11.0;
            }
        });
        range3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                range = "50";
                range3.setBackground(getResources().getDrawable(R.drawable.border, null));
                range2.setBackgroundResource(backgroundResource);
                range1.setBackgroundResource(backgroundResource);
                zoom = (float) 9.0;
            }
        });
        //GPSTracker Service
        gps = new GPSTracker(getActivity()) {
            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }
        };

        // check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            sLat = String.valueOf(latitude);
            sLong = String.valueOf(longitude);
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Location location = gps.getLocation();
                    sLat = String.valueOf(location.getLatitude());
                    sLong = String.valueOf(location.getLongitude());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                searchText = String.valueOf(searchView.getText());
                Log.e("search", "click " + searchText);
                if (searchText.length() > 0) {
                    if (isOnline()) {
                        Uri.Builder builder = new Uri.Builder();
                        builder.scheme("https")
                                .authority("seekpick.herokuapp.com")
                                .appendPath("search")
                                .appendQueryParameter("id", searchText);
                        String urlQuery = builder.build().toString();
                        Log.e("url ", urlQuery);
                        search(urlQuery);
                    }
                } else {
                    searchView.requestFocus();
                    searchView.setError("Search something..");
                }
            }
        });
        return rootView;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void search(String uri) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent i = new Intent(getActivity(), MapsActivity.class);
                        datalist = SearchJsonParser.parsefeed(response);
                        i.putExtra("response", response);
                        i.putExtra("zoom", zoom);
                        i.putExtra("itemList", (Serializable) datalist);
                        gps.stopUsingGPS();
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Search error", String.valueOf(error));
                Toast.makeText(getActivity(), "Cannot find your location", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<>();
                Log.e("Params", sLat + " " + sLong);
                params.put("lat", sLat);
                params.put("long", sLong);
                params.put("range", range);

                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
}