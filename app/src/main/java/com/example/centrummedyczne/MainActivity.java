package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference addressCol = db.collection("address");
    private CollectionReference doctorCol = db.collection("doctor");


    private static final String TAG = "MainActivity";
    TextView mText1;
    Button mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

       /* db.collection("specialization")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                mText1.setText(document.getData().toString());
                            }
                        }
                        else{
                            Log.w(TAG, "Error: ", task.getException());
                        }
                    }
                });

        */
        /*db.collection("address")
                .whereEqualTo("city","Lublin")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                mText1.setText(document.getId() + document.getData().toString());
                            }
                        }
                        else {
                            mText1.setText("Error: " + task.getException());
                        }
                    }
                });*/
    }

    public void onClickCreateDoc(View view){

        EditText mEmail = (EditText) findViewById(R.id.docEmailRegister);
        EditText mPassword = (EditText) findViewById(R.id.docPasswordRegister);
        EditText mPassRepeat = (EditText) findViewById(R.id.docRepeatPassRegister);

        String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        String repeatPass = mPassRepeat.getText().toString();

        if(email.equals("")){ // empty email input field
            Toast.makeText(MainActivity.this,
                    R.string.required_email, Toast.LENGTH_LONG).show();
        }
        else if(!Validation.validateEmail(email)){
            Toast.makeText(MainActivity.this,
                    R.string.incorrect_email, Toast.LENGTH_LONG).show();
        }
        else if(password.equals("")){ //empty password input field
            Toast.makeText(MainActivity.this,
                    R.string.empty_password, Toast.LENGTH_LONG).show();
        }
        else {
            if (!password.equals(repeatPass)){ //different passwords given
                Toast.makeText(MainActivity.this,
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
                                    createDoc(userId);

                                }
                                else {
                                    Toast.makeText(MainActivity.this,
                                            R.string.register_error, Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            }
        }
    }

    private void createDoc(final String userId){
        //create address

        EditText mCity = (EditText) findViewById(R.id.docCityRegister);
        EditText mStreet = (EditText) findViewById(R.id.docStreetRegister);
        EditText mBuilding = (EditText) findViewById(R.id.docBuildingRegister);
        EditText mApartment = (EditText) findViewById(R.id.docApartmentRegister);
        EditText mPostalCode = (EditText) findViewById(R.id.docPostalCodeRegister);

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
            address.put("postalCode", postalCode);
        else
            Toast.makeText(MainActivity.this, R.string.incorrect_postal_code, Toast.LENGTH_SHORT).show();

        addressCol.add(address)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("MainActivity", "added, ID = " + documentReference.getId());
                        //create patient
                        EditText mName = (EditText) findViewById(R.id.nameRegister);
                        EditText mSurname = (EditText) findViewById(R.id.surnameRegister);
                        EditText mPesel = (EditText) findViewById(R.id.peselRegister);
                        EditText mPhone = (EditText) findViewById(R.id.phoneRegister);

                        Map<String,Object> doctor = new HashMap<>();
                        doctor.put("blocked_to", null);
                        doctor.put("mobile", mPhone.getText().toString());
                        doctor.put("first_name", mName.getText().toString());
                        if(Validation.validatePesel(mPesel.getText().toString()))
                            doctor.put("PESEL", mPesel.getText().toString());
                        else
                            Toast.makeText(MainActivity.this, R.string.incorrect_pesel, Toast.LENGTH_SHORT).show();
                        doctor.put("last_name", mSurname.getText().toString());
                        doctor.put("address_id", documentReference);

                        doctor.put("appointment_price", 120);
                        doctor.put("average_rate", 3.45);
                        doctor.put("degree", "dr");
                        doctor.put("department_id", null);
                        doctor.put("employment_start", FieldValue.serverTimestamp());
                        doctor.put("employment_stop", null);
                        doctor.put("is_active", true);
                        String lorem = getResources().getString(R.string.lorem_ipsum);
                        doctor.put("personal_info",lorem);


                        doctorCol.document(userId)
                                .set(doctor)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("MainActivity", "DocumentSnapshot successfully written!");

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("MainActivity","Error " ,e);
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MainActivity","Error " ,e);
                    }
                });
    }
}