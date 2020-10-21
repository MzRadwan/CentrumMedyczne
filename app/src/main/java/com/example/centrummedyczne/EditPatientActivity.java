package com.example.centrummedyczne;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
    private CollectionReference patientCol = db.collection("patient");

    private EditText mPhoneEdit, mStreetEdit, mBuildingEdit, mApartmentEdit, mCityEdit, mPostalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);

        TextView mForgotPass = (TextView) findViewById(R.id.forgetPasswordSettings);
        mForgotPass.setPaintFlags(mForgotPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (EditPatientActivity.this, ResetPassword.class);
                startActivity(intent);
            }
        });

        mApartmentEdit = findViewById(R.id.apartmentEdit);
        mBuildingEdit = findViewById(R.id.buildingNrEdit);
        mCityEdit = findViewById(R.id.cityEdit);
        mPhoneEdit = findViewById(R.id.phoneEdit);
        mStreetEdit =  findViewById(R.id.streetEdit);
        mPostalCode = findViewById(R.id.postalCodeEdit);

        setHints();

    }

    public void onClickSearch(View view){
        Intent intent = new Intent(EditPatientActivity.this, StartActivity.class);
        startActivity(intent);
    }

    public void onClickAccount(View view){
        Intent intent = new Intent(EditPatientActivity.this, PatientAccountActivity.class);
        startActivity(intent);
    }

    public void onClickSaveNewData(View view){
        String userId = user.getUid();
        updatePhone(userId);
        updateAddress(userId, getNewAddress());
    }

    private Map<String, Object> getNewAddress(){
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
            if(postalCode.matches("\\d{2}-\\d{3}"))
                address.put("postalCode", postalCode);
            else
                Toast.makeText(EditPatientActivity.this, R.string.incorrect_postal_code, Toast.LENGTH_SHORT).show();
        }
        return address;
    }

    private void updateAddress(String userId, final Map<String, Object> address){
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
                                        Toast.makeText(EditPatientActivity.this, R.string.changes_saved, Toast.LENGTH_SHORT).show();
                                        cleanAddressInputs();
                                        setHints();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditPatientActivity.this,
                                                R.string.address_change_error, Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });
    }

    private void updatePhone(String userId){
        Map<String, Object> patient = new HashMap<>();
        String mobile = mPhoneEdit.getText().toString();
        if(mobile.length() == 9){//correct phone number length
            patient.put("mobile", mobile);
            patientCol.document(userId).update(patient)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("EditPatientActivity", "Phone number updated");
                            Toast.makeText(EditPatientActivity.this, R.string.changes_saved, Toast.LENGTH_SHORT).show();
                            mPhoneEdit.setText("");
                            setHints();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditPatientActivity.this, R.string.phone_change_error, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else if (mobile.length() > 0) {
            Toast.makeText(EditPatientActivity.this, R.string.incorrect_mobile, Toast.LENGTH_SHORT).show();
            mPhoneEdit.setText("");
        }
    }
    private void cleanAddressInputs(){
        mApartmentEdit.setText("");
        mBuildingEdit.setText("");
        mCityEdit.setText("");
        mPostalCode.setText("");
        mStreetEdit.setText("");
    }

    public void onClickReady(View view){
        Intent intent = new Intent(EditPatientActivity.this, PatientAccountActivity.class);
        startActivity(intent);
    }

    public void onClickChangePassword(View view){
        EditText mCurrentPass = findViewById(R.id.currentPassEdit);

        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(),mCurrentPass.getText().toString());

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("EditPatientActivity", "User re-authenticated.");
                            EditText mRepeatPass = findViewById(R.id.repeatPassEdit);
                            EditText mNewPass = findViewById(R.id.newPassEdit);
                            String newPass = mNewPass.getText().toString();
                            String repeatPass = mRepeatPass.getText().toString();
                            if (newPass.equals("") || repeatPass.equals("")) { //empty password inputs
                                Toast.makeText(EditPatientActivity.this,
                                        R.string.incomplete_inputs, Toast.LENGTH_SHORT).show();
                            }
                            else if (!newPass.equals(repeatPass)) {//different passwords
                                Toast.makeText(EditPatientActivity.this,
                                        R.string.different_passwords, Toast.LENGTH_SHORT).show();
                            }
                            else { //correct passwords
                                user.updatePassword(newPass)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(EditPatientActivity.this,
                                                            R.string.pass_updated, Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(EditPatientActivity.this,
                                                            R.string.pass_change_error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                        else {
                            Toast.makeText(EditPatientActivity.this,
                                    R.string.incorrect_current_password, Toast.LENGTH_SHORT).show();
                        }
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