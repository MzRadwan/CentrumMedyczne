package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrescriptionsActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final CollectionReference patientCol = db.collection("patient");
    private final CollectionReference prescCol = db.collection("prescription");
    private final CollectionReference prescHasDrug = db.collection("prescription_has_drug");

    private List<String> issueDates, expireDates, docNames, prescDrugs;
    private PrescriptionsAdapter prescriptionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptions);

        getPrescriptions();
    }

    private void createPrescriptionsRecycler(){
        issueDates = new ArrayList<>();
        expireDates = new ArrayList<>();
        docNames = new ArrayList<>();
        prescDrugs = new ArrayList<>();

        RecyclerView prescriptionsRecycler = findViewById(R.id.prescriptionsRecycler);
        prescriptionsAdapter = new PrescriptionsAdapter(this,
                                issueDates, expireDates, docNames, prescDrugs);
        prescriptionsRecycler.setAdapter(prescriptionsAdapter);
        prescriptionsRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getPrescriptions(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            String userId = user.getUid();
            patientCol.document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DocumentReference userRef = documentSnapshot.getReference();
                        getUsersPresc(userRef);
                    }
                });
        }
    }

    private void getUsersPresc(DocumentReference userRef){
        prescCol.whereEqualTo("patient_id",userRef).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty()){
                            TextView mNoPrescriptions = findViewById(R.id.noPrescriptionsText);
                            mNoPrescriptions.setVisibility(View.VISIBLE);
                        }
                    }
                })
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    createPrescriptionsRecycler();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Date issueDate = documentSnapshot.getDate("issue_date");
                    Date expireDate = documentSnapshot.getDate("expire_after");
                    issueDates.add("Data wystawienia: " + FormatData.reformatDate(issueDate));
                    expireDates.add("Ważna do: " + FormatData.reformatDate(expireDate));
                    docNames.add("");
                    prescDrugs.add("");
                    prescriptionsAdapter.notifyDataSetChanged();
                    DocumentReference docRef = documentSnapshot.getDocumentReference("doctor_id");
                    int prescNum = issueDates.size() - 1;
                    getDocName(docRef, prescNum);
                    DocumentReference prescRef = documentSnapshot.getReference();
                    getPrescDrugs(prescRef, prescNum);
                }
                }
            });

    }

    private void getDocName(DocumentReference docRef, final int prescNum){
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Doctor doc = documentSnapshot.toObject(Doctor.class);
                docNames.set(prescNum, doc.getDegree() + " "
                        + doc.getFirst_name() + " "
                        + doc.getLast_name());
                prescriptionsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getPrescDrugs(DocumentReference prescRef, final int prescNum){
        prescHasDrug.whereEqualTo("prescription_id", prescRef).get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        DocumentReference drugRef = documentSnapshot
                                                        .getDocumentReference("drug_id");
                        getDrug(drugRef, prescNum);
                    }
                }
            });
    }

    private void getDrug(DocumentReference drugRef, final int prescNum){
        drugRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Drug drug = documentSnapshot.toObject(Drug.class);
                String allDrugs = prescDrugs.get(prescNum);
                prescDrugs.set(prescNum, allDrugs
                        + drug.getTrade_name() + "\n"
                        + drug.getActive_substance() + " " + drug.getDose()
                        + "\n" + drug.getForm() + "\n"+ "\n");
                prescriptionsAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onClickSearchPrescriptions(View view){
        Intent intent = new Intent(PrescriptionsActivity.this, StartActivity.class);
        finish();
        startActivity(intent);
    }

    public void onClickAccountPrescriptions(View view){
        Intent intent = new Intent(PrescriptionsActivity.this, PatientAccountActivity.class);
        finish();
        startActivity(intent);
    }
}