package com.example.auroracharities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.InputStream;
import java.util.Map;

public class IndividualCharityPageActivity extends AppCompatActivity {
    String charityDocID;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "IndividualCharityPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_charity_page);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

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
                    new DownloadImageFromInternet((ImageView)findViewById(R.id.ind_charityLogo)).execute((String)docData.get("logo"));

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
                this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
            //Toast.makeText(DownloadImageFromInternet.this, "Please wait, it may take a few minute...",Toast.LENGTH_SHORT).show();
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
