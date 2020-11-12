package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
import java.util.List;

public class DoctorsChatActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final CollectionReference patientCol = db.collection("patient");
    private final CollectionReference appointmentCol = db.collection("appointment");
    private final CollectionReference docHasSpec = db.collection("doctor_has_specialization");

    private List<String> docIds, docNames, docSpecs, docCMs, docCMAddress;
    private List<Integer> docImgs;
    private DoctorsChatAdapter doctorsChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_chat);

        createDocChatRecycler();

        getUsersDocs();
    }

    private void createDocChatRecycler(){
        docIds = new ArrayList<>();
        docNames = new ArrayList<>();
        docSpecs = new ArrayList<>();
        docCMs = new ArrayList<>();
        docCMAddress = new ArrayList<>();
        docImgs = new ArrayList<>();
        RecyclerView docChatRecycler = findViewById(R.id.DocChatRecycler);
        doctorsChatAdapter = new DoctorsChatAdapter(this, docIds, docNames, docSpecs,
                                docCMs, docCMAddress, docImgs);
        docChatRecycler.setAdapter(doctorsChatAdapter);
        docChatRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getUsersDocs(){
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
        appointmentCol.whereEqualTo("patient_id", userRef).get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        DocumentReference doctorRef = documentSnapshot.getDocumentReference("doctor_id");
                        getDocData(doctorRef);
                    }
                }
            });
    }

    private void getDocData(final DocumentReference doctorRef){
        doctorRef.get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    final Doctor foundDoctor = documentSnapshot.toObject(Doctor.class);
                    if(!docIds.contains(doctorRef.getId())){
                        docIds.add(doctorRef.getId());
                        docNames.add(foundDoctor.getDegree() + " "
                                + foundDoctor.getFirst_name() + " "
                                + foundDoctor.getLast_name());
                        docImgs.add(R.drawable.ic_profile_lagoon);
                        docSpecs.add("");
                        docCMs.add("");
                        docCMAddress.add("");
                        doctorsChatAdapter.notifyDataSetChanged();
                        int docNum = docIds.size() - 1;
                        getDocsSpec(doctorRef, docNum);
                        getDocsClinics(foundDoctor.getClinic_id(), docNum);
                    }
                }
            });
    }

    private void getDocsSpec(DocumentReference doctorRef, final int docNum){
        docHasSpec
            .whereEqualTo("doctor_id",doctorRef)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        DocumentReference docHasSpecs = documentSnapshot
                                .getDocumentReference("specialization_id");
                        docHasSpecs.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String docSpec= documentSnapshot.getString("specialization_name");
                                String allSpecs = docSpecs.get(docNum);
                                if(allSpecs.equals("")){
                                    allSpecs += docSpec;
                                }
                                else {
                                    allSpecs += ", " + docSpec;
                                }
                                if(allSpecs.length() > 30){
                                    for (int i = allSpecs.length(); i > 5; i--){
                                        if(allSpecs.substring(i-1, i).equals(",")){
                                            allSpecs = allSpecs.substring(0,i) + "\n" + allSpecs.substring(i+1);
                                        }
                                    }
                                }
                                docSpecs.set(docNum, allSpecs);
                                    doctorsChatAdapter.notifyDataSetChanged();
                                }
                            });
                    }
                }
                }
            });
    }

    private void getDocsClinics(DocumentReference clinicRef, final int docNum){
        clinicRef.get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                docCMs.set(docNum,String.valueOf(documentSnapshot.get("clinic_name")));
                System.out.println("Clinic_name" + String.valueOf(documentSnapshot.get("clinic_name")));
                    doctorsChatAdapter.notifyDataSetChanged();
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

                        docCMAddress.set(docNum,cmAddress);
                            doctorsChatAdapter.notifyDataSetChanged();
                        }
                        });
                }
            });
    }
}