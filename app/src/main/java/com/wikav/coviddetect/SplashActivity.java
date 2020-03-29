package com.wikav.coviddetect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {
    SessionManager sessionManger;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        sessionManger = new SessionManager(this);

        if(!sessionManger.isLoging()){
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
