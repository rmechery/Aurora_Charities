package com.example.auroracharities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auroracharities.data.model.EditRequest;
import com.example.auroracharities.data.model.ViewRequestCharitiesAdapter;
import com.example.auroracharities.data.model.ViewRequestIndividualAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

public class IndividualCharityPageActivity extends AppCompatActivity {
    String charityDocID;
    private FirebaseFirestore db;
    private static final String TAG = "IndividualCharityPage";
    private StorageReference storageReference;

    private ArrayList<EditRequest> requestList;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private Map<String, Object> oldData;

    private ViewRequestIndividualAdapter adapter;
    private LinearLayout charityLayout;
    private String charityName = "";

    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_charity_page);

        db = FirebaseFirestore.getInstance();

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        storageReference = FirebaseStorage.getInstance().getReference();

        TextView charityName = (TextView) findViewById(R.id.ind_charityName) ;
        ImageView charityLogo = (ImageView) findViewById(R.id.ind_charityLogo);
        TextView mottoText = (TextView) findViewById(R.id.ind_mottoText) ;
        TextView emailText = (TextView) findViewById(R.id.ind_emailText) ;
        TextView phoneText = (TextView) findViewById(R.id.ind_phoneText) ;
        TextView addressText = (TextView) findViewById(R.id.ind_addressText) ;


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            charityDocID = extras.getString("charityDocID");
            //The key argument here must match that used in the other activity
        }

        requestList = new ArrayList<EditRequest>();
        recyclerView = findViewById(R.id.individualRecyclerView);
        charityLayout = findViewById(R.id.individualLinearLayout);

        Query query = db.collection("Charities").document("Veterans Inc").collection("Requests").whereNotEqualTo("name", null);
        FirestoreRecyclerOptions<EditRequest> options = new FirestoreRecyclerOptions.Builder<EditRequest>()
                .setQuery(query, EditRequest.class)
                .build();
        adapter = new ViewRequestIndividualAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        Log.v(TAG, "Charity DOC ID ->" + charityDocID);
        DocumentReference docRef = db.collection("Charities").document(charityDocID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.v(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.v(TAG, "Current data: " + snapshot.getData().get("title"));
                    Map<String, Object> docData = snapshot.getData();
                    charityName.setText((String)docData.get("title"));
                    mottoText.setText((String)docData.get("motto"));
                    emailText.setText((String)docData.get("email"));
                    phoneText.setText((String)docData.get("phone"));
                    addressText.setText((String)docData.get("addressString"));

                    storageReference.child((String)docData.get("logo")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            Glide.with(IndividualCharityPageActivity.this)
                                    .load(uri)
                                    .into((ImageView)findViewById(R.id.ind_charityLogo));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Toast.makeText( IndividualCharityPageActivity.this,"" + exception,  Toast.LENGTH_LONG);
                        }
                    });


                } else {
                    Log.v(TAG, "Current data: null");
                }
            }
        });
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(IndividualCharityPageActivity.this, PublicMainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed () {
        Intent i = new Intent(IndividualCharityPageActivity.this, PublicMainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
