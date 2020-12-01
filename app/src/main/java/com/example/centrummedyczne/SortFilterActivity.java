package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.bendik.simplerangeview.SimpleRangeView;

public class SortFilterActivity extends AppCompatActivity {


    private int averageMin, ratesMin, priceMin, opinionMin, opinionMax,
            averageMax, ratesMax, priceMax;

    private int priceMinRange, priceMaxRange, ratesMaxRange, opinionsMaxRange;
    private TextView mMaxPriceText, mMinPriceText, mMinAvgText,
            mMaxAvgText, mMinRatesText, mMaxRatesText, mMinOpinionText, mMaxOpinionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_filter);

        getData();
        setAverageFilters();
        setOpinionFilters();
        setRateFilters();
        setPriceFilters();

    }

    private void setAverageFilters(){
        SimpleRangeView mAverageBar = findViewById(R.id.averageBar);

        mMinAvgText = findViewById(R.id.avgMinRangeText);
        mMaxAvgText = findViewById(R.id.avgMaxRangeText);

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
                mMinAvgText.setText(String.valueOf(i));
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                mMaxAvgText.setText(String.valueOf(i));
            }
        });

    }
    private void setOpinionFilters(){
        SimpleRangeView mOpinionBar = findViewById(R.id.opinionBar);
        TextView mOpinionsText = findViewById(R.id.textViewOpinionsHeader);

        mMinOpinionText = findViewById(R.id.opinionMinRangeText);
        mMaxOpinionText = findViewById(R.id.opinionMaxRangeText);

        if(opinionsMaxRange == 0){
            mOpinionsText.setVisibility(View.GONE);
            mOpinionBar.setVisibility(View.GONE);
            mMinOpinionText.setVisibility(View.GONE);
            mMaxOpinionText.setVisibility(View.GONE);
        }
        else{
            mMaxOpinionText.setText(String.valueOf(opinionsMaxRange));

            mOpinionBar.setCount(opinionsMaxRange+1);
            mOpinionBar.setEnd(opinionsMaxRange + 1);

            mOpinionBar.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
                @Override
                public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                    setOpinionMin(i);
                    setOpinionMin(i1);
                }
            });
            mOpinionBar.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
                @Override
                public void onStartRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                    mMinOpinionText.setText(String.valueOf(i));
                }
                @Override
                public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                    mMaxOpinionText.setText(String.valueOf(i));
                }
            });
        }

    }

    private void setRateFilters(){
        SimpleRangeView mRatesNumberBar = findViewById(R.id.ratesBar);
        TextView mRatesText = findViewById(R.id.rateNumHeaderText);

        mMinRatesText = findViewById(R.id.ratesMinRangeText);
        mMaxRatesText = findViewById(R.id.ratesMaxRangeText);

        if(ratesMaxRange == 0){
            mRatesText.setVisibility(View.GONE);
            mRatesNumberBar.setVisibility(View.GONE);
            mMinRatesText.setVisibility(View.GONE);
            mMaxRatesText.setVisibility(View.GONE);
        }
        else{
            mMaxRatesText.setText(String.valueOf(ratesMaxRange));

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
                    mMinRatesText.setText(String.valueOf(i));
                }
                @Override
                public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                    mMaxRatesText.setText(String.valueOf(i));
                }
            });
        }
    }

    private void setPriceFilters(){
        SimpleRangeView mPriceBar = findViewById(R.id.priceBar);
        mMinPriceText = findViewById(R.id.priceMinRangeText);
        mMaxPriceText = findViewById(R.id.priceMaxRangeText);

        mMaxPriceText.setText(String.valueOf(priceMaxRange));

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
                mMinPriceText.setText(String.valueOf(i));
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                mMaxPriceText.setText(String.valueOf(i));
            }
        });

    }

    private void getData(){
       // priceMinRange = getIntent().getIntExtra("minPrice", 0);
        priceMaxRange = (int) getIntent().getFloatExtra("maxPrice", 300);
        ratesMaxRange =  getIntent().getIntExtra("maxRatesNumber", 300);
        opinionsMaxRange =  getIntent().getIntExtra("maxOpinionsNumber", 300);
    }

    public void onClickApply(View view){
        Intent applyIntent = new Intent();
        Spinner mSortOption = findViewById(R.id.sortOptionSpinner);
        Spinner mSortDirection = findViewById(R.id.sortDirectionSpinner);
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

        if(ratesMin != 0)
            applyIntent.putExtra("ratesMin", ratesMin);
        if (ratesMax != ratesMaxRange && ratesMax != 0)
            applyIntent.putExtra("ratesMax", ratesMax);

        if(opinionMin != 0)
            applyIntent.putExtra("opinionMin", opinionMin);
        if (opinionMax != opinionsMaxRange && opinionMax != 0)
            applyIntent.putExtra("opinionMax", opinionMax);

        setResult(RESULT_OK, applyIntent);
        finish();
    }

    public void setAverageMin(int averageMin) {
        this.averageMin = averageMin;
    }

    public void setAverageMax(int averageMax) {
        this.averageMax = averageMax;
    }

    public void setOpinionMin(int opinionMin) {
        this.opinionMin = opinionMin;
    }

    public void setOpinionMax(int opinionMax) {
        this.opinionMax = opinionMax;
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