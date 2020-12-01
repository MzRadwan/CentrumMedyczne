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

import java.util.HashMap;
import java.util.Map;

import static com.example.centrummedyczne.R.string.opinion_added;

public class RateVisitActivity extends AppCompatActivity {

    RatingBar  mDocRateBar;
    private float rate;
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
        mDoctor.setText("" + docName);
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
                                    Map<String, Object> review = new HashMap<>();
                                    review.put("accepted", false);
                                    review.put("patient_id", userRef);
                                    review.put("rate",getRate());
                                    TextView mReview = findViewById(R.id.opinionEditText);
                                    review.put("review", mReview.getText().toString());
                                    Switch mAnon = findViewById(R.id.anonSwitch);
                                    review.put("anonymous", mAnon.isChecked());
                                    System.out.println(documentSnapshot.getData());
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


    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void onClickAccount(View view){
        Intent intent = new Intent(view.getContext(),PatientAccountActivity.class);
        startActivity(intent);
    }

    public void onClickSearch(View view){
        Intent intent = new Intent(view.getContext(),StartActivity.class);
        startActivity(intent);
    }

    public void onClickAddLater(View view){
        Intent intent = new Intent(RateVisitActivity.this,
                AppointmentHistoryActivity.class);
        startActivity(intent);
    }
}