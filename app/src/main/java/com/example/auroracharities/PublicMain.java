package com.example.auroracharities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auroracharities.data.model.Charities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PublicMain extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Charities> charitiesList;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "PublicMain";

    public void doStuff(){
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        charitiesList = new ArrayList<Charities>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_main);

        doStuff();

        charitiesList = new ArrayList<>();
        prepareCharities();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(charitiesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        recyclerViewAdapter.setOnItemClickListener(new ClickListener<Charities>(){
            @Override
            public void onItemClick(Charities data) {
                Toast.makeText(PublicMain.this, data.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(recyclerViewAdapter);

    }

    private void prepareCharities(){
        charitiesList.add(new Charities("Charity 1", R.drawable.a4g_logo_background));
        charitiesList.add(new Charities("Charity 2", R.drawable.gfgimage));
        charitiesList.add(new Charities("Charity 3", R.drawable.gfgimage));
        charitiesList.add(new Charities("Charity 4", R.drawable.gfgimage));
        charitiesList.add(new Charities("Charity 5", R.drawable.gfgimage));
        charitiesList.add(new Charities("Charity 6", R.drawable.gfgimage));
        charitiesList.add(new Charities("Charity 7", R.drawable.gfgimage));
    }
}