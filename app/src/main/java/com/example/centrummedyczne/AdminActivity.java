package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clinics = db.collection("clinic");
    private CollectionReference addressCol = db.collection("address");
    private CollectionReference drugs = db.collection("drug");
    private CollectionReference appointments = db.collection("appointment");
    private CollectionReference docHasSpec = db.collection("doctor_has_specialization");
    private CollectionReference healthcardCol = db.collection("healthcard");
    private CollectionReference favouriteCol = db.collection("favourite");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

    }

    public void onClickCreateDoc(View view){
        Intent intent = new Intent(AdminActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickCreateEmptySpec(View view){
        Map<String, Object> dhs = new HashMap<>();
        dhs.put("doctor_id", null);
        dhs.put("specialization_id", null);
        docHasSpec.add(dhs)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AdminActivity", "added, ID = " + documentReference.getId());
                    }
              })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("AdminActivity","Error " ,e);
                }
            });
    }

    public void onClickCreateEmptyFav(View view){
        Map<String, Object> fav = new HashMap<>();
        fav.put("doctor_id", null);
        fav.put("patient_id", null);
        favouriteCol.add(fav)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AdminActivity", "added, ID = " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AdminActivity","Error " ,e);
                    }
                });
    }

    public void onAddClinic(View view){

        EditText mClinicName = (EditText) findViewById(R.id.clinicName);
        EditText mClinicPhone = (EditText) findViewById(R.id.clinicPhone);
        EditText mClinicEmail = (EditText) findViewById(R.id.clinicEmail);

        Map<String, Object> clinic = new HashMap<>();

        String name = mClinicName.getText().toString();
        String email = mClinicEmail.getText().toString();
        String phone = mClinicPhone.getText().toString();

        clinic.put("address_id", null);
        clinic.put("clinic_name", name);
        clinic.put("clinic_email", email);
        clinic.put("clinic_phone", phone);

        clinics.add(clinic)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AdminActivity", "added, ID = " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AdminActivity","Error " ,e);
                    }
                });
    }

    public void onClickAddAddress(View view){

        EditText mCityAddress = (EditText) findViewById(R.id.cityAddress);
        EditText mStreetAddress = (EditText) findViewById(R.id.streetAddress);
        EditText mBuildingNr = (EditText) findViewById(R.id.buildingNrAddress);
        EditText mPostalCode = (EditText) findViewById(R.id.postalCodeAddress);
        EditText mApartment = (EditText) findViewById(R.id.apartmentAddress);

        Map<String, Object> address = new HashMap<>();
        address.put("city", mCityAddress.getText().toString());
        address.put("apartment", mApartment.getText().toString());
        address.put("building_number", mBuildingNr.getText().toString());
        address.put("street", mStreetAddress.getText().toString());
        address.put("postal_code", mPostalCode.getText().toString());

        addressCol.add(address)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AdminActivity", "added, ID = " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AdminActivity","Error " ,e);
                    }
                });
    }

    public void onClickAddDrug(View view){
        EditText mTradeName = findViewById(R.id.tradeNameDrug);
        EditText mActiveSubstance = findViewById(R.id.activeSubstanceDrug);

        Map<String,Object> drug = new HashMap<>();
        drug.put("trade_name", mTradeName.getText().toString());
        drug.put("active_substance", mActiveSubstance.getText().toString());

        drugs.add(drug)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AdminActivity", "added, ID = " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AdminActivity","Error " ,e);
                    }
                });
    }

    public void onClickAddHealthCard(View view){
        Map< String, Object> healthcard = new HashMap<>();
        healthcard.put("patient_note", "Lorem ipsum...");
        healthcard.put("treatment", "Lorem ipsum...");
        healthcard.put("time_created", FieldValue.serverTimestamp());
        EditText mSymptoms = findViewById(R.id.symptomsHealthcard);
        healthcard.put("symptoms", mSymptoms.getText().toString());

        healthcardCol.add(healthcard)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AdminActivity", "added, ID = " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AdminActivity","Error " ,e);
                    }
                });

    }

    public void onClickAddAppointment(View view){
        EditText mPrice = findViewById(R.id.priceAppointment);
        Map< String, Object> appointment = new HashMap<>();
        appointment.put("price", mPrice.getText().toString());
        appointment.put("booked", false);
        appointment.put("completed", false);
        appointment.put("doctor_absent", false);
        appointment.put("patient_absent", false);
        appointment.put("notification-sent", false);
        appointment.put("appointment_start", null);
        appointment.put("appointment_stop", null);
        appointment.put("clinic_id", null);
        appointment.put("doctor_id", null);
        appointment.put("healthcard_id", null);
        appointment.put("patient_id", null);
        appointments.add(appointment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AdminActivity", "added, ID = " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AdminActivity","Error " ,e);
                    }
                });



    }
}