package com.example.auroracharities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddRequestActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private static final String TAG = "AddRequestActivity";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String charity;
    private EditText nameEditText;
    private EditText tagDescriptionEditText;

    private ArrayList<String> itemsArray = new ArrayList<String>();
    private List<String> ageTaglist = new ArrayList<String>();
    private List<String> sizeTagList = new ArrayList<String>();
    private List<String> conditionTagList = new ArrayList<String>();
    private List<String> categoriesTagList = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> tagMap = new HashMap<>();
    private HashMap<String, boolean[]> selectedItemsMap = new HashMap<>();

    private Button ageTagBtn;
    private Button sizeTagBtn;
    private Button conditionTagBtn;
    private Button categoriesTagBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        mAuth = FirebaseAuth.getInstance();


        nameEditText = (EditText)findViewById(R.id.addRequest_NameEditText);
        tagDescriptionEditText = (EditText)findViewById(R.id.addRequest_NameEditText2);

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
            getCharity();
        }
        else{
            Toast.makeText(this, "Admin is not signed In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddRequestActivity.this, HomeScreenActivity.class));
        }
    }

    private void CreateAlertDialog(int id, String tagType) {
        if (!tagMap.containsKey(tagType)) tagMap.put(tagType,  new ArrayList<String>());
        ArrayList<String> list = tagMap.get(tagType);

        if(getIntent().getExtras().getStringArray("ageTagEdit") != null){
            //tagMap.put("age" , (ArrayList<String>)Arrays.asList( getIntent().getExtras().getStringArray("ageTagEdit")));
        }


        if (!selectedItemsMap.containsKey(tagType)) selectedItemsMap.put(tagType, new boolean[getResources().getStringArray(id).length]);
        boolean[] selectedItems = selectedItemsMap.get(tagType);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Select " + tagType);
        builder.setMultiChoiceItems(id, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    String[] tagArr = getResources().getStringArray(id);

                    Log.d(TAG, Arrays.toString(selectedItems));
                    selectedItems[i] = b;

                    if(b){
                        list.add(tagArr[i]);
                    } else if(list.contains(tagArr[i])){
                        list.remove(tagArr[i]);
                    }
            }
        });
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String data = "";
                for(String item: list){
                    data = data + item;
                }
                Toast.makeText(AddRequestActivity.this, data, Toast.LENGTH_SHORT).show();
                Log.d(TAG, list.toString());
            }
        });

        builder.create().setCanceledOnTouchOutside(true);
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
//                else if(tagTypeEditText.getText().toString().trim().equals("")){
//                    Toast.makeText(this, "Please enter the Request Tag Type.", Toast.LENGTH_SHORT).show();
//                }
                else if(tagDescriptionEditText.getText().toString().trim().equals("")){
                    Toast.makeText(this, "Please enter the Request Tag Description.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Request requestObj = new Request(charity, nameEditText.getText().toString(),
                            tagDescriptionEditText.getText().toString(),
                            tagMap.get("age"), tagMap.get("size"), tagMap.get("categories"), tagMap.get("condition"));

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

}
