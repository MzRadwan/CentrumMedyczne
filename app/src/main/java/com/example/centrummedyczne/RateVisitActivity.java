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

public class RateVisitActivity extends AppCompatActivity {

    RatingBar  mDocRateBar;
    private float rate;

    private  FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  CollectionReference reviewCol = db.collection("review");
    private CollectionReference patient = db.collection("patient");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_visit);

        mDocRateBar = (RatingBar) findViewById(R.id.docRatingBar);
        mDocRateBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                setRate(rating);
            }
        });


    }

    public void onClickPublishReview(View view){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        patient.document(user.getUid())
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DocumentReference userRef = documentSnapshot.getReference();
                    Map<String, Object> review = new HashMap<>();
                    review.put("accepted", false);
                    review.put("appointment_id", null);
                    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    review.put("patient_id", userRef);
                    review.put("rate",getRate());
                    TextView mReview = findViewById(R.id.opinionEditText);
                    review.put("review", mReview.getText().toString());
                    Switch mAnon = findViewById(R.id.anonSwitch);
                    review.put("anonymous", mAnon.isChecked());

                    reviewCol.add(review)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("RateActivity", "added, ID = " + documentReference.getId());
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
}