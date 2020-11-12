package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class PatientAccountActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_account);

    }


    public void onClickVisitSearch(View view){
        Intent intent = new Intent(view.getContext(), StartActivity.class);
        startActivity(intent);
    }

    public void onClickEdit(View view){
        Intent intent = new Intent(view.getContext(), EditPatientActivity.class); //TO DO
        startActivity(intent);
    }

    public void onClickPlanned(View view){
        Intent intent = new Intent(view.getContext(), PlannedVisitsActivity.class); //TO DO
        startActivity(intent);
    }

    public void onClickFavourite(View view){
        Intent intent = new Intent(view.getContext(), FavDocsActivity.class); //TO DO
        startActivity(intent);
    }

    public void onClickAskDoctor(View view){
        Intent intent = new Intent(view.getContext(), DoctorsChatActivity.class); //TO DO
        startActivity(intent);
    }

    public void onClickAppointement(View view){
        Intent intent = new Intent(view.getContext(), AppointmentHistoryActivity.class);
        startActivity(intent);
    }

    public void onClickPrescriptions(View view){
        Intent intent = new Intent(view.getContext(), PrescriptionsActivity.class);
        startActivity(intent);
    }

    public void onClickMedicine(View view){
        Intent intent = new Intent(view.getContext(), MyMedsActivity.class);
        startActivity(intent);
    }

    public void onClickLogOut(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(PatientAccountActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }
}