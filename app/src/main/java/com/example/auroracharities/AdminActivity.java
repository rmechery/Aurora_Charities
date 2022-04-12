package com.example.auroracharities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AdminActivity extends AppCompatActivity {
    private String charity;
    private static final String TAG = "AdminActivity";

    public void setCharity(String charity) {
        this.charity = charity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_home);
        charity = getIntent().getStringExtra("charity");
        Log.v(TAG, charity);
        TextView welcomeMSG = (TextView) findViewById(R.id.welcomeTextCharityHome);
        welcomeMSG.setText(charity + " Admin Page");
    }


}