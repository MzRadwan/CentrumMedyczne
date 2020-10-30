package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorActivity extends AppCompatActivity {

    ImageView mainImageView;
    TextView title, description, mDocPrice, mDocInfo;
    TextView mDocRate;

    String data1, data2, name, info;
    float rate, price;
    int myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        mainImageView = findViewById(R.id.mainImageView);
        title = findViewById(R.id.title);
        description = findViewById(R.id.docSpecDocActivity);
        mDocRate = findViewById(R.id.docRateTextView);
        mDocPrice = findViewById(R.id.docPriceDocActivity);
        mDocInfo = findViewById(R.id.docInfoDocAcitivity);

        getData();
        setData();
    }

    private void getData() {
        if(getIntent().hasExtra("images")
                && getIntent().hasExtra("data1")
                && getIntent().hasExtra("data2")){
            data1 = getIntent().getStringExtra("data1");
            data2 = getIntent().getStringExtra("data2");
            name = getIntent().getStringExtra("name");
            myImage = getIntent().getIntExtra("images", 1);
            rate = getIntent().getFloatExtra("rate", 0);
            price = getIntent().getFloatExtra("price", 0);
            info = getIntent().getStringExtra("info");

        }
        else{
            Toast.makeText(this,"No data", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData(){
        title.setText(""+name);
        description.setText(""+data2);
        mDocInfo.setText(""+info);
        mDocRate.setText(" "+Float.toString(rate));
       // mDocPrice.setText(" "+Float.toString(price) + " PLN");
        mDocPrice.setText(" "+ String.format("%.2f", price) + " PLN");
        mainImageView.setImageResource(myImage);
    }

    public void onClickSearchDocActivity(View view){
        Intent intent = new Intent(DoctorActivity.this, StartActivity.class);
        startActivity(intent);
    }

    public void onClickAccountDocActivity(View view){
        Intent intent = new Intent(DoctorActivity.this, PatientAccountActivity.class);
        startActivity(intent);
    }
}