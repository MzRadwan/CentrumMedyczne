package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentHistoryActivity extends AppCompatActivity {

    private List<String> docNames, docSpecs, appointmentDates,
            docCms, docAddresses, patientNotes, opinions;
    private List<Float> rates;
    private List<Integer> docImages;
    private List<String> visitRefs;

    private HistoryAdapter historyAdapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reviewCol = db.collection("review");
    private final CollectionReference appoinmentCol = db.collection("appointment");
    private final CollectionReference patientCol = db.collection("patient");
    private final CollectionReference docHasSpec = db.collection("doctor_has_specialization");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);


        createHistoryRecycler();
        getHistory();

    }

    private void createHistoryRecycler(){
        RecyclerView historyRecycler = findViewById(R.id.historyRecycler);

        docNames = new ArrayList<>();
        docSpecs = new ArrayList<>();
        appointmentDates = new ArrayList<>();
        docCms = new ArrayList<>();
        docAddresses = new ArrayList<>();
        patientNotes = new ArrayList<>();
        opinions = new ArrayList<>();
        rates = new ArrayList<>();
        docImages = new ArrayList<>();
        visitRefs = new ArrayList<>();

        historyAdapter = new HistoryAdapter(this, docNames, docSpecs, appointmentDates,
                docCms,docAddresses,patientNotes, opinions, rates, docImages, visitRefs);
        historyRecycler.setAdapter(historyAdapter);
        historyRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getHistory(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String userId = user.getUid();
            patientCol.document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                    DocumentReference userRef = documentSnapshot.getReference();
                    appoinmentCol.whereEqualTo("patient_id", userRef)
                            .whereEqualTo("completed", true).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                    Date date = documentSnapshot.getDate("appointment_start");
                                   // System.out.println("DDAAYYY"+FormatData.reformatDate(date));
                                    appointmentDates.add(FormatData.reformatDate(date));
                                    DocumentReference visitRef = documentSnapshot.getReference();
                                    visitRefs.add(documentSnapshot.getId());
                                    historyAdapter.notifyDataSetChanged();
                                    int visitNum = appointmentDates.size() - 1;

                                    if(documentSnapshot.getBoolean("rated")) {
                                        rates.add((float) 0);
                                        opinions.add(" ");
                                        getVisitRating(visitRef, visitNum);
                                    }

                                    else {
                                        rates.add((float) -1);
                                        opinions.add("Nie wystawiono opinii");

                                    }

                                    patientNotes.add("");
                                    docCms.add("");
                                    docAddresses.add("");
                                    docNames.add("");
                                    historyAdapter.notifyDataSetChanged();
                                    getHealthcardData(documentSnapshot
                                                .getDocumentReference("healthcard_id"), visitNum);


                                    getDocData(documentSnapshot.getDocumentReference("doctor_id"), visitNum);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                TextView mNoHistory = findViewById(R.id.noHistory);
                                mNoHistory.setVisibility(View.VISIBLE);
                                Button mNewest = findViewById(R.id.newestHistry);
                                mNewest.setVisibility(View.GONE);
                                Button mOldest = findViewById(R.id.oldestHistory);
                                mOldest.setVisibility(View.GONE);
                            }
                        });


                    }
                });
        }
    }

    private void getHealthcardData(DocumentReference cardRef, final int visitNum){
        cardRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                patientNotes.set(visitNum, documentSnapshot.getString("patient_note"));
            }
        });
    }

    private void getVisitRating(DocumentReference visitRef, final int visitNum){
        reviewCol.whereEqualTo("appointment_id", visitRef).get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        Review review = documentSnapshot.toObject(Review.class);
                        rates.set(visitNum, review.getRate());
                        System.out.println("RATE" + review.getRate());
                        opinions.set(visitNum, review.getReview());
                        historyAdapter.notifyDataSetChanged();
                    }
                }
            });
    }

    private void getDocData(final DocumentReference docRef, final int visitNum){
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       // s1.add(docRef.getId());
                        Doctor doc = documentSnapshot.toObject(Doctor.class);
                        docNames.set(visitNum,doc.getDegree() + " "
                                + doc.getFirst_name() + " "
                                + doc.getLast_name());
                        docImages.add(R.drawable.ic_profile_lagoon);


                        historyAdapter.notifyDataSetChanged();

                       // final int docNum = docNames.size() - 1;
                        getDocsSpec(docRef,visitNum);
                        getDocsClinics(doc.getClinic_id(), visitNum);

                    }
                });
    }

    private void getDocsSpec(DocumentReference docRef, final int visitNum){
        docSpecs.add("");
        historyAdapter.notifyDataSetChanged();

        docHasSpec
            .whereEqualTo("doctor_id",docRef)
            .get()
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
                                    historyAdapter.notifyDataSetChanged();
                                }
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
                docCms.set(visitNum, documentSnapshot.getString("clinic_name"));
                historyAdapter.notifyDataSetChanged();
                DocumentReference clinicAddress = documentSnapshot.getDocumentReference("address_id");
                clinicAddress.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Address address = documentSnapshot.toObject(Address.class);
                        String cmAddress = "";
                        cmAddress += address.getCity() + ", "
                                + address.getStreet() + " " + address.getBuilding_number();
                        //if (address.getApartment()!=null){
                        //  cmAddress += "/" + address.getApartment();
                        //}

                        docAddresses.set(visitNum,cmAddress);
                        historyAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
    }

    public void onClickAccountIconHistory(View view){
        Intent intent = new Intent(view.getContext(), PatientAccountActivity.class);
        startActivity(intent);
    }

    public void onClickSearchIconHistory(View view){
        Intent intent = new Intent(view.getContext(), StartActivity.class);
        startActivity(intent);
    }
}