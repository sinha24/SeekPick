package com.solipsism.seekpick;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.solipsism.seekpick.Dash.DashActivity;
import com.solipsism.seekpick.Dash.SearchFragment;
import com.solipsism.seekpick.Login.LoginActivity;
import com.solipsism.seekpick.utils.PrefsHelper;

public class SearchActivity extends AppCompatActivity {

    SearchFragment msearchFragment;
    ImageView searchUp;
    TextView signIn;
    Animation arrowShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        msearchFragment = new SearchFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.searchFrame, msearchFragment).commit();

        searchUp = (ImageView) findViewById(R.id.search_up);
        signIn = (TextView) findViewById(R.id.search_sign_in);
        arrowShake = AnimationUtils.loadAnimation(this, R.anim.arrorw_shake);

        searchUp.startAnimation(arrowShake);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrefsHelper.getPrefsHelper(SearchActivity.this).getPref(PrefsHelper.PREF_TOKEN,"token").equals("token")) {
                    Intent i = new Intent(SearchActivity.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.up_in, R.anim.fade_out);
                } else {
                    Intent i = new Intent(SearchActivity.this, DashActivity.class);
                    startActivity(i);
                    finish();
                }
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
