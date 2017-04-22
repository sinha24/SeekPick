package com.solipsism.seekpick.Dash;


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

import static com.solipsism.seekpick.R.id.container;
import static com.solipsism.seekpick.R.id.itemname;
import static com.solipsism.seekpick.R.id.itemprice;
import static com.solipsism.seekpick.R.id.tags;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends Fragment {
    public static final String ADD_URL = "https://seekpick.herokuapp.com/item/add";
    public static String KEY_ITEMNAME = "itemname";
    public static String KEY_ITEMPRICE = "itemprice";
    public static String KEY_TAGS = "tags";
String itemname2,itemprice2,tags2;
    private EditText mItemName;
    private EditText mItemPrice;
    private EditText mTags;
    private Button mUpload;
    private Fragment mAddFragment;

    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        mItemName = (EditText) view.findViewById(itemname);
        mItemPrice = (EditText) view.findViewById(R.id.itemprice);
        mTags = (EditText) view.findViewById(R.id.tags);
        mAddFragment = new Fragment();
        mUpload = (Button) view.findViewById(R.id.upload);

        mUpload.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                 itemname2=mItemName.getText().toString().trim();
                itemprice2=mItemPrice.getText().toString();
                tags2=mTags.getText().toString();

                if (isonline()) {

                    if (itemname > 0) {
                        if (itemprice > 0) {
                            if (tags > 0) {
                            } else {
                                mTags.requestFocus();
                                mTags.setError("please enter one word description");
                            }
                        } else {
                            mItemPrice.requestFocus();
                            mItemPrice.setError("please enter product proce");
                        }
                    } else {
                        mItemName.requestFocus();
                        mItemName.setError("please enter product name");
                    }

                }

                try {
                    UploadAdd();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // mUpload.setOnClickListener(this);
                Toast.makeText(getActivity(),"Uploaded",Toast.LENGTH_LONG).show();
            }
        }

    );


    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_add,container,false);

}


    private void UploadAdd() throws JSONException {
         itemname2 = mItemName.getText().toString().trim();
         itemprice2 = mItemPrice.getText().toString().trim();
         tags2 = mTags.getText().toString().trim();


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
                        startActivity(i);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("itemname", KEY_ITEMNAME);
                params.put("itemprice", KEY_ITEMPRICE);
                params.put("tags", KEY_TAGS);
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
        Log.e("Request added for add", " ");
        //Adding request to the queue
        requestQueue.add(stringRequest);

    }


    public boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }


    }


    public void AfterResponse() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mAddFragment)
                .commit();
    }


}
