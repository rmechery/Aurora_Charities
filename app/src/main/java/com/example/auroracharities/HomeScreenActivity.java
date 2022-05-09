package com.example.auroracharities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.continueToCharity:
                if (user != null) {
                    // User is signed in
                    //Log.v("HomeScreenActivity",user.getEmail());
                    startActivity(new Intent(HomeScreenActivity.this, CharityHomeActivity.class));
                } else {
                    // No user is signed in
                    startActivity(new Intent(HomeScreenActivity.this, LoginActivity.class));
                }

                break;
            case R.id.continueToIndividual:
                startActivity(new Intent(HomeScreenActivity.this, PublicMainActivity.class));
                //setContentView(R.layout.activity_public_main);
                break;
        }
    }

    public void onBackPressed () {
        //do nothing when back button is pressed on the homescreen
    }
}