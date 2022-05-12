package com.example.auroracharities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auroracharities.data.model.EditRequest;
import com.example.auroracharities.data.model.ViewRequestCharitiesAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Map;

public class EditPastRequestsActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<EditRequest> requestList;
    private RecyclerView recyclerView;
    private  FirebaseFirestore db;
    private final String TAG = "EditPastRequestsActiv";
    private FirebaseAuth mAuth;
    private Map<String, Object> oldData;

    private ViewRequestCharitiesAdapter adapter;
    private LinearLayout charityLayout;
    private String charityName = "";

    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_past_requests);

        constraintLayout = findViewById(R.id.constraintLayout);

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
                  //  Toast.makeText(EditPastRequestsActivity.this, "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(EditPastRequestsActivity.this, AddRequestActivity.class);

                    DocumentReference docRef = db.collection("Charities").document(charityName).collection("Requests").document(id);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    if(((ArrayList<String>) document.getData().get("ageTag")) != null ) i.putStringArrayListExtra( "ageTag",((ArrayList<String>) document.getData().get("ageTag")));
                                    if(((ArrayList<String>) document.getData().get("categoriesTag"))!= null )i.putStringArrayListExtra( "categoriesTag",((ArrayList<String>) document.getData().get("categoriesTag")));
                                    if(((ArrayList<String>) document.getData().get("conditionTag"))!= null ) i.putStringArrayListExtra( "conditionTag",((ArrayList<String>) document.getData().get("conditionTag")));
                                    if(((ArrayList<String>) document.getData().get("sizeTag")) != null ) i.putStringArrayListExtra( "sizeTag",((ArrayList<String>) document.getData().get("sizeTag")));
                                    if(document.getData().get("description") != null) i.putExtra("description", (String)document.getData().get("description"));
                                    if(document.getData().get("name") != null) i.putExtra("name", (String)document.getData().get("name"));
                                    if(document.getId() != null) i.putExtra("docId", (String)document.getId());
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

            enableSwipeToDeleteAndUndo();

        }

    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getLayoutPosition();
                String id = adapter.getSnapshots().getSnapshot(position).getId();
                DocumentReference docRef = db.collection("Charities").document(charityName).collection("Requests").document(id);


                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                oldData = document.getData();
                                docRef.delete();


                                Snackbar snackbar = Snackbar
                                        .make(constraintLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                                snackbar.setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        db.collection("Charities").document(charityName).collection("Requests").add(oldData);

                                    }
                                });

                                snackbar.setActionTextColor(Color.YELLOW);
                                snackbar.show();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });



            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
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
    public void onClick(@NonNull View view) {
        switch (view.getId()){
            case R.id.viewRequestAddButton:
                Intent i = new Intent(EditPastRequestsActivity.this, AddRequestActivity.class);
                startActivity(i);
                break;
        }
    }
}