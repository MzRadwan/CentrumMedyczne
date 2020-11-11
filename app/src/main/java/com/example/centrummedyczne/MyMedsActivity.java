package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
       /* drugCol.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            medNames.add(documentSnapshot.getString("trade_name"));
                            medDetails.add(documentSnapshot.getString("active_substance") + " "
                                        + documentSnapshot.getString("dose"));
                            medForms.add(documentSnapshot.getString("form"));
                            myMedsAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        TextView mNoMeds = findViewById(R.id.noMyMeds);
                        mNoMeds.setVisibility(View.VISIBLE);
                    }
                });*/

    }

    private void getUsersPresc(DocumentReference userRef){
        prescCol.whereEqualTo("patient_id",userRef).get()
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
                //String allDrugs = prescDrugs.get(prescNum);
                //prescDrugs.set(prescNum, allDrugs
                  //      + drug.getTrade_name() + "\n"
                    //    + drug.getActive_substance() + " " + drug.getDose()
                      //  + "\n" + drug.getForm() + "\n"+ "\n");
                medNames.add(drug.getTrade_name());
                medDetails.add(drug.getActive_substance() + " " + drug.getDose());
                medForms.add(drug.getForm());
                myMedsAdapter.notifyDataSetChanged();
            }
        });
    }
}