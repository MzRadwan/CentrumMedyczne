package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class AppointmentConfirmActivity extends AppCompatActivity {

    private String visitId, visitDate;

    private Button mVisitDate, mConfirm, mCancel;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference appointmentCol = db.collection("appointment");
    private final CollectionReference patientCol = db.collection("patient");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_confirm);

        mVisitDate = findViewById(R.id.avaliableAppointmentConfirm);
        mCancel = findViewById(R.id.stopBookingConfirm);
        mConfirm = findViewById(R.id.confirmBookingConfirm);

        getData();
        setData();

    }

    private void getData(){
        visitId = getIntent().getStringExtra("visitId");
        visitDate = getIntent().getStringExtra("visitDate");
    }

    private void setData(){
        mVisitDate.setText(visitDate);
    }

    public void onClickConfirmVisit(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        patientCol.document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DocumentReference patientRef = documentSnapshot.getReference();
                        bookVisit(patientRef);
                    }
                });

    }

    private void bookVisit(DocumentReference patientRef){
        Map<String, Object> visit = new HashMap<>();
        visit.put("booked", true);
        visit.put("patient_id", patientRef);
        appointmentCol.document(visitId).update(visit)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AppointmentConfirmActivity.this,
                                "Zarezerwowano wizytę", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AppointmentConfirmActivity.this,
                                "Nieudana rezerwacja wizyty", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}