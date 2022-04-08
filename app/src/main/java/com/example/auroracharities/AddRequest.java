package com.example.auroracharities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class AddRequest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
    }

    Spinner spinnerLanguages=findViewById(R.id.spinner_categories);
    ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);


}