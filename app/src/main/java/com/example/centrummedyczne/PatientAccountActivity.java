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
            mSignOutButton,
            mPlannedButton,
            mAskButton,
            mVisitButtton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_account);

        setButtons();

    }

    private void setButtons(){
        mPlannedButton = (Button) findViewById(R.id.planedVisitsButton);
        mVisitButtton =(Button) findViewById(R.id.visitSearch);
        mAppointmentButton = (Button) findViewById(R.id.appointmentButton);
        mEditPersonalButton = (Button) findViewById(R.id.editPersonalDataButton);
        mFavouriteButton = (Button) findViewById(R.id.favouriteDoctorsButton);
        mMyMedsButton = (Button) findViewById(R.id.myMedicineButton);
        mMyPrescriptionButton = (Button) findViewById(R.id.myPrescriptionsButton);
        mSignOutButton = (Button) findViewById(R.id.logOutButton);
        mAskButton = (Button) findViewById(R.id.askDoctorButton);
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
        Intent intent = new Intent(view.getContext(), DoctorsChat.class); //TO DO
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