package com.example.auroracharities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.auroracharities.data.model.Charities;
import com.example.auroracharities.RecyclerViewAdapter;
import com.example.auroracharities.ClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Charities> charitiesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        charitiesList.add(new Charities("Charity 1", R.drawable.gfgimage));
        charitiesList.add(new Charities("Charity 2", R.drawable.gfgimage));
        charitiesList.add(new Charities("Charity 3", R.drawable.gfgimage));
        charitiesList.add(new Charities("Charity 4", R.drawable.gfgimage));
        charitiesList.add(new Charities("Charity 5", R.drawable.gfgimage));
        charitiesList.add(new Charities("Charity 6", R.drawable.gfgimage));
        charitiesList.add(new Charities("Charity 7", R.drawable.gfgimage));
    }
}