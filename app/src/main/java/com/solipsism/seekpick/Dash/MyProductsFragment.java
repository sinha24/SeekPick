package com.solipsism.seekpick.Dash;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProductsFragment extends Fragment {
    ListView listview;
    List<Product> dataList;
    ProgressDialog progress;


    public MyProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_products, container, false);

        listview = (ListView) view.findViewById(R.id.List_view);
        if (isonline()) {
            Log.e("Requesting the  port", " ");
            progress = new ProgressDialog(getActivity());
            progress.setTitle("Loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            requestData("https://seekpick.herokuapp.com/item/myproducts");

        }
        else {
            Toast.makeText(getActivity(), "Network isnt available ", Toast.LENGTH_SHORT).show();
        }


        return view;
    }
    public   boolean isonline(){

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
    public void requestData(String uri){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.length()>2) {
                            Log.e("response of items ",response);
                            dataList = ProductsJsonParser.parsefeed(response);
                            progress.dismiss();
                            ProductsAdapter adapter = new ProductsAdapter(getActivity(), R.layout.products_form, dataList);
                            listview.setAdapter(adapter);
                        }
                        else{
                            progress.dismiss();
                            Toast.makeText(getActivity(),"No product found",Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Toast.makeText(getActivity(), " Login Again ", Toast.LENGTH_LONG).show();
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

}
