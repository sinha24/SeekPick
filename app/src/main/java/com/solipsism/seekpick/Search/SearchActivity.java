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

import com.solipsism.seekpick.Login.LoginActivity;

public class SearchActivity extends AppCompatActivity {

    EditText searchView;
    Button searchButton;
    ImageView searchUp;
    TextView signIn;
    Animation arrowShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = (EditText) findViewById(R.id.search_text);
        searchButton = (Button) findViewById(R.id.search_btn);
        searchUp = (ImageView) findViewById(R.id.search_up);
        signIn = (TextView) findViewById(R.id.search_sign_in);
        arrowShake = AnimationUtils.loadAnimation(this, R.anim.arrorw_shake);

        searchUp.startAnimation(arrowShake);
        searchView.requestFocus();

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
