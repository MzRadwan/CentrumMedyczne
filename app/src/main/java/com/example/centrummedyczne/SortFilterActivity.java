package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class SortFilterActivity extends AppCompatActivity {

    Button mApply;
    Spinner mSortOption, mSortDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_filter);

        mApply = (Button) findViewById(R.id.applyButton);
        mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }
}