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

   // Button mApply;
    private Spinner mSortOption, mSortDirection;
    private SimpleRangeView mAverageBar, mRatesNumberBar, mPriceBar;

    private int averageMin, ratesMin, priceMin,
            averageMax, ratesMax, priceMax;

    private int priceMinRange, priceMaxRange, ratesMaxRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_filter);

        getData();
        setAverageFilters();
        setRateFilters();
        setPriceFilters();

    }

    private void setAverageFilters(){
        mAverageBar = findViewById(R.id.averageBar);
        mAverageBar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                setAverageMin(i);
                setAverageMax(i1);
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
    }

    private void setRateFilters(){
        mRatesNumberBar = findViewById(R.id.ratesBar);
        mRatesNumberBar.setCount(ratesMaxRange+1);
        mRatesNumberBar.setEnd(ratesMaxRange + 1);

        mRatesNumberBar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                setRatesMin(i);
                setRatesMax(i1);
            }
        });

        mRatesNumberBar.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {

            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {

            }
        });

        mRatesNumberBar.setOnRangeLabelsListener(new SimpleRangeView.OnRangeLabelsListener() {
            @Nullable
            @Override
            public String getLabelTextForPosition(@NotNull SimpleRangeView simpleRangeView, int i, @NotNull SimpleRangeView.State state) {
                return String.valueOf(i);
            }
        });
    }

    private void setPriceFilters(){
        mPriceBar = findViewById(R.id.priceBar);

        mPriceBar.setCount(priceMaxRange+1);
        mPriceBar.setEnd(priceMaxRange+1);

        mPriceBar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                setPriceMin(i);
                setPriceMax(i1);
            }
        });

        mPriceBar.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {

            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {

            }
        });

        mPriceBar.setOnRangeLabelsListener(new SimpleRangeView.OnRangeLabelsListener() {
            @Nullable
            @Override
            public String getLabelTextForPosition(@NotNull SimpleRangeView simpleRangeView, int i, @NotNull SimpleRangeView.State state) {
                return String.valueOf(i);
            }
        });
    }

    private void getData(){
       // priceMinRange = getIntent().getIntExtra("minPrice", 0);
        priceMaxRange = (int) getIntent().getFloatExtra("maxPrice", 300);
        ratesMaxRange = (int) getIntent().getFloatExtra("maxPrice", 300);
    }

    public void onClickApply(View view){
        Intent applyIntent = new Intent();
        mSortOption =  findViewById(R.id.sortOptionSpinner);
        mSortDirection = findViewById(R.id.sortDirectionSpinner);
        String option = mSortOption.getSelectedItem().toString();
        String direction = mSortDirection.getSelectedItem().toString();
        if(!option.equals("Sortuj według") && !direction.equals("Kolejność sortowania")){
            applyIntent.putExtra("sortOption", option);
            applyIntent.putExtra("sortDirection", direction);
        }

        if(averageMin > 0)
            applyIntent.putExtra("averageMin", averageMin);
        if(averageMax != 5 && averageMax!=0)
            applyIntent.putExtra("averageMax", averageMax);

        if(priceMin != 0)
            applyIntent.putExtra("priceMin", priceMin);
        if (priceMax != priceMaxRange && priceMax != 0)
            applyIntent.putExtra("priceMax", priceMax);
        //applyIntent.putExtra("ratesMin", ratesMin);
       // applyIntent.putExtra("ratesMax", ratesMax);
        setResult(RESULT_OK, applyIntent);
        finish();
    }

    public void setAverageMin(int averageMin) {
        this.averageMin = averageMin;
    }

    public void setAverageMax(int averageMax) {
        this.averageMax = averageMax;
    }

    public void setRatesMin(int ratesMin) {
        this.ratesMin = ratesMin;
    }

    public void setRatesMax(int ratesMax) {
        this.ratesMax = ratesMax;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public void setPriceMax(int priceMax) {
        this.priceMax = priceMax;
    }


}