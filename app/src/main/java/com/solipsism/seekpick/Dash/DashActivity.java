package com.solipsism.seekpick.Dash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.solipsism.seekpick.Login.LoginActivity;
import com.solipsism.seekpick.R;
import com.solipsism.seekpick.Search.SearchFragment;
import com.solipsism.seekpick.utils.PrefsHelper;

public class DashActivity extends AppCompatActivity {
    AddProductFragment addProductFragment;
    SearchFragment searchFragment;
    MyProductsFragment myProductsFragment;
    MyProfileFragment myProfileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        addProductFragment = new AddProductFragment();
        searchFragment = new SearchFragment();
        myProductsFragment = new MyProductsFragment();
        myProfileFragment = new MyProfileFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, searchFragment).commit();

        BottomNavigationView navigation =
                (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleBottomNavigationItemSelected(item);
                return true;
            }

            private void handleBottomNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_search:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, searchFragment).commit();
                        break;
                    case R.id.navigation_add_product:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, addProductFragment).commit();
                        break;
                    case R.id.navigation_my_product:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, myProductsFragment).commit();
                        break;
                    case R.id.navigation_my_profile:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, myProfileFragment).commit();
                        break;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // show dash_menu when dash_menu button is pressed
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dash_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            PrefsHelper.getPrefsHelper(DashActivity.this).savePref(PrefsHelper.PREF_TOKEN, "token");
            Intent i = new Intent(DashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }

}