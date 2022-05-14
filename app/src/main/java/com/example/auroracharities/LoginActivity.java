package com.example.auroracharities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button signInBtn;
    private EditText unEdit;
    private EditText pwEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // [START initialize_auth]
        // Initialize Firebase Auth
        Log.v(TAG, "LoginActivity.java loaded.");

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        signInBtn = findViewById(R.id.signInButton);
        unEdit = (EditText)findViewById(R.id.username);
        pwEdit = (EditText)findViewById(R.id.password);

        signInBtn.setOnClickListener((View.OnClickListener) this);
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        Log.v("This worked", "This worked");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            db.collection("users")
                                    .whereEqualTo(FieldPath.documentId(), email.toLowerCase())
                                    .whereEqualTo("type", "charityAdmin")
                                    .limit(1)
                                    .get()
                                    .addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {
                                            if(task2.getResult().getDocuments().size() > 0) {
                                                String charity = task2.getResult().getDocuments().get(0).getData().get("charity").toString();
                                                Log.v(TAG, charity);
                                                Intent intent = new Intent(LoginActivity.this, CharityHomeActivity.class);
                                                //intent.putExtra("charity", charity);
                                                startActivity(intent);
                                            }
                                            else{
                                                Log.d(TAG, "signInWithEmail:failure");
                                                Toast.makeText(LoginActivity.this, "Authentication failed: You are not a charity admin.",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                            //setContentView(R.layout.activity_public_main);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END sign_in_with_email]
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(LoginActivity.this, HomeScreenActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.animation_enter,
                        R.anim.animation_leave);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.signInButton:
                // Do something
                if(TextUtils.isEmpty(unEdit.getText().toString()) || TextUtils.isEmpty(pwEdit.getText().toString())){
                    Toast.makeText(LoginActivity.this, "Enter username or password before submission.", Toast.LENGTH_LONG);
                }
                else {
                    signIn(unEdit.getText().toString(), pwEdit.getText().toString());
                }
                break;
            case R.id.NewUserBtn:
                String url = "https://docs.google.com/forms/d/e/1FAIpQLSdy3T6zJoR8fnnMNKWJLvcC6ScOzKO-Qf-uv2u_KukN4pjyog/viewform?usp=sf_link";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(Intent.createChooser(intent, "Browse with"));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    public void onBackPressed () {
        this.finish();
        Intent i = new Intent(LoginActivity.this, HomeScreenActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(i);
    }
}

