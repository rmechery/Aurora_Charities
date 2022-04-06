package com.example.auroracharities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auroracharities.data.model.Charities;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Charities> charitiesList;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        FirebaseTest random = new FirebaseTest(db);
        random.doStuff();

        charitiesList = new ArrayList<>();
        prepareCharities();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(charitiesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        recyclerViewAdapter.setOnItemClickListener(new ClickListener<Charities>(){
            @Override
            public void onItemClick(Charities data) {
                Toast.makeText(MainActivity.this, data.getTitle(), Toast.LENGTH_SHORT).show();
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