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

    private int priceMinRange, priceMaxRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_filter);

        getData();

       // mWaitTimeBar = findViewById(R.id.waitTimeBar);
       //mOpinionsNumberBar = findViewById(R.id.opinionsBar);
       // mDistanceBar = findViewById(R.id.distanceBar);

        setAverageFilters();
        setRateFilters();
        setPriceFilters();

       /* mDistanceBar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                setDistanceMin(i);
                setDistanceMax(i1);
            }
        });
*/


       /* mOpinionsNumberBar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                setOpinionsMin(i);
                setOpinionsMax(i1);
            }
        });
*/

/*
        mWaitTimeBar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                setWaitTimeMin(i);
                setWaitTimeMax(i1);
            }
        });

*/
        mApply = (Button) findViewById(R.id.applyButton);
        mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickApply();
            }
        });
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
        priceMaxRange = (int) getIntent().getFloatExtra("maxPrice", 100);
    }

    private void onClickApply(){
        Intent applyIntent = new Intent();
        mSortOption = (Spinner) findViewById(R.id.sortOptionSpinner);
        mSortDirection =(Spinner) findViewById(R.id.sortDirectionSpinner);
        String option = mSortOption.getSelectedItem().toString();
        applyIntent.putExtra("sortOption", option);
        String direction = mSortDirection.getSelectedItem().toString();
        applyIntent.putExtra("sortDirection", direction);
        //applyIntent.putExtra("averageMin", averageMin);
       // applyIntent.putExtra("averageMax", averageMax);
        //applyIntent.putExtra("distanceMin", distanceMin);
       // applyIntent.putExtra("distanceMax", distanceMax);
        if(priceMin != 0)
            applyIntent.putExtra("priceMin", priceMin);
        if (priceMax != priceMaxRange)
            applyIntent.putExtra("priceMax", priceMax);
       // applyIntent.putExtra("opinionsMin", opinionsMin);
       // applyIntent.putExtra("opinionsMax", opinionsMax);
        //applyIntent.putExtra("ratesMin", ratesMin);
       // applyIntent.putExtra("ratesMax", ratesMax);
       // applyIntent.putExtra("waitTimeMin", waitTimeMin);
       // applyIntent.putExtra("waitTimeMax", waitTimeMax);
        setResult(RESULT_OK, applyIntent);
        finish();
    }

    public void setAverageMin(int averageMin) {
        this.averageMin = averageMin;
    }

    public void setWaitTimeMin(int waitTimeMin) {
        this.waitTimeMin = waitTimeMin;
    }

    public void setOpinionsMin(int opinionsMin) {
        this.opinionsMin = opinionsMin;
    }

    public void setRatesMin(int ratesMin) {
        this.ratesMin = ratesMin;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public void setDistanceMin(int distanceMin) {
        this.distanceMin = distanceMin;
    }

    public void setAverageMax(int averageMax) {
        this.averageMax = averageMax;
    }

    public void setWaitTimeMax(int waitTimeMax) {
        this.waitTimeMax = waitTimeMax;
    }

    public void setOpinionsMax(int opinionsMax) {
        this.opinionsMax = opinionsMax;
    }

    public void setRatesMax(int ratesMax) {
        this.ratesMax = ratesMax;
    }

    public void setPriceMax(int priceMax) {
        this.priceMax = priceMax;
    }

    public void setDistanceMax(int distanceMax) {
        this.distanceMax = distanceMax;
    }
}