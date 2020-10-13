package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

public class RateVisitActivity extends AppCompatActivity {

    RatingBar  mDocRateBar;
    private float rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_visit);

        mDocRateBar = (RatingBar) findViewById(R.id.docRatingBar);
        mDocRateBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                setRate(rating);
            }
        });


    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void onClickAccount(View view){
        Intent intent = new Intent(view.getContext(),PatientAccountActivity.class);
        startActivity(intent);
    }

    public void onClickSearch(View view){
        Intent intent = new Intent(view.getContext(),StartActivity.class);
        startActivity(intent);
    }
}