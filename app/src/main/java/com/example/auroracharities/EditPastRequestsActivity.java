package com.example.auroracharities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auroracharities.data.model.Charities;
import com.example.auroracharities.data.model.CharitiesAdapter;
import com.example.auroracharities.data.model.EditRequest;
import com.example.auroracharities.data.model.Request;
import com.example.auroracharities.data.model.ViewRequestCharitiesAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditPastRequestsActivity extends AppCompatActivity {

    private ArrayList<EditRequest> requestList;
    private RecyclerView recyclerView;
    private  FirebaseFirestore db;
    private final String TAG = "EditPastRequestsActiv";
    private FirebaseAuth mAuth;


    private ViewRequestCharitiesAdapter adapter;
    private LinearLayout charityLayout;
    private String charityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_past_requests);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        charityName = (String) getIntent().getSerializableExtra("charityName");

        requestList = new ArrayList<EditRequest>();
        recyclerView = findViewById(R.id.viewRequestRecyclerView);
        charityLayout = findViewById(R.id.viewRequestLayout);
        if(mAuth.getCurrentUser() != null){
            Query query =  db.collection("Charities").document(charityName).collection("Requests").whereNotEqualTo("name", null);
            FirestoreRecyclerOptions<EditRequest> options = new FirestoreRecyclerOptions.Builder<EditRequest>()
                    .setQuery(query, EditRequest.class)
                    .build();
            adapter = new ViewRequestCharitiesAdapter(options);
            // Connecting Adapter class with the Recycler view*/
            recyclerView.setLayoutManager(new LinearLayoutManager(EditPastRequestsActivity.this));
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new ViewRequestCharitiesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    EditRequest request = documentSnapshot.toObject(EditRequest.class);
                    String id = documentSnapshot.getId();
                    Toast.makeText(EditPastRequestsActivity.this, "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(EditPastRequestsActivity.this, AddRequestActivity.class);

                    DocumentReference docRef = db.collection("Charities").document(charityName).collection("Requests").document(id);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    if(document.getData().get("ageTag") != null) i.putExtra( "ageTagEdit",((List<String>)document.getData().get("ageTag")).toArray());
                                    if(document.getData().get("categoriesTag") != null)i.putExtra( "categoriesTagEdit",((List<String>)document.getData().get("categoriesTag")).toArray());
                                    if(document.getData().get("conditionTag") != null) i.putExtra( "conditionTagEdit",((List<String>)document.getData().get("conditionTag")).toArray());
                                    if(document.getData().get("sizeTagEdit") != null) i.putExtra( "sizeTagEdit",((List<String>)document.getData().get("sizeTagEdit")).toArray());
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
            });

        }


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
}