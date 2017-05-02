package com.solipsism.seekpick.Dash;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by sakshi on 4/30/2017.
 */

public class ProductsAdapter extends ArrayAdapter <Product> {
    private Context context;
    private List<Product> dataList;
    Product product, product2;
    int pos;
    public ListView listview;
    MyProductsFragment myProductsFragment;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public ProductsAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.dataList = objects;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.products_form, parent, false);

        MyViewHolder viewHolder = new MyViewHolder();
        product = dataList.get(position);
        listview = (ListView) view.findViewById(R.id.List_view);
        viewHolder.mProName = (TextView) view.findViewById(R.id.name1);
        viewHolder.mProprice = (TextView) view.findViewById(R.id.price1);
        viewHolder.delete = (Button) view.findViewById(R.id.delete_product);
        viewHolder.mProTags = (TextView) view.findViewById(R.id.tag1);
        viewHolder.edit = (Button) view.findViewById(R.id.edit_product);

        viewHolder.mProName.setText(product.getProName());
        viewHolder.mProprice.setText(product.getProPrice());
        viewHolder.mProTags.setText(product.getProTags());

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isonline()){
                    pos = position;
                    product2 = dataList.get(pos);
                    Intent intent =new Intent(context,DashActivity.class);
                    //intent.putExtra("obj", (Parcelable) product2);
                    //intent.putExtra("value",8);
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, "Network isnt available ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isonline()) {
                    Log.e("Requesting the  port", " ");
                    pos = position;
                    product2 = dataList.get(pos);
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https")
                            .authority("seekpick.herokuapp.com")
                            .appendPath("item")
                            .appendPath("delete")
                            .appendQueryParameter("id",product2.get_id());
                    String urlQuery = builder.build().toString();
                    Log.e("url for delete:-- ", urlQuery);
                    requestDelete(urlQuery);
                } else {
                    Toast.makeText(context, "Network isnt available ", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }

    static class MyViewHolder {

        private TextView mProName;
        private TextView mProTags;
        private TextView mProprice;
        private Button edit, delete;

    }
    public void requestDelete(String uri) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String success = "";
                        String message = "";
                        Log.e("After delete:-- ",response);
                        try {
                            JSONObject obje = new JSONObject(response);
                            success = obje.getString("success");
                            message = obje.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (success.equals("true")) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            afterres();

                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error on delete:-- ,",error+"");
               /* Toast.makeText(context,"Login again ", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, LoginActivity.class);
                context.startActivity(i);*/

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", (String) PrefsHelper.getPrefsHelper(context).getPref(PrefsHelper.PREF_TOKEN));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding request to the queue
        requestQueue.add(stringRequest);


    }

    public boolean isonline() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }

    }
    public void afterres() {
        /*Intent i = new Intent(context, DashActivity.class);
        context.startActivity(i);*/
        myProductsFragment =new MyProductsFragment();
        ((DashActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.content, myProductsFragment).commit();

    }

}


