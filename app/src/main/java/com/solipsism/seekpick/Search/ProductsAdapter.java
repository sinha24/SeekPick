package com.solipsism.seekpick.Search;

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
import android.widget.Filter;
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
import com.solipsism.seekpick.Dash.DashActivity;
import com.solipsism.seekpick.Dash.EditProductActivity;
import com.solipsism.seekpick.Dash.MyProductsFragment;
import com.solipsism.seekpick.R;
import com.solipsism.seekpick.utils.PrefsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class ProductsAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> dataList,allProduct;
    private ProductFilter filter;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    ProductsAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.dataList = objects;
        this.allProduct=new ArrayList<Product>();
        this.allProduct.addAll(objects);
    }
    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new ProductFilter();
        }
        return filter;
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.modal_all_products, parent, false);

        MyViewHolder viewHolder = new MyViewHolder();
        Product product = dataList.get(position);
        viewHolder.mProName = (TextView) view.findViewById(R.id.name2);
        viewHolder.mProprice = (TextView) view.findViewById(R.id.price2);
        viewHolder.mProStatus = (TextView) view.findViewById(R.id.status2);
        viewHolder.mProName.setText(product.getProName());
        viewHolder.mProprice.setText(product.getProPrice());
        viewHolder.mProStatus.setText(product.getProStatus() + " " + product.getProUpdate());
        return view;
    }

    private static class MyViewHolder {
        private TextView mProName;
        private TextView mProprice;
        private TextView mProStatus;
    }
    private class ProductFilter extends Filter {
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataList = (ArrayList<Product>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = dataList.size(); i < l; i++)
                add(dataList.get(i));
            notifyDataSetInvalidated();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                Log.e("in filter",allProduct.size()+"");
                ArrayList<Product> filteredItems = new ArrayList<Product>();

                for(int i = 0, l = allProduct.size(); i < l; i++)
                {
                    Product newProduct = allProduct.get(i);
                    if(newProduct.getProName().toString().toLowerCase().contains(constraint))
                        filteredItems.add(newProduct);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {  Log.e("no char :--",allProduct.size()+"");
                    result.values = allProduct;
                    result.count = allProduct.size();

                }
            }
            return result;
        }
    }

}


