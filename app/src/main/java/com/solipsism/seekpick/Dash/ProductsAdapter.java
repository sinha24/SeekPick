package com.solipsism.seekpick.Dash;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.solipsism.seekpick.R;
import com.solipsism.seekpick.utils.PrefsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


class ProductsAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> dataList, allProduct;
    private Product product2;
    private int pos;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    ProductsAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.dataList = objects;
        this.allProduct = objects;
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.modal_my_products, parent, false);

        MyViewHolder viewHolder = new MyViewHolder();
        Product product = dataList.get(position);
        viewHolder.mProName = (TextView) view.findViewById(R.id.name1);
        viewHolder.mProprice = (TextView) view.findViewById(R.id.price1);
        viewHolder.delete = (Button) view.findViewById(R.id.delete_product);
        viewHolder.mProTags = (TextView) view.findViewById(R.id.tag1);
        viewHolder.mProStatus = (TextView) view.findViewById(R.id.status1);
        viewHolder.edit = (Button) view.findViewById(R.id.edit_product);
        viewHolder.mProName.setText(product.getProName());
        viewHolder.mProprice.setText(product.getProPrice());
        viewHolder.mProTags.setText(product.getProTags());
        viewHolder.mProStatus.setText(product.getProStatus() + " " + product.getProUpdate());

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isonline()) {
                    pos = position;
                    product2 = dataList.get(pos);
                    Intent intent = new Intent(context, EditProductActivity.class);
                    intent.putExtra("product_object", product2);
                    intent.putExtra("value", 3);
                    ((Activity) context).finish();
                    context.startActivity(intent);
                } else {
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
                            .appendQueryParameter("id", product2.get_id());
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

    private static class MyViewHolder {

        private TextView mProName;
        private TextView mProTags;
        private TextView mProprice;
        private TextView mProStatus;
        private Button edit, delete;

    }

    private void requestDelete(String uri) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String success = "";
                        String message = "";
                        Log.e("After delete:-- ", response);
                        try {
                            JSONObject obje = new JSONObject(response);
                            success = obje.getString("success");
                            message = obje.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (success.equals("true")) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            afterResponse();

                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error on delete:-- ,", error + "");
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

    private boolean isonline() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    private void afterResponse() {
        MyProductsFragment myProductsFragment = new MyProductsFragment();
        ((DashActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.content, myProductsFragment).commit();
    }
}


