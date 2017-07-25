package com.solipsism.seekpick.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.solipsism.seekpick.R;

import java.util.ArrayList;

class ListAdapter extends BaseAdapter {

    private Context cx;
    private ArrayList<ListItem> items;

    ListAdapter(Context cx, ArrayList<ListItem> items) {
        this.cx = cx;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        PlaceHolder holder = new PlaceHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.adapter_search_list, null);
            // Now we can fill the layout with the right values
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView address = (TextView) v.findViewById(R.id.address);

            holder.name = name;
            holder.address = address;

            v.setTag(holder);
        } else
            holder = (PlaceHolder) v.getTag();

        System.out.println("Position [" + position + "]");
        ListItem p = items.get(position);
        holder.name.setText(""+p.getName());
        holder.address.setText("" + p.getAddress()+" - "+p.getPincode()+"\n"+p.getPhone());
        return v;
    }

    private static class PlaceHolder {
        public TextView name;
        TextView address;
    }
}