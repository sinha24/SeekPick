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
        items =(ArrayList<ListItem>) intent.getSerializableExtra("itemList");
        listAdapter = new ListAdapter(SearchResultActivity.this, items);
        listView.setAdapter(listAdapter);
    }
}
