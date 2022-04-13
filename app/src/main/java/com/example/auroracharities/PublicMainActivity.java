package com.example.auroracharities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auroracharities.data.model.Charities;
import com.example.auroracharities.data.model.CharitiesAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class PublicMainActivity extends AppCompatActivity implements CharitiesAdapter.OnItemClickListener{

    private RecyclerView recyclerView;

    private ArrayList<Charities> charitiesList;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "PublicMain";

    private CharitiesAdapter adapter;

    public void doStuff(){
        //Log.v(TAG, charitiesList.toString());

        db.collection("Charities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map charityData = document.getData();
                                //Log.d(TAG, charityData+ " => " + charityData.get("logo"));
                                Log.d(TAG, charityData.toString());
                                String motto = (String)(charityData.get("motto"));
                                //charitiesList.add(new Charities("Title", motto, R.drawable.gfgimage));
                                Log.d(TAG, charitiesList.toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_main);
        charitiesList = new ArrayList<Charities>();

        recyclerView = findViewById(R.id.recyclerView);

        Query query = FirebaseFirestore.getInstance()
                .collection("Charities");

        FirestoreRecyclerOptions<Charities> options = new FirestoreRecyclerOptions.Builder<Charities>()
                .setQuery(query, Charities.class)
                .build();

        adapter = new CharitiesAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CharitiesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Charities charity = documentSnapshot.toObject(Charities.class);
                String id = documentSnapshot.getId();
                Toast.makeText(PublicMainActivity.this, "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PublicMainActivity.this, IndividualCharityPageActivity.class);
                i.putExtra("charityDocID",id);
                startActivity(i);
            }
        });
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        //adapter.stopListening();
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

    }
}