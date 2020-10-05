package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorActivity extends AppCompatActivity {

    ImageView mainImageView;
    TextView title, description;

    String data1, data2;
    int myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        mainImageView = findViewById(R.id.mainImageView);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);

        getData();
        setData();
    }

    private void getData() {
        if(getIntent().hasExtra("images")
                && getIntent().hasExtra("data1")
                && getIntent().hasExtra("data2")){
            data1 = getIntent().getStringExtra("data1");
            data2 = getIntent().getStringExtra("data2");
            myImage = getIntent().getIntExtra("images", 1);

        }
        else{
            Toast.makeText(this,"No data", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData(){
        title.setText(""+data1);
        description.setText(""+data2);
        mainImageView.setImageResource(myImage);
    }
}