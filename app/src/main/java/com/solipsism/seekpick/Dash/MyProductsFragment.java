package com.solipsism.seekpick.Dash;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProductsFragment extends Fragment {
    ListView listview;
    List<Product> dataList,tempList;
    Dialog progressDialog;
    EditText search;
    ProductsAdapter adapter;

    public MyProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_products, container, false);

        listview = (ListView) view.findViewById(R.id.List_view);
        search = (EditText) view.findViewById(R.id.search_product);
        if (isonline()) {
            Log.e("Requesting the  port", " ");
            progressDialog = new Dialog(getActivity());
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.custom_dialog_progress);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setCancelable(false);
            progressDialog.show();
            requestData("https://seekpick.herokuapp.com/item/myproducts");

        }
        else {
            Toast.makeText(getActivity(), "Network isnt available ", Toast.LENGTH_SHORT).show();
        }
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ss=s.toString();

                adapter.getFilter().filter(ss);

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("After string",s+"5");
            }

        });

        return view;
    }
    public   boolean isonline(){

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }
    public void requestData(String uri){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.length()>2) {
                            Log.e("response of items ",response);
                            dataList = ProductsJsonParser.parsefeed(response);
                            tempList=dataList;
                            if (progressDialog != null) {
                                progressDialog.cancel();
                                progressDialog.hide();
                            }
                            adapter = new ProductsAdapter(getActivity(), R.layout.modal_my_products, dataList);
                            listview.setAdapter(adapter);
                        }
                        else{
                            if (progressDialog != null) {
                                progressDialog.cancel();
                                progressDialog.hide();
                            }
                            ArrayList<String> arrayList = new ArrayList<>();
                            arrayList.add("No Products Found");
                            ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1 , arrayList);
                            listview.setAdapter(adapter);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null) {
                    progressDialog.cancel();
                    progressDialog.hide();
                }
                /*Toast.makeText(getActivity(), " Login Again ", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(i);*/
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
