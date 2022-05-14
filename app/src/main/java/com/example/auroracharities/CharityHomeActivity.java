package com.example.auroracharities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CharityHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private String charity;
    private static final String TAG = "CharityHomeActivity";
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getCharity(){
        if(mAuth.getCurrentUser() != null){
            Log.v(TAG, mAuth.getCurrentUser().getEmail());
            DocumentReference docRef = db.collection("users").document((String)mAuth.getCurrentUser().getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            charity = (String)document.getData().get("charity");
                            Log.v(TAG, charity);
                            TextView welcomeMSG = (TextView) findViewById(R.id.welcomeTextCharityHome);
                            welcomeMSG.setText(charity + " Admin Page");
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        else{
            startActivity(new Intent(CharityHomeActivity.this, LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_home);
        mAuth = FirebaseAuth.getInstance();
        //charity = getIntent().getStringExtra("charity");
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_home_button);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        getCharity();
        if(charity != null) actionBar.setTitle(charity + " Admin Home Page");
        else actionBar.setTitle("Aurora Charities Admin Home Page");
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.viewRequests:
                Intent i = new Intent(CharityHomeActivity.this, ViewEditRequestsActivity.class);
                if(mAuth.getCurrentUser() != null){
                    Log.v(TAG, mAuth.getCurrentUser().getEmail());
                    DocumentReference docRef = db.collection("users").document((String)mAuth.getCurrentUser().getEmail());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    charity = (String)document.getData().get("charity");
                                    i.putExtra("charityName", charity);
                                    startActivity(i);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }

                break;
            case R.id.updateAboutUsButton:
                Intent i2 = new Intent(CharityHomeActivity.this, EditPublicProfileActivity.class);
                i2.putExtra("charityName", charity);
                startActivity(i2);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.signOutButton:
                signOut();
                startActivity(new Intent(CharityHomeActivity.this, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, HomeScreenActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed () {
        Intent i = new Intent(this, HomeScreenActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
