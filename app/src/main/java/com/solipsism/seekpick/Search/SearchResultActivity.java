package com.solipsism.seekpick.Search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.solipsism.seekpick.R;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    TextView searchText;
    ListView listView;
    ListAdapter listAdapter;
    ArrayList<ListItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        String response = intent.getStringExtra("response");

        listView = (ListView) findViewById(R.id.result_list);
        searchText = (TextView) findViewById(R.id.result_text);
        items = new ArrayList<>();

        searchText.setText(response);
        ListItem item1 = new ListItem();
        item1.setName("KKK");
        item1.setAddress("adfasdfa");
        ListItem item2 = new ListItem();
        item1.setName("lll");
        item1.setAddress("afEvsdzgv");
        ListItem item3 = new ListItem();
        item1.setName("mmm");
        item1.setAddress("sevd");
        ListItem item4 = new ListItem();
        item1.setName("hhh");
        item1.setAddress("adfassdzvdfa");
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);

        listAdapter = new ListAdapter(SearchResultActivity.this, items);
        listView.setAdapter(listAdapter);
    }
}
