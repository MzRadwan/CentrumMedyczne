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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference addressCol = db.collection("address");
    private CollectionReference patientCol = db.collection("patient");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView mAlreadySignedUp = (TextView) findViewById(R.id.alreadySignedUp);
        mAlreadySignedUp.setPaintFlags(mAlreadySignedUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mAlreadySignedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    public void onClickRegister(View view){

        EditText mEmail = (EditText) findViewById(R.id.emailRegister);
        EditText mPassword = (EditText) findViewById(R.id.passwordRegister);
        EditText mPassRepeat = (EditText) findViewById(R.id.repeatPassRegister);

        String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        String repeatPass = mPassRepeat.getText().toString();

        if(email.equals("")){ // empty email input field
            Toast.makeText(RegisterActivity.this,
                    R.string.required_email, Toast.LENGTH_LONG).show();
        }
        else if(!Validation.validateEmail(email)){
            Toast.makeText(RegisterActivity.this,
                    R.string.incorrect_email, Toast.LENGTH_LONG).show();
        }
        else if(password.equals("")){ //empty password input field
            Toast.makeText(RegisterActivity.this,
                    R.string.empty_password, Toast.LENGTH_LONG).show();
        }
        else {
            if (!password.equals(repeatPass)){ //different passwords given
                Toast.makeText(RegisterActivity.this,
                        R.string.different_passwords, Toast.LENGTH_LONG).show();
            }
            else{
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    assert user != null;
                                    final String userId = user.getUid();
                                    createPatient(userId);
                                    updateUI(user);
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this,
                                            R.string.register_error, Toast.LENGTH_LONG).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        }
    }

    private void createPatient(final String userId){
        //create address

        EditText mCity = (EditText) findViewById(R.id.cityRegister);
        EditText mStreet = (EditText) findViewById(R.id.streetRegister);
        EditText mBuilding = (EditText) findViewById(R.id.buildingRegister);
        EditText mApartment = (EditText) findViewById(R.id.apartmentRegister);
        EditText mPostalCode = (EditText) findViewById(R.id.postalCodeRegister);

        Map<String, Object> address = new HashMap<>();
        address.put("city", mCity.getText().toString());
        if (mApartment.getText().toString().equals(""))
            address.put("apartment", null);
        else
            address.put("apartment", mApartment.getText().toString());
        address.put("building_number", mBuilding.getText().toString());
        address.put("street", mStreet.getText().toString());
        String postalCode =  mPostalCode.getText().toString();
        if(Validation.validatePostalCode(postalCode))
            address.put("postal_code", postalCode);
        else
            Toast.makeText(RegisterActivity.this, R.string.incorrect_postal_code, Toast.LENGTH_SHORT).show();

        addressCol.add(address)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AdminActivity", "added, ID = " + documentReference.getId());
                        //create patient
                        EditText mName = (EditText) findViewById(R.id.nameRegister);
                        EditText mSurname = (EditText) findViewById(R.id.surnameRegister);
                        EditText mPesel = (EditText) findViewById(R.id.peselRegister);
                        EditText mPhone = (EditText) findViewById(R.id.phoneRegister);

                        Map<String,Object> patient = new HashMap<>();
                        patient.put("blocked_to", null);
                        patient.put("mobile", mPhone.getText().toString());
                        patient.put("first_name", mName.getText().toString());
                        if(Validation.validatePesel(mPesel.getText().toString()))
                            patient.put("PESEL", mPesel.getText().toString());
                        else
                            Toast.makeText(RegisterActivity.this, R.string.incorrect_pesel, Toast.LENGTH_SHORT).show();
                        patient.put("last_name", mSurname.getText().toString());
                        patient.put("address_id", documentReference);

                        patientCol.document(userId)
                                .set(patient)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("RegisterActivity", "DocumentSnapshot successfully written!");
                                        Toast.makeText(RegisterActivity.this,
                                                "Utworzono nowe konto.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("RegisterActivity","Error " ,e);
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("RegisterActivity","Error " ,e);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    //Change UI according to user data.
    public void  updateUI(FirebaseUser account){
        if(account != null) {
            Toast.makeText(this, "Zalogowano poprawnie", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, PatientAccountActivity.class));
        }
    }
}