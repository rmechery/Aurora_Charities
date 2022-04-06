package com.example.auroracharities;

import androidx.appcompat.app.AppCompatActivity;

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
                setContentView(R.layout.activity_login);
                break;
            case R.id.continueToIndividual:
                setContentView(R.layout.activity_main);
                break;
        }
    }
}