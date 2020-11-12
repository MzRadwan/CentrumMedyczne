package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DoctorMessageActivity extends AppCompatActivity {

    private TextView mDocName, mDocSpec;
    private ImageView mDocImg;

    private String docName, docSpec;
    private int docImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_message);

        mDocName = findViewById(R.id.docNameMsg);
        mDocSpec = findViewById(R.id.docSpecMsg);
        mDocImg = findViewById(R.id.docImgMsg);

        getData();
        setData();
    }

    private void getData(){
        docName = getIntent().getStringExtra("docName");
        docSpec = getIntent().getStringExtra("docSpec");
        docImg = getIntent().getIntExtra("docImg", 0);
    }

    private void setData(){
        mDocName.setText(docName);
        mDocSpec.setText(docSpec);
        mDocImg.setImageResource(docImg);
    }
}