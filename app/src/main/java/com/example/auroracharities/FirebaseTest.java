package com.example.auroracharities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.auroracharities.data.model.Charities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseTest {
    private final FirebaseFirestore db;
    private final String TAG = "FirebaseTest";
    private final ArrayList<Charities> charitiesList;

    public FirebaseTest(FirebaseFirestore db) {
        this.db = db;
        charitiesList = new ArrayList<Charities>();
    }

    public void  doStuff(){
        db.collection("Charities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map charityData = document.getData();
                                Log.d(TAG, document.getId() + " => " + charityData);
                                Log.v(TAG, (String)charityData.get("motto"));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
