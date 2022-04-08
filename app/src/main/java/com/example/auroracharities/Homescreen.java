package com.example.auroracharities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Homescreen extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        Log.v("Homescreen", "OKFJLAKFJLEKFJLAKFJLSKFJLDKFJLSDF");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.continueToCharity:
                startActivity(new Intent(Homescreen.this, LoginActivity.class));
                //setContentView(R.layout.activity_login);
                break;
            case R.id.continueToIndividual:
                startActivity(new Intent(Homescreen.this, PublicMain.class));
                //setContentView(R.layout.activity_public_main);
                break;
        }
    }
}