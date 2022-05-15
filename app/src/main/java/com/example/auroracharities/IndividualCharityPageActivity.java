package com.example.auroracharities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auroracharities.data.model.EditRequest;
import com.example.auroracharities.data.model.ViewRequestCharitiesAdapter;
import com.example.auroracharities.data.model.ViewRequestIndividualAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    private String charity;

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
            charity = extras.getString("charityName");
            //The key argument here must match that used in the other activity
        }

        requestList = new ArrayList<EditRequest>();
        recyclerView = (RecyclerView) findViewById(R.id.individualRecyclerView);
        charityLayout = findViewById(R.id.individualLinearLayout);


        if (charityDocID != null) {
            Query query = db.collection("Charities").document(charityDocID).collection("Requests").whereNotEqualTo("name", null);
            FirestoreRecyclerOptions<EditRequest> options = new FirestoreRecyclerOptions.Builder<EditRequest>()
                    .setQuery(query, EditRequest.class)
                    .build();
            adapter = new ViewRequestIndividualAdapter(options);
            // Connecting Adapter class with the Recycler view*/
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }


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
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed () {
        Intent i = new Intent(IndividualCharityPageActivity.this, PublicMainActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();

        db.collection("Charities").document(charityDocID).collection("Requests").whereNotEqualTo("name", null).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Query query = db.collection("Charities").document(charityDocID).collection("Requests").whereNotEqualTo("name", null);
                FirestoreRecyclerOptions<EditRequest> options = new FirestoreRecyclerOptions.Builder<EditRequest>()
                        .setQuery(query, EditRequest.class)
                        .build();
                adapter = new ViewRequestIndividualAdapter(options);
                // Connecting Adapter class with the Recycler view*/
                recyclerView.setLayoutManager(new LinearLayoutManager(IndividualCharityPageActivity.this));
                recyclerView.setAdapter(adapter);

                if(adapter != null) {

                    adapter.setOnItemClickListener(new ViewRequestIndividualAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                            LinearLayout layout = new LinearLayout(IndividualCharityPageActivity.this);
                            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layout.setOrientation(LinearLayout.VERTICAL);
                            layout.setLayoutParams(parms);

                            layout.setGravity(Gravity.CLIP_VERTICAL);
                            layout.setPadding(2, 2, 2, 2);


                            EditText nameEditText = new EditText(IndividualCharityPageActivity.this);
                            TextView nameTextView = new TextView(IndividualCharityPageActivity.this);
                            EditText emailEditText = new EditText(IndividualCharityPageActivity.this);
                            emailEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                            TextView emailTextView = new TextView(IndividualCharityPageActivity.this);
                            EditText msgEditText = new EditText(IndividualCharityPageActivity.this);
                            TextView msgTextView = new TextView(IndividualCharityPageActivity.this);

                            nameTextView.setText("Enter Name:");
                            nameTextView.setPadding(0, 10, 0, 5);
                            nameTextView.setGravity(Gravity.LEFT);
                            nameTextView.setTextSize(20);

                            emailTextView.setText("Enter Email:");
                            emailTextView.setPadding(0, 10, 0, 5);
                            emailTextView.setGravity(Gravity.LEFT);
                            emailTextView.setTextSize(20);

                            msgTextView.setText("Enter Message (Optional):");
                            msgTextView.setPadding(0, 10, 0, 5);
                            msgTextView.setGravity(Gravity.LEFT);
                            msgTextView.setTextSize(20);

                            LinearLayout.LayoutParams nameTextViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            nameTextViewParams.bottomMargin = 5;
                            layout.addView(nameTextView,nameTextViewParams);
                            layout.addView(nameEditText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                            LinearLayout.LayoutParams emailTextViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            emailTextViewParams.bottomMargin = 5;
                            layout.addView(emailTextView,emailTextViewParams);
                            layout.addView(emailEditText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                            LinearLayout.LayoutParams msgTextViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            msgTextViewParams.bottomMargin = 5;
                            layout.addView(msgTextView,msgTextViewParams);
                            layout.addView(msgEditText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                            new AlertDialog.Builder(IndividualCharityPageActivity.this)
                                    .setTitle("Connect with Charity")
                                    .setMessage("It's great to see you are interested in donating this item. " +
                                            "Do you want to connect with a charity admin to complete the donation? " +
                                            "If yes, enter your name and email below.")
                                    .setView(layout)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                           // Toast.makeText(((Dialog) dialog).getContext(), "send email" + documentSnapshot.get("name").toString(), Toast.LENGTH_SHORT).show();

                                                try {
                                                    Map<String, Object> emailObj = new HashMap<>();
                                                    Map<String, Object> emailObjMessage = new HashMap<>();
                                                    emailObj.put("to", Arrays.asList(new String[]{"ryanmechery@gmail.com"}));
                                                    emailObjMessage.put("subject", documentSnapshot.get("name").toString() + " – An Aurora Charities User");
                                                    String emailMessage = "Aurora Charities User named " + nameEditText.getText().toString() + " wants to donate " + documentSnapshot.get("name").toString()  + ".";
                                                    if(emailEditText.getText().toString() != null){
                                                        emailMessage +="\nYou can contact them at " + emailEditText.getText().toString() + ".";
                                                    }
                                                    if(msgEditText.getText().toString() != null){
                                                        emailMessage += "\nTheir message was: \n\t\t" +  msgEditText.getText().toString();
                                                    }
                                                    emailMessage += "\n\nThanks,\n\tAurora Charities Development Team";
                                                    emailObjMessage.put("text", emailMessage);
                                                    emailObj.put("message", emailObjMessage);
                                                    db.collection("mail").add(emailObj).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            Toast.makeText(IndividualCharityPageActivity.this, "SUCCESS: Charity notified", Toast.LENGTH_SHORT);
                                                        }
                                                    });
                                                } catch (Exception e) {
                                                    Toast.makeText(((Dialog) dialog).getContext(), "App was unable to notify the charity. Please try again later." , Toast.LENGTH_SHORT).show();
                                                    e.printStackTrace();
                                                }

                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_email)
                                    .show();
                        }
                    });

                    adapter.startListening();
                }
            }
        });

    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    public void getCharity(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Log.v(TAG, FirebaseAuth.getInstance().getCurrentUser().getEmail());
            DocumentReference docRef = db.collection("users").document( FirebaseAuth.getInstance().getCurrentUser().getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            charity = (String)document.getData().get("charity");
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }


}
