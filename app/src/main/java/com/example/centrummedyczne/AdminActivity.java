package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference clinics = db.collection("clinic");
    private final CollectionReference addressCol = db.collection("address");
    private final CollectionReference drugs = db.collection("drug");
    private final CollectionReference appointments = db.collection("appointment");
    private final CollectionReference docHasSpec = db.collection("doctor_has_specialization");
    private final CollectionReference healthcardCol = db.collection("healthcard");
    private final CollectionReference favouriteCol = db.collection("favourite");
    private final CollectionReference prescriptionCol = db.collection("prescription");
    private final CollectionReference prescHasDrug = db.collection("prescription_has_drug");
    private final CollectionReference reviewCol = db.collection("review");
    private final CollectionReference doctorCol = db.collection("doctor");
    private final CollectionReference patientCol = db.collection("patient");

    private List<Double>  docRatesSum, docRatesCount;
    private List<Double>  docRatesAvg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        createNotificationChannel();


    }

    public void onClickGetPhoto(View view){
        final ImageView mTestPhoto = findViewById(R.id.testPhotoImageView);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference s = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/centrum-medyczne-8367d.appspot.com/o/doctors%2F1604259130016.jpeg?alt=media&token=1a33c800-72cf-4288-b687-6e99496d4e3f");
        //StorageReference imageRef = storage.getReference().child("doctors").child("1604256112092.jpg");
        s.getBytes(1024 * 1024)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        mTestPhoto.setImageBitmap(bitmap);
                    }
                });
    }

    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("My notification",
                    "My notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    private void createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AdminActivity.this,
                "My notification");
        builder.setContentTitle("Centrum Medyczne");
        builder.setContentText("Powiadomienie");
        builder.setAutoCancel(false);
        builder.setSmallIcon(R.drawable.ic_launcher_custom_background);
        NotificationManagerCompat manager = NotificationManagerCompat.from(AdminActivity.this);
        manager.notify(1,builder.build());
    }

    public void onClickNotification(View view){
        createNotification();
    }

    public void onClickAddPrescription(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        patientCol.document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final DocumentReference userRef = documentSnapshot.getReference();
                        Map<String, Object> presc = new HashMap<>();
                        presc.put("issue_date", FieldValue.serverTimestamp());
                        presc.put("expire_after", FieldValue.serverTimestamp());
                        presc.put("patient_id", userRef);
                        presc.put("doctor_id", null);
                        prescriptionCol.add(presc)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    DocumentReference prescRef = documentReference;
                                    Map<String, Object> phd = new HashMap<>();
                                    phd.put("drug_id", null);
                                    phd.put("prescription_id", prescRef);
                                    prescHasDrug.add(phd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                        }
                                    });
                                    prescHasDrug.add(phd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                        }
                                    });

                                }
                            });
                    }
                });
    }

    public void onClickAddReview(View view){
        Intent intent = new Intent(AdminActivity.this, RateVisitActivity.class);
        startActivity(intent);
    }



    public void onClickUpdateReviews(View view){
        docRatesAvg = new ArrayList<>();
        docRatesSum = new ArrayList<>();
        docRatesCount = new ArrayList<>();
    doctorCol.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                docRatesAvg.add( 0.0);
                docRatesSum.add(0.0);
                docRatesCount.add(0.0);
                final int docNum = docRatesAvg.size() -1;
                final DocumentReference docRef = documentSnapshot.getReference();
                appointments.whereEqualTo("doctor_id", docRef)
                    .whereEqualTo("rated", true).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (final QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots){
                            DocumentReference visitRef = documentSnapshot1.getReference();
                            reviewCol.whereEqualTo("appointment_id", visitRef).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (QueryDocumentSnapshot documentSnapshot2 : queryDocumentSnapshots){
                                                Double review = documentSnapshot2.getDouble("rate");
                                                Double sum = docRatesSum.get(docNum);
                                                sum += review;
                                                docRatesSum.set(docNum, sum);
                                                Double count = docRatesCount.get(docNum);
                                                docRatesCount.set(docNum, ++count);
                                                Double avg = sum / count;
                                                docRatesAvg.set(docNum, avg);
                                                System.out.println(docRef.toString() + review + count + sum + avg);
                                                Map<String, Object> newRate = new HashMap<>();
                                                newRate.put("average_rate", avg);
                                                docRef.update(newRate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(AdminActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                            }
                        }
                    });
            }
        }
    });


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
        appointment.put("notification_sent", false);
        appointment.put("appointment_start", null);
        appointment.put("appointment_stop", null);
        appointment.put("clinic_id", null);
        appointment.put("doctor_id", null);
        appointment.put("healthcard_id", null);
        appointment.put("patient_id", null);
        appointment.put("rated", false);
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