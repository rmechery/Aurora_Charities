package com.example.auroracharities;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Supplier;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.algolia.search.exceptions.AlgoliaApiException;
import com.algolia.search.exceptions.AlgoliaRuntimeException;
import com.algolia.search.models.RequestOptions;
import com.algolia.search.models.indexing.SearchResult;
import com.example.auroracharities.data.model.Charities;
import com.example.auroracharities.data.model.CharitiesAdapter;
import com.example.auroracharities.data.model.Request;
import com.example.auroracharities.data.model.RequestAdapter;
import com.example.auroracharities.data.model.RequestAlgolia;
import com.example.auroracharities.databinding.ActivityPublicMainBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PublicMainActivity extends AppCompatActivity implements CharitiesAdapter.OnItemClickListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener{

    private RecyclerView recyclerView;
    private ArrayList<Charities> charitiesList;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "PublicMain";

    private CharitiesAdapter adapter;
    private LinearLayout charityLayout;

    SearchClient client = DefaultSearchClient.create("J6Z3ZI49SV", "3bce901f2b3a60c69aa8673af38cbecb");
    SearchIndex<RequestAlgolia> index = client.initIndex("Request", RequestAlgolia.class);
    private ActivityPublicMainBinding binding ;
    private RequestAdapter algoliaAdapter;
    private MenuItem search;
    private SearchView searchView;
    private LinearLayout algoliaLayout;

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

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_home_button);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Aurora Charities Homepage");

        //set up algolia adapter
        binding = ActivityPublicMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        algoliaAdapter = new RequestAdapter();
        algoliaLayout = findViewById(R.id.algoliaLayout);
        algoliaLayout.setVisibility(View.GONE);

        binding.algoliaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.algoliaRecyclerView.setAdapter(algoliaAdapter);
        algoliaAdapter.setOnItemClickListener(new RequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(PublicMainActivity.this, "Position: " + position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PublicMainActivity.this, IndividualCharityPageActivity.class);
                i.putExtra("charityDocID",algoliaAdapter.getOldData().get(position).getCharity());
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        //set up Firebase Adapter
        charitiesList = new ArrayList<Charities>();
        recyclerView = findViewById(R.id.recyclerView);
        charityLayout = findViewById(R.id.charityLayout);
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
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.public_main_menu, menu);

        search = (MenuItem)menu.findItem(R.id.public_main_menu_search);
        searchView = (SearchView) search.getActionView();
        //searchView.isSubmitButtonEnabled(this);
        searchView.setOnCloseListener(this);
        searchView.setOnQueryTextListener(this);

        return true;
    }




    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onQueryTextChange(String query) {
        charityLayout.setVisibility(View.INVISIBLE);
        algoliaLayout.setVisibility(View.VISIBLE);
        algoliaLayout.bringToFront();
        if(query != null){
            try {
                searchAlgolia(query);
            } catch (ExecutionException | InterruptedException e) {
                Log.wtf(TAG, e);
                e.printStackTrace();
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void  searchAlgolia(String queryStr) throws ExecutionException, InterruptedException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    SearchResult<RequestAlgolia> search = index.search(new com.algolia.search.models.indexing.Query(queryStr));
                    algoliaAdapter.setData((ArrayList<RequestAlgolia>) search.getHits());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        thread.interrupt();
    }

    @Override
    public boolean onClose() {
        algoliaLayout.setVisibility(View.INVISIBLE);
        charityLayout.setVisibility(View.VISIBLE);
        charityLayout.bringToFront();

        return false;
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