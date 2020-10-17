package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clinics = db.collection("clinic");
    private CollectionReference addressCol = db.collection("address");
    private CollectionReference drugs = db.collection("drug");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

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
        EditText mTradeName = (EditText) findViewById(R.id.tradeNameDrug);
        EditText mActiveSubstance = (EditText) findViewById(R.id.activeSubstanceDrug);

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
}