package com.solipsism.seekpick.Search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.solipsism.seekpick.Dash.DashActivity;
import com.solipsism.seekpick.Login.LoginActivity;
import com.solipsism.seekpick.R;
import com.solipsism.seekpick.utils.PrefsHelper;

public class SearchActivity extends AppCompatActivity {

    SearchFragment searchFragment;
    ImageView searchUp;
    TextView signIn;
    Animation arrowShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(PrefsHelper.getPrefsHelper(SearchActivity.this).getPref(PrefsHelper.PREF_TOKEN,"token").equals("token"))) {
            Intent i = new Intent(SearchActivity.this, DashActivity.class);
            startActivity(i);
            finish();
        }
        setContentView(R.layout.activity_search);
        searchFragment = new SearchFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content,searchFragment).commit();

        searchUp = (ImageView) findViewById(R.id.search_up);
        signIn = (TextView) findViewById(R.id.search_sign_in);
        arrowShake = AnimationUtils.loadAnimation(this, R.anim.arrorw_shake);

        searchUp.startAnimation(arrowShake);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.up_in, R.anim.fade_out);
            }
        });
        searchUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.up_in, R.anim.fade_out);
            }
        });
    }

}