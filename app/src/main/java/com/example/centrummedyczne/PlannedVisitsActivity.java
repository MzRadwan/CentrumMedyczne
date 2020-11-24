package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class PlannedVisitsActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final CollectionReference patientCol = db.collection("patient");
    private final CollectionReference appointmentCol = db.collection("appointment");
    private final CollectionReference docHasSpec = db.collection("doctor_has_specialization");

    private PlannedVisitAdapter plannedVisitAdapter;

    private List<String> appointmentDates, docNames, docSpecs, docCMs, docCMCities, visitRefs;
    private List<Integer> docImgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planned_visits);

        getPlannedVisits();
    }

    private void createPlannedRecycler(){
        appointmentDates = new ArrayList<>();
        docNames = new ArrayList<>();
        docSpecs = new ArrayList<>();
        docCMs = new ArrayList<>();
        docCMCities = new ArrayList<>();
        docImgs = new ArrayList<>();
        visitRefs = new ArrayList<>();

        RecyclerView plannedRecycler = findViewById(R.id.plannedRecycler);
        plannedVisitAdapter = new PlannedVisitAdapter(this, appointmentDates, docNames, docSpecs,
                            docCMs, docCMCities, visitRefs, docImgs);
        plannedRecycler.setAdapter(plannedVisitAdapter);
        plannedRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getPlannedVisits(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            String userId = user.getUid();
            patientCol.document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DocumentReference userRef = documentSnapshot.getReference();
                    getAppointments(userRef);
                    }
                });
        }
    }

    private void getAppointments(DocumentReference userRef){
        appointmentCol.whereEqualTo("patient_id", userRef)
            .whereEqualTo("booked", true)
            .whereEqualTo("completed", false)
            .whereEqualTo("doctor_absent", false)
            .whereEqualTo("patient_absent", false).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty()){
                            TextView mNoPlanned = findViewById(R.id.noPlanneVisits);
                            mNoPlanned.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    createPlannedRecycler();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        visitRefs.add(documentSnapshot.getId());
                        Date date = documentSnapshot.getDate("appointment_start");
                        appointmentDates.add(FormatData.reformatDateTime(date));
                        docNames.add("");
                        docSpecs.add("");
                        docCMs.add("");
                        docCMCities.add("");
                        docImgs.add(R.drawable.ic_profile_lagoon);
                        plannedVisitAdapter.notifyDataSetChanged();
                        int visitNum = visitRefs.size() - 1;
                        getDocData(documentSnapshot.getDocumentReference("doctor_id"),visitNum);
                    }
                    }
                });
    }

    private void getDocData(final DocumentReference docRef, final int visitNum){
        docRef.get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                Doctor doc = documentSnapshot.toObject(Doctor.class);
                docNames.set(visitNum,doc.getDegree() + " "
                        + doc.getFirst_name() + " "
                        + doc.getLast_name());
                plannedVisitAdapter.notifyDataSetChanged();
                getDocsSpec(docRef,visitNum);
                getDocsClinics(doc.getClinic_id(), visitNum);
                }
            });
    }

    private void getDocsSpec(DocumentReference docRef, final int visitNum){
        docHasSpec
            .whereEqualTo("doctor_id",docRef).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        final DocumentReference specRef = documentSnapshot
                                .getDocumentReference("specialization_id");
                        specRef.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String docSpec= documentSnapshot.getString("specialization_name");
                                    String allSpecs = docSpecs.get(visitNum);
                                    if(allSpecs.equals("")){
                                        allSpecs += docSpec;
                                    }
                                    else {
                                        allSpecs += ", " + docSpec;
                                    }
                                    if(allSpecs.length() > 30){
                                        for (int i = allSpecs.length(); i > 5; i--){
                                            if(allSpecs.startsWith(",", i-1)){
                                                allSpecs = allSpecs.substring(0,i) + "\n" + allSpecs.substring(i+1);
                                            }
                                        }
                                    }
                                    docSpecs.set(visitNum, allSpecs);
                                    plannedVisitAdapter.notifyDataSetChanged();                                }
                            });
                    }
                }
                }
            });
    }

    private void getDocsClinics(DocumentReference clinicRef,final int visitNum){
        clinicRef.get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                docCMs.set(visitNum, documentSnapshot.getString("clinic_name"));
                    plannedVisitAdapter.notifyDataSetChanged();               
                    DocumentReference clinicAddress = documentSnapshot.getDocumentReference("address_id");
                    clinicAddress.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Address address = documentSnapshot.toObject(Address.class);
                            /*String cmAddress = "";
                            cmAddress += address.getCity() + ", "
                                    + address.getStreet() + " " + address.getBuilding_number();
                           if (address.getApartment()!=null){
                              cmAddress += "/" + address.getApartment();
                            }
*/
                            docCMCities.set(visitNum,address.getFullAddress());
                                plannedVisitAdapter.notifyDataSetChanged();
                            }
                        });
                }
            });
    }
    public void onClickAccountPlanned(View view){
        Intent intent = new Intent(PlannedVisitsActivity.this,PatientAccountActivity.class);
        finish();
        startActivity(intent);
    }

    public void onClickSearchPlanned(View view){
        Intent intent = new Intent(PlannedVisitsActivity.this,StartActivity.class);
        finish();
        startActivity(intent);
    }

}