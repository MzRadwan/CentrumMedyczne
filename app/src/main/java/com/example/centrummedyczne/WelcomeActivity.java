package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    Button loginButton, continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        loginButton = (Button) findViewById(R.id.loginStart);
        continueButton = (Button) findViewById(R.id.guestStart);
    }

    public void onClickLogin(View view){
        Intent intent = new Intent(view.getContext(),LoginActivity.class);
        startActivity(intent);
    }

    public void onClickContinue(View view){

        Intent intent = new Intent(view.getContext(),StartActivity.class);
        startActivity(intent);
    }
}