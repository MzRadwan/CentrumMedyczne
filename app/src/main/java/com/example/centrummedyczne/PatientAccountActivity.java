package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class PatientAccountActivity extends AppCompatActivity {

    Button mEditPersonalButton,
            mMyPrescriptionButton,
            mFavouriteButton,
            mAppointmentButton,
            mMyMedsButton,
            mSignOutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_account);

        mAppointmentButton = (Button) findViewById(R.id.appointmentButton);
        mAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AppointmentHistoryActivity.class);
                startActivity(intent);
            }
        });

        mEditPersonalButton = (Button) findViewById(R.id.editPersonalDataButton);
        mEditPersonalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditPatientActivity.class); //TO DO
                startActivity(intent);
            }
        });

        mFavouriteButton = (Button) findViewById(R.id.favouriteDoctorsButton);
        mFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FavDocsActivity.class); //TO DO
                startActivity(intent);
            }
        });

        mMyMedsButton = (Button) findViewById(R.id.myMedicineButton);
        mMyMedsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyMedsActivity.class);
                startActivity(intent);
            }
        });

        mMyPrescriptionButton = (Button) findViewById(R.id.myPrescriptionsButton);
        mMyPrescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PrescriptionsActivity.class);
                startActivity(intent);
            }
        });

        mSignOutButton = (Button) findViewById(R.id.logOutButton);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(PatientAccountActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });
    }
}