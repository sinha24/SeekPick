package com.solipsism.seekpick.Dash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.solipsism.seekpick.R;

public class DashActivity extends AppCompatActivity {
    AddFragment maddFragment;
    SearchFragment msearchFragment;
    MyProductsFragment myProductsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        maddFragment = new AddFragment();
        msearchFragment = new SearchFragment();
        myProductsFragment = new MyProductsFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, msearchFragment).commit();

        BottomNavigationView navigation =
                (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleBottomNavigationItemSelected(item);
                return true;
            }

            private void handleBottomNavigationItemSelected (MenuItem item){
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, msearchFragment).commit();
                        break;
                    case R.id.navigation_dashboard:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, maddFragment).commit();
                        break;
                    case R.id.navigation_notifications:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, myProductsFragment).commit();
                        break;
                }
            }
        });

    }
}