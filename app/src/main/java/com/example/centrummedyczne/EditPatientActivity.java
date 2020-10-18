package com.example.centrummedyczne;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class EditPatientActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
   // private CollectionReference addressCol = db.collection("address");
    private CollectionReference patientCol = db.collection("patient");

    private EditText mPhoneEdit, mStreetEdit, mBuildingEdit, mApartmentEdit, mCityEdit, mPostalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);

        mApartmentEdit = (EditText) findViewById(R.id.apartmentEdit);
        mBuildingEdit = (EditText) findViewById(R.id.buildingNrEdit);
        mCityEdit = (EditText) findViewById(R.id.cityEdit);
        mPhoneEdit = (EditText) findViewById(R.id.phoneEdit);
        mStreetEdit = (EditText) findViewById(R.id.streetEdit);
        mPostalCode = (EditText) findViewById(R.id.postalCodeEdit);

        setHints();

    }

    public void onClickSearch(View view){
        Intent intent = new Intent(view.getContext(), StartActivity.class);
        startActivity(intent);
    }

    public void onClickAccount(View view){
        Intent intent = new Intent(view.getContext(), PatientAccountActivity.class);
        startActivity(intent);
    }

    public void onClickSaveNewData(View view){
        String userId = user.getUid();

        Map<String, Object> patient = new HashMap<>();
        String mobile = mPhoneEdit.getText().toString();
        if(mobile.length() == 9){//correct phone number length
            patient.put("mobile", mobile);
            patientCol.document(userId).update(patient)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("EditPatientActivity", "Phone number updated");
                            Toast.makeText(EditPatientActivity.this, "Zapisano zmiany.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditPatientActivity.this, "Błąd zmiany telefonu!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            Toast.makeText(EditPatientActivity.this, "Niepoprawny numer telefonu!", Toast.LENGTH_SHORT).show();
        }

        final Map<String, Object> address = new HashMap<>();
        String street = mStreetEdit.getText().toString();
        if(!street.equals("")){
            address.put("street", street);
        }
        String apartment = mApartmentEdit.getText().toString();
        if(!apartment.equals("")){
            address.put("apartment", apartment);
        }
        String building = mBuildingEdit.getText().toString();
        if(!building.equals("")){
            address.put("building_number", building);
        }
        String city = mCityEdit.getText().toString();
        if(!city.equals("")){
            address.put("city", city);
        }
        String postalCode = mPostalCode.getText().toString();
        if(!postalCode.equals("")){
            address.put("postalCode", postalCode);
        }

        patientCol.document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Patient patient = documentSnapshot.toObject(Patient.class);
                        assert patient != null;
                        final DocumentReference addressId = patient.getAddress_id();
                        addressId.update(address)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EditPatientActivity.this, "Zapisano zmiany.", Toast.LENGTH_SHORT).show();
                                }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditPatientActivity.this,
                                        "Błąd zmiany adresu!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

    }

    private void setHints(){
        String userId = user.getUid();
        patientCol.document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Patient patient = documentSnapshot.toObject(Patient.class);
                        assert patient != null;
                        mPhoneEdit.setHint(patient.getMobile());
                        final DocumentReference addressId = patient.getAddress_id();
                        addressId.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Address address = documentSnapshot.toObject(Address.class);
                                        assert address != null;
                                        mCityEdit.setHint(address.getCity());
                                        mApartmentEdit.setHint(address.getApartment());
                                        mBuildingEdit.setHint(address.getBuilding_number());
                                        mStreetEdit.setHint(address.getStreet());
                                        mPostalCode.setHint(address.getPostal_code());
                                    }
                                });

                    }
                });

    }
}