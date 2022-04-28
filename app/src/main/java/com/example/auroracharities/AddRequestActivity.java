package com.example.auroracharities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.auroracharities.data.model.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddRequestActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = "AddRequestActivity";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String charity;
    private EditText nameEditText;
    private Spinner spinnerRequests;
    private EditText tagTypeEditText;
    private EditText tagDescriptionEditText;

    //private String[] sizeTagArr = {"deez Nuts", "gotti"};
    private ArrayList<String> itemsArray = new ArrayList<String>();
    private List<String> ageTaglist;

    private Button ageTagBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        mAuth = FirebaseAuth.getInstance();

//        nameEditText = (EditText)findViewById(R.id.addRequest_NameEditText);
//        spinnerRequests =findViewById(R.id.spinner_categories);
//        tagTypeEditText = (EditText)findViewById(R.id.addRequest_typeEditText);
//        tagDescriptionEditText = (EditText)findViewById(R.id.add_Request_descriptionEditText);
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerRequests.setAdapter(adapter);
//        spinnerRequests.setOnItemSelectedListener(this);

            ageTagBtn = findViewById(R.id.ageTagButton);
            ageTagBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreateAlertDialog(R.array.age, "lkjl");
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
            getCharity();
        }
        else{
            Toast.makeText(this, "Admin is not signed In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddRequestActivity.this, HomeScreenActivity.class));
        }

    }

    private void CreateAlertDialog(int id, String tagType) {
        //ageTagList = new ArrayList<String>();
        ArrayList<String> list = new ArrayList<String>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select size");
        builder.setMultiChoiceItems(id, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    String[] tagArr = getResources().getStringArray(id);
                    Log.d(TAG,tagArr.toString());
                    if(b){
                        list.add(tagArr[i]);
                    } else if(list.contains(tagArr[i])){
                        list.remove(tagArr[i]);
                    }
//                    switch(tagType){
//                        case "age":
//                            Log.d(TAG, "Added something");
//                            break;
//
//                    }
            }
        });
        builder.setPositiveButton("Show", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String data = "";
                for(String item: list){
                    data = data + item;
                }
                Toast.makeText(AddRequestActivity.this, data, Toast.LENGTH_SHORT).show();
            }
        });

        builder.create();
        builder.show();
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
                else if(tagTypeEditText.getText().toString().trim().equals("")){
                    Toast.makeText(this, "Please enter the Request Tag Type.", Toast.LENGTH_SHORT).show();
                }
                else if(tagDescriptionEditText.getText().toString().trim().equals("")){
                    Toast.makeText(this, "Please enter the Request Tag Description.", Toast.LENGTH_SHORT).show();
                }
                else{

                    Map<String, Object> tagMap = new HashMap<>();
                    tagMap.put(tagTypeEditText.getText().toString(), tagDescriptionEditText.getText().toString());

                    Request requestObj = new Request(nameEditText.getText().toString(),
                            spinnerRequests.getSelectedItem().toString(),
                            tagMap);

                    Log.v(TAG, requestObj.toString());
                    db.collection("Charities").document(charity).collection("Requests")
                            .add(requestObj)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                    Toast.makeText(AddRequestActivity.this, "SUCCESS:" + requestObj.getName() + " added to the database.", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddRequestActivity.this, "WARNING: Error Adding Document.", Toast.LENGTH_LONG).show();
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });


                }
                break;
        }
    }

//    public void tagSearchSetup(){
//        String products[] = {"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE",
//                "iPhone 4S", "Samsung Galaxy Note 800",
//                "Samsung Galaxy S3", "MacBook Air", "Mac Mini", "MacBook Pro"};
//        myListView = (ListView) findViewById(R.id.editlist_view);
//        inputSearch = (EditText) findViewById(R.id.itemSearch);
//        myAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, products);
//        myListView.setAdapter(myAdapter);
//    }
}
