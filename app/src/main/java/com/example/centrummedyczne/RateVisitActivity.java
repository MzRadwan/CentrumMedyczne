package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static com.example.centrummedyczne.R.string.filtruj;
import static com.example.centrummedyczne.R.string.opinion_added;

public class RateVisitActivity extends AppCompatActivity {

    RatingBar  mDocRateBar;
    private float rate;
    private double rateAvg, rateSum, rateCount;
    private String docName, docSpec, visitId;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reviewCol = db.collection("review");
    private final CollectionReference patient = db.collection("patient");
    private final CollectionReference appointmentCol = db.collection("appointment");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_visit);

        getIntentData();
        setIntentData();

        mDocRateBar = (RatingBar) findViewById(R.id.docRatingBar);
        mDocRateBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                setRate(rating);
            }
        });


    }

    private void getIntentData(){
        docName = getIntent().getStringExtra("docName");
        docSpec = getIntent().getStringExtra("docSpec");
        visitId = getIntent().getStringExtra("appointment_id");

    }

    private void setIntentData(){
        TextView mDoctor = findViewById(R.id.docNameRate);
        TextView mSpec = findViewById(R.id.docSpecRate);
        mDoctor.setText(docName);
        mSpec.setText(docSpec);

    }

    public void onClickPublishReview(View view){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        patient.document(user.getUid())
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    final DocumentReference userRef = documentSnapshot.getReference();
                    appointmentCol.document(visitId).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    final DocumentReference docRef = documentSnapshot.getDocumentReference("doctor_id");

                                    Map<String, Object> review = new HashMap<>();
                                    review.put("accepted", false);
                                    review.put("patient_id", userRef);
                                    review.put("rate",getRate());
                                    TextView mReview = findViewById(R.id.opinionEditText);
                                    review.put("review", mReview.getText().toString());
                                    Switch mAnon = findViewById(R.id.anonSwitch);
                                    review.put("anonymous", mAnon.isChecked());
                                    //System.out.println(documentSnapshot.getData());
                                    final DocumentReference appointmentRef = documentSnapshot.getReference();
                                    review.put("appointment_id", appointmentRef);

                                    reviewCol.add(review)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d("RateActivity", "added, ID = " + documentReference.getId());
                                                appointmentRef.update("rated", true)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(RateVisitActivity.this,
                                                                opinion_added, Toast.LENGTH_SHORT).show();
                                                        updateDocRateAvg(docRef);
                                                        Intent intent = new Intent(RateVisitActivity.this,
                                                                        AppointmentHistoryActivity.class);
                                                        finish();
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("RateActivity","Error " ,e);
                                            }
                                        });
                                }
                            });
                }
            });
    }

    private void updateDocRateAvg(final DocumentReference docRef){
        rateAvg = 0.0;
        rateSum = 0.0;
        rateCount = 0.0;
        appointmentCol.whereEqualTo("doctor_id", docRef)
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
                                rateSum += review;
                                rateCount++;
                                rateAvg = rateSum / rateCount;
                                //System.out.println(docRef.toString() + review + rateCount + rateSum + rateAvg);
                                Map<String, Object> newRate = new HashMap<>();
                                newRate.put("average_rate", rateAvg);
                                docRef.update(newRate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(RateVisitActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            }
                        });
                }
                }
            });

    }


    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void onClickAccountRateVisit(View view){
        Intent intent = new Intent(view.getContext(),PatientAccountActivity.class);
        finish();
        startActivity(intent);
    }

    public void onClickSearchRateVisit(View view){
        Intent intent = new Intent(view.getContext(),StartActivity.class);
        finish();
        startActivity(intent);
    }

    public void onClickAddLater(View view){
        Intent intent = new Intent(RateVisitActivity.this,
                AppointmentHistoryActivity.class);
        startActivity(intent);
        finish();
    }
}