package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyMedsActivity extends AppCompatActivity {

    private List<String> medNames, medDetails, medForms;
    private MyMedsAdapter myMedsAdapter;
    private RecyclerView myMedsRecycler;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference patientCol = db.collection("patient");
    private final CollectionReference prescCol = db.collection("prescription");
    private final CollectionReference prescHasDrug = db.collection("prescription_has_drug");

    private CollectionReference drugCol = db.collection("drug");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meds);

        createMyMedsRecycler();

        findMeds();

    }
    private void createMyMedsRecycler(){
        myMedsRecycler = findViewById(R.id.myMedsRecycler);

        medNames = new ArrayList<>();
        medDetails = new ArrayList<>();
        medForms = new ArrayList<>();

        myMedsAdapter = new MyMedsAdapter(this,medNames, medDetails, medForms);
        myMedsRecycler.setAdapter(myMedsAdapter);
        myMedsRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void findMeds(){
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
                        TextView noMeds = findViewById(R.id.noMyMeds);
                        noMeds.setVisibility(View.VISIBLE);
                    }
                }
            })
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        DocumentReference prescRef = documentSnapshot.getReference();
                        getPrescDrugs(prescRef);
                    }
                }
            });
    }

    private void getPrescDrugs(DocumentReference prescRef){
        prescHasDrug.whereEqualTo("prescription_id", prescRef).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            DocumentReference drugRef = documentSnapshot
                                    .getDocumentReference("drug_id");
                            getDrug(drugRef);
                        }
                    }
                });
    }

    private void getDrug(DocumentReference drugRef){
        drugRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Drug drug = documentSnapshot.toObject(Drug.class);
                medNames.add(drug.getTrade_name());
                medDetails.add(drug.getActive_substance() + " " + drug.getDose());
                medForms.add(drug.getForm());
                myMedsAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onClickSearchIconMyMeds(View view){
        Intent intent = new Intent(MyMedsActivity.this, StartActivity.class);
        finish();
        startActivity(intent);
    }

    public void onClickAccountIconMyMeds(View view){
        Intent intent = new Intent(MyMedsActivity.this, PatientAccountActivity.class);
        finish();
        startActivity(intent);
    }
}