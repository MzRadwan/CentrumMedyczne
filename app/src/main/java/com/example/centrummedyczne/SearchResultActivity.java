package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class SearchResultActivity extends AppCompatActivity {

    String specs[], cities[];
    AutoCompleteTextView mSpecs, mCities;

    RecyclerView mRecyclerView;

    String s1[], s2[];
    int images[] = {R.drawable.bear,R.drawable.bird,
            R.drawable.chameleon, R.drawable.dog,
            R.drawable.flamingo, R.drawable.koala,
            R.drawable.lizard, R.drawable.rabbit,
            R.drawable.sheep, R.drawable.zebra};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        //Full Screen Activity
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        mSpecs = (AutoCompleteTextView) findViewById(R.id.specializationTextView);
        mCities = (AutoCompleteTextView) findViewById(R.id.cityTextView);

        Intent intent = getIntent();
        String chosenSpec = intent.getStringExtra("specialization");
        mSpecs.setHint(chosenSpec);
        String chosenCity = intent.getStringExtra("city");
        mCities.setHint(chosenCity);

        specs = getResources().getStringArray(R.array.specs);
        cities = getResources().getStringArray(R.array.cities);

        ArrayAdapter<String> specsAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, specs);
        mSpecs.setAdapter(specsAdapter);

        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, cities);
        mCities.setAdapter(citiesAdapter);

        mRecyclerView = findViewById(R.id.docRecyler);

        s1 = getResources().getStringArray(R.array.doctors);
        s2 = getResources().getStringArray(R.array.description);

        MyAdapter myAdapter = new MyAdapter(this,s1, s2, images);
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}