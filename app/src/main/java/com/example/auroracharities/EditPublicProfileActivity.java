package com.example.auroracharities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditPublicProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EditPublicProfileAct" ;
    private String charity;
    private TextView motto;
    private TextView email;
    private TextView phone;
    private TextView address;
    private ImageView image;

    private FirebaseFirestore db;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_charity_profile);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            charity = extras.getString("charityName");
            ( (TextView) findViewById(R.id.charityViewName)).setText(charity);
        }

        motto =  ( (TextView) findViewById(R.id.editMotto));
        email =  ( (TextView) findViewById(R.id.editEmail));
        phone =  ( (TextView) findViewById(R.id.editPhone));
        address =  ( (TextView) findViewById(R.id.editAddress));
        image = (ImageView)findViewById(R.id.imageView);

        if(charity != null){
            DocumentReference docRef = db.collection("Charities").document(charity);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.v(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.v(TAG, "Current data: " + snapshot.getData().get("motto"));
                        Map<String, Object> docData = snapshot.getData();
                        motto.setText((String)docData.get("motto"));
                        email.setText((String)docData.get("email"));
                        phone.setText((String)docData.get("phone"));
                        address.setText((String)docData.get("addressString"));

                        storageReference.child((String)docData.get("logo")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
                                Glide.with(EditPublicProfileActivity.this)
                                        .load(uri)
                                        .into((ImageView)findViewById(R.id.imageView));

                                image.setBackgroundColor(Color.WHITE);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                Toast.makeText( EditPublicProfileActivity.this,"" + exception,  Toast.LENGTH_LONG);
                            }
                        });

                    } else {
                        Log.v(TAG, "Current data: null");
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.aboutUsSubmit:
                if(motto != null && email != null && phone != null && address != null ){
                    DocumentReference docRef = db.collection("Charities").document(charity);
                    Map<String, Object> map = new HashMap<>();
                    map.put("motto", (String) ""+ motto.getText());
                    map.put("email", (String) ""+email.getText());
                    map.put("phone", (String) ""+phone.getText());
                    map.put("addressString", (String) ""+address.getText());
                    docRef.set(map, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }
        }
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, CharityHomeActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed () {
        Intent i = new Intent(this, CharityHomeActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}