package com.example.auroracharities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CharityHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private String charity = "charity";
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
        getCharity();
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.viewRequests:
                Intent i = new Intent(CharityHomeActivity.this, EditPastRequestsActivity.class);
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
                startActivity(new Intent(CharityHomeActivity.this, EditAboutusPageActivity.class));
                break;
            case R.id.signOutButton:
                signOut();
                startActivity(new Intent(CharityHomeActivity.this, LoginActivity.class));
                break;
        }
    }
}
