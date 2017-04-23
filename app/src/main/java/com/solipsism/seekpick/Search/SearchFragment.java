package com.solipsism.seekpick.Search;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.solipsism.seekpick.Dash.DashActivity;
import com.solipsism.seekpick.Login.LoginActivity;
import com.solipsism.seekpick.R;

import java.util.Hashtable;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }

    EditText searchView;
    Button searchButton;
    String searchText = "", sLat="", sLong="";

    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        Log.e("search ", "start");
        searchView = (EditText) rootView.findViewById(R.id.search_text);
        searchButton = (Button) rootView.findViewById(R.id.search_btn);
        searchView.requestFocus();

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("search ", "check");
            return rootView;
        }
        Log.e("search ", "checked");

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                sLat = String.valueOf(location.getLatitude());
                sLong = String.valueOf(location.getLongitude());
                Log.e("Location ", sLat+ " "+ sLong);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
        Log.e("search ", "gpsd");

        if (sLat.isEmpty() && sLong.isEmpty()) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    sLat = String.valueOf(location.getLatitude());
                    sLong = String.valueOf(location.getLongitude());
                    Log.e("Location ", sLat+ " "+ sLong);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
        Log.e("search ", "NWd");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = String.valueOf(searchView.getText());
                Log.e("search","click "+searchText);
                if (searchText.length() > 0) {
                    if (isOnline()) {
                        Uri.Builder builder = new Uri.Builder();
                        builder.scheme("https")
                                .authority("seekpick.herokuapp.com")
                                .appendPath("search")
                                .appendQueryParameter("id", searchText);
                        String urlQuery = builder.build().toString();
                        Log.e("url ", urlQuery);
                        Intent i = new Intent(getActivity(), SearchResultActivity.class);
                        i.putExtra("response","");
                        startActivity(i);
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
                        Intent i = new Intent(getActivity(), SearchResultActivity.class);
                        i.putExtra("response",response);
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Search error", String.valueOf(error));
                Toast.makeText(getActivity(), "Error in search ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<>();
                params.put("lat", sLat);
                params.put("long", sLong);
                params.put("range", "50");

                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
}
