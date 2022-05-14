package com.example.auroracharities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.auroracharities.data.model.EditRequest;
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
    private ArrayList<String> ageTaglist = new ArrayList<String>();
    private ArrayList<String> sizeTagList = new ArrayList<String>();
    private ArrayList<String> conditionTagList = new ArrayList<String>();
    private ArrayList<String> categoriesTagList = new ArrayList<String>();
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

        getCharity();


        nameEditText = (EditText)findViewById(R.id.addRequest_NameEditText);
        tagDescriptionEditText = (EditText)findViewById(R.id.addRequest_NameEditText2);

        if (getIntent().getExtras() != null) {
            if(getIntent().getExtras().get("name") != null) {
                nameEditText.setText((String)getIntent().getExtras().get("name"));
            }
            if(getIntent().getExtras().get("description") != null) {
                tagDescriptionEditText.setText((String)getIntent().getExtras().get("description"));
            }
            if(getIntent().getExtras().get("ageTag") != null) {
                ageTaglist = (ArrayList<String>) getIntent().getExtras().get("ageTag");
            }
            if(getIntent().getExtras().get("categoriesTag") != null) {
                ageTaglist = (ArrayList<String>) getIntent().getExtras().get("categoriesTag");
            }
            if(getIntent().getExtras().get("conditionTag") != null) {
                ageTaglist = (ArrayList<String>) getIntent().getExtras().get("conditionTag");
            }
            if(getIntent().getExtras().get("sizeTag") != null) {
                ageTaglist = (ArrayList<String>) getIntent().getExtras().get("sizeTag");
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
            startActivity(new Intent(AddRequestActivity.this, HomeScreenActivity.class));
        }
    }

    private void CreateAlertDialog(int id, String tagType){
        //View view = LayoutInflater.from(this).inflate(R.layout.activity_add_request, null);
        ChipGroup chipGroup = new ChipGroup(this);
        for(int i = 0; i < getResources().getStringArray(id).length; i++){
            Chip chip = new Chip(this);
            LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,0,0,0);
            chip.setLayoutParams(layoutParams);
            chip.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            chip.setClickable(true);
            chip.setCheckable(true);
            chip.setText(getResources().getStringArray(id)[i]);
            switch(tagType){
                case "ageTag":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#3FA67A")));
                    break;
                case "categoriesTag":

                    break;
                case "conditionTag":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#5196BC")));
                    break;
                case "sizeTag":
                    chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#AA065A")));
                    break;
            }

            chipGroup.addView(chip);
        }


        new AlertDialog.Builder(this)
                .setTitle("Add " + tagType + " tag")
                .setMessage("Click on any number of tags. Press done when completed or cancel to clear selection")
                .setView(chipGroup)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // Continue with delete operation
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void CreateAlertDialog2(int id, String tagType) {
        if (!tagMap.containsKey(tagType)) tagMap.put(tagType,  new ArrayList<String>());
        ArrayList<String> list = tagMap.get(tagType);

        boolean[] selectedItems;
        if (!selectedItemsMap.containsKey(tagType)) selectedItemsMap.put(tagType, new boolean[getResources().getStringArray(id).length]);
        selectedItems = selectedItemsMap.get(tagType);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Bundle bundle = getIntent().getExtras();
        List<Boolean> selectedArrList = new ArrayList<Boolean>();
        if(bundle != null) {
            if (tagType == "age"  &&  bundle.getStringArrayList("ageTag") != null) {
                for (int e = 0; e < getResources().getStringArray(R.array.ageTagArr).length; e++) {
                    selectedArrList.add(false);
                }
                for (int i = 0; i < getResources().getStringArray(R.array.ageTagArr).length; i++) {
                    for (int j = 0; j < bundle.getStringArrayList("ageTag").size(); j++) {
                        if (getResources().getStringArray(R.array.ageTagArr)[i].contains( bundle.getStringArrayList("ageTag").get(j))) {
                            selectedArrList.set(i, true);
                            list.add(getResources().getStringArray(R.array.ageTagArr)[i]);
                        }
                    }
                }
            }
            else if (tagType == "categories"  &&  bundle.getStringArrayList("categoriesTag") != null) {
                for (int e = 0; e < getResources().getStringArray(R.array.categoriesTagArr).length; e++) {
                    selectedArrList.add(false);
                }
                for (int i = 0; i < getResources().getStringArray(R.array.categoriesTagArr).length; i++) {
                    for (int j = 0; j < bundle.getStringArrayList("categoriesTag").size(); j++) {
                        if (getResources().getStringArray(R.array.categoriesTagArr)[i].contains( bundle.getStringArrayList("categoriesTag").get(j))) {
                            selectedArrList.set(i, true);
                            list.add(getResources().getStringArray(R.array.categoriesTagArr)[i]);
                        }
                    }
                }
            }
            else if (tagType == "condition"  &&  bundle.getStringArrayList("conditionTag") != null) {
                for (int e = 0; e < getResources().getStringArray(R.array.conditionTagArr).length; e++) {
                    selectedArrList.add(false);
                }
                for (int i = 0; i < getResources().getStringArray(R.array.conditionTagArr).length; i++) {
                    for (int j = 0; j < bundle.getStringArrayList("conditionTag").size(); j++) {
                        if (getResources().getStringArray(R.array.conditionTagArr)[i].contains( bundle.getStringArrayList("conditionTag").get(j))) {
                            selectedArrList.set(i, true);
                            list.add(getResources().getStringArray(R.array.conditionTagArr)[i]);
                        }
                    }
                }
            }
            else if (tagType == "size"  &&  bundle.getStringArrayList("sizeTag") != null) {
                for (int e = 0; e < getResources().getStringArray(R.array.sizeTagArr).length; e++) {
                    selectedArrList.add(false);
                }
                for (int i = 0; i < getResources().getStringArray(R.array.sizeTagArr).length; i++) {
                    for (int j = 0; j < bundle.getStringArrayList("sizeTag").size(); j++) {
                        if (getResources().getStringArray(R.array.sizeTagArr)[i].contains( bundle.getStringArrayList("sizeTag").get(j))) {
                            selectedArrList.set(i, true);
                            list.add(getResources().getStringArray(R.array.sizeTagArr)[i]);
                        }
                    }
                }
            }

            if (selectedArrList.size() != 0) {
                selectedItems = new boolean[ selectedArrList.toArray().length];
                for(int k = 0; k < selectedArrList.size(); k++){
                    selectedItems[k] = (boolean) selectedArrList.get(k);
                }
            }
        }

        builder.setCancelable(true);
        builder.setTitle("Select " + tagType);
        boolean[] finalSelectedItems = selectedItems;
        builder.setMultiChoiceItems(id, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    String[] tagArr = getResources().getStringArray(id);

                    Log.d(TAG, Arrays.toString(finalSelectedItems));
                    finalSelectedItems[i] = b;

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
                                        Toast.makeText(AddRequestActivity.this, "SUCCESS:" + requestObj.getName() + " added to the database.", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(AddRequestActivity.this, EditPastRequestsActivity.class);
                                        startActivity(i);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddRequestActivity.this, "WARNING: Error Adding Document.", Toast.LENGTH_LONG).show();
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                    } else if (getIntent().getExtras() != null && getIntent().getExtras().get("docId") != null){
                        db.collection("Charities").document(charity).collection("Requests").document((String) getIntent().getExtras().get("docId"))
                                .set(requestObj)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(AddRequestActivity.this, "SUCCESS:" + requestObj.getName() + " updated on database.", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(AddRequestActivity.this, EditPastRequestsActivity.class);
                                        startActivity(i);
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
                }
                break;
        }
    }


}
