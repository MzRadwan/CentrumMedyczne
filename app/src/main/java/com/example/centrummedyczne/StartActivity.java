package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    Button mLoginButton, mSearchButton;
    String specs[], cities[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Full Screen Activity
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        specs = getResources().getStringArray(R.array.specs);
        cities = getResources().getStringArray(R.array.cities);

        ArrayAdapter<String> specsAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, specs);
        final AutoCompleteTextView mSpecs = (AutoCompleteTextView) findViewById(R.id.specsAutoComplete);
        mSpecs.setAdapter(specsAdapter);

        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, cities);
        final AutoCompleteTextView mCities = (AutoCompleteTextView) findViewById(R.id.citiesAutoComplete);
        mCities.setAdapter(citiesAdapter);


        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PreLoginActivity.class);
                startActivity(intent);
            }
        });

        mSearchButton = (Button) findViewById(R.id.docSearchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchResultActivity.class);
                String specializaion = mSpecs.getText().toString();
                intent.putExtra("specialization", specializaion);
                String city = mCities.getText().toString();
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });
    }
}