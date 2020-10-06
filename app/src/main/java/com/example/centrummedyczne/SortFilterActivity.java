package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.bendik.simplerangeview.SimpleRangeView;

public class SortFilterActivity extends AppCompatActivity {

    Button mApply;
    Spinner mSortOption, mSortDirection;
    SimpleRangeView mAverageBar, mWaitTimeBar, mOpinionsNumberBar, mRatesNumberBar, mPriceBar, mDistanceBar;

    private int averageMin, waitTimeMin, opinionsMin,
            ratesMin, priceMin, distanceMin,
            averageMax, waitTimeMax, opinionsMax,
            ratesMax, priceMax, distanceMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_filter);

        mAverageBar = findViewById(R.id.averageBar);
        mWaitTimeBar = findViewById(R.id.waitTimeBar);
        mOpinionsNumberBar = findViewById(R.id.opinionsBar);
        mRatesNumberBar = findViewById(R.id.ratesBar);
        mPriceBar = findViewById(R.id.priceBar);
        mDistanceBar = findViewById(R.id.distanceBar);

        mAverageBar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                averageMin = i;
                averageMax = i1;
            }
        });

        mAverageBar.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {

            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {

            }
        });

        mAverageBar.setOnRangeLabelsListener(new SimpleRangeView.OnRangeLabelsListener() {
            @Nullable
            @Override
            public String getLabelTextForPosition(@NotNull SimpleRangeView simpleRangeView, int i, @NotNull SimpleRangeView.State state) {
                return String.valueOf(i);
            }
        });

        mDistanceBar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                distanceMin = i;
                distanceMax = i1;
            }
        });

        mOpinionsNumberBar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                opinionsMin = i;
                opinionsMax = i1;
            }
        });

        mRatesNumberBar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                ratesMin = i;
                ratesMax = i1;
            }
        });

        mWaitTimeBar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                waitTimeMin = i;
                waitTimeMax = i1;
            }
        });


        mApply = (Button) findViewById(R.id.applyButton);
        mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickApply();
            }
        });
    }

    private void onClickApply(){
        Intent applyIntent = new Intent();
        mSortOption = (Spinner) findViewById(R.id.sortOptionSpinner);
        mSortDirection =(Spinner) findViewById(R.id.sortDirectionSpinner);
        String option = mSortOption.getSelectedItem().toString();
        applyIntent.putExtra("sortOption", option);
        String direction = mSortDirection.getSelectedItem().toString();
        applyIntent.putExtra("sortDirection", direction);
        setResult(RESULT_OK, applyIntent);
        finish();
    }
}