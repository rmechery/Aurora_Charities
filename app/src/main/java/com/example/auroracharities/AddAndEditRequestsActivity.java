package com.example.auroracharities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.example.auroracharities.data.model.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddAndEditRequestsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private static final String TAG = "AddRequestActivity";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String charity;
    private EditText nameEditText;
    private EditText tagDescriptionEditText;

    private ArrayList<String> itemsArray = new ArrayList<String>();
    private ArrayList<String> ageTaglist = new ArrayList<String>();
    private ArrayList<String> sizeTagList = new ArrayList<String>();
    private ArrayList<String> conditionTagList = new ArrayList<String>();
    private ArrayList<String> categoriesTagList = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> tagMap;
    private HashMap<String, boolean[]> selectedItemsMap = new HashMap<>();

    private Button ageTagBtn;
    private Button sizeTagBtn;
    private Button conditionTagBtn;
    private Button categoriesTagBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_edit_requests);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // Customize the back button
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_delete);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        getCharity();
        if(charity != null) actionBar.setTitle(charity + " Create/Edit Request Page");
        else actionBar.setTitle("Aurora Charities Create/Edit Request Page");

        tagMap = new HashMap<>();

        nameEditText = (EditText)findViewById(R.id.addRequest_NameEditText);
        tagDescriptionEditText = (EditText)findViewById(R.id.addRequest_NameEditText2);

        tagMap.put("age", new ArrayList<String>());
        tagMap.put("categories", new ArrayList<String>());
        tagMap.put("condition", new ArrayList<String>());
        tagMap.put("size", new ArrayList<String>());

        if (getIntent().getExtras() != null) {
            if(getIntent().getExtras().get("name") != null) {
                nameEditText.setText((String)getIntent().getExtras().get("name"));
            }
            if(getIntent().getExtras().get("description") != null) {
                tagDescriptionEditText.setText((String)getIntent().getExtras().get("description"));
            }
            if(getIntent().getExtras().get("age") != null) {
                tagMap.put("age", (ArrayList<String>) getIntent().getExtras().get("age"));
            }
            if(getIntent().getExtras().get("categories") != null) {
                 tagMap.put("categories", (ArrayList<String>) getIntent().getExtras().get("categories"));
            }
            if(getIntent().getExtras().get("condition") != null) {
                 tagMap.put("condition", (ArrayList<String>) getIntent().getExtras().get("condition"));
            }
            if(getIntent().getExtras().get("size") != null) {
                 tagMap.put("size", (ArrayList<String>) getIntent().getExtras().get("size"));
            }
        }

        ageTagBtn = findViewById(R.id.ageTagButton);
            ageTagBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreateAlertDialog(R.array.ageTagArr, "age");
                }
            });

            sizeTagBtn = findViewById(R.id.sizeTagButton);
            sizeTagBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreateAlertDialog(R.array.sizeTagArr, "size");
                }
            });

            conditionTagBtn = findViewById(R.id.conditionTagBtn);
            conditionTagBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreateAlertDialog(R.array.conditionTagArr, "condition");
                }
            });

            categoriesTagBtn = findViewById(R.id.categoriesTagBtn);
            categoriesTagBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreateAlertDialog(R.array.categoriesTagArr, "categories");
                }
            });
    }

    public void getCharity(){
        if(mAuth.getCurrentUser() != null){
            Log.v(TAG, mAuth.getCurrentUser().getEmail());
            DocumentReference docRef = db.collection("users").document( mAuth.getCurrentUser().getEmail());
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

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(this, "Admin is signed In", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Admin is not signed In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddAndEditRequestsActivity.this, HomeScreenActivity.class));
        }
    }

    private void CreateAlertDialog(int id, String tagType){
        //View view = LayoutInflater.from(this).inflate(R.layout.activity_add_request, null);
        ChipGroup chipGroup = new ChipGroup(this);
        for(int i = 0; i < getResources().getStringArray(id).length; i++){
            Chip chip = new Chip(this);
            LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10,10,10,10);
            chip.setLayoutParams(layoutParams);
            chip.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            chip.setClickable(true);
            chip.setCheckable(true);
            chip.setPadding(15,5,15,5);
            chip.setText(getResources().getStringArray(id)[i]);
            switch(tagType){
                case "age":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#3FA67A")));
                    break;
                case "categories":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#7249A5")));
                    break;
                case "condition":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#5196BC")));
                    break;
                case "size":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#AA065A")));
                    break;
            }
            if (tagMap.get(tagType) != null ) {
                for(String j : tagMap.get(tagType)){
                    if (j.equalsIgnoreCase(getResources().getStringArray(id)[i])) chip.setChecked(true);
                }
            }
            chipGroup.addView(chip);
        }


        new AlertDialog.Builder(this)
                .setTitle("Add " + tagType + " tag")
                .setMessage("Click on any number of tags. Press done when completed or cancel to clear selection.")
                .setView(chipGroup)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // Continue with delete operation
                    tagMap.get(tagType).clear();
                    for (int i=0; i<chipGroup.getChildCount();i++){
                        Chip chip = (Chip)chipGroup.getChildAt(i);
                        if (chip.isChecked()){
                            //this chip is selected.....
                            tagMap.get(tagType).add((String) chip.getText());
                        }
                    }
                    Set<String> set = new HashSet<>(tagMap.get(tagType));
                    tagMap.get(tagType).clear();
                    tagMap.get(tagType).addAll(set);
                    Log.v(TAG, tagMap.toString());
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#8A8383"));
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addRequest_SubmitButton:
                if(nameEditText.getText().toString().trim().equals("")){
                    Toast.makeText(this, "Please enter the Request Name.", Toast.LENGTH_SHORT).show();
                }
                else if(tagDescriptionEditText.getText().toString().trim().equals("")){
                    Toast.makeText(this, "Please enter the Request Tag Description.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Request requestObj = new Request(charity, nameEditText.getText().toString(),
                            tagDescriptionEditText.getText().toString(),
                            tagMap.get("age"), tagMap.get("size"), tagMap.get("categories"), tagMap.get("condition"));

                    Log.v(TAG, requestObj.toString());
                    if (getIntent().getExtras() == null) {
                        db.collection("Charities").document(charity).collection("Requests")
                                .add(requestObj)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                        Toast.makeText(AddAndEditRequestsActivity.this, "SUCCESS:" + requestObj.getName() + " added to the database.", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(AddAndEditRequestsActivity.this, ViewEditRequestsActivity.class);
                                        i.putExtra("charityName", charity);
                                        startActivity(i);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out  );
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddAndEditRequestsActivity.this, "WARNING: Error Adding Document.", Toast.LENGTH_LONG).show();
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                    } else if (getIntent().getExtras() != null && getIntent().getExtras().get("docId") != null){
                        db.collection("Charities").document(charity).collection("Requests").document((String) getIntent().getExtras().get("docId"))
                                .set(requestObj)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(AddAndEditRequestsActivity.this, "SUCCESS:" + requestObj.getName() + " updated on database.", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(AddAndEditRequestsActivity.this, ViewEditRequestsActivity.class);
                                        i.putExtra("charityName", charity);
                                        startActivity(i);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out  );
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddAndEditRequestsActivity.this, "WARNING: Error Adding Document.", Toast.LENGTH_LONG).show();
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                    }
                }
                break;
        }
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, ViewEditRequestsActivity.class);
                i.putExtra("charityName", charity);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out );
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed () {
        Intent i = new Intent(this, ViewEditRequestsActivity.class);
        i.putExtra("charityName", charity);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out  );

    }


}
