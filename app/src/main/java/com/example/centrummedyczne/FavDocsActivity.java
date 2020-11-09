package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
import java.util.List;

public class FavDocsActivity extends AppCompatActivity {


    private RecyclerView mFavDocRecycler;

    private List<String> s1, s2, docNames, docInfos, docCMs, docCities, docReviews;
    private List<Boolean> favourites;
    private List<Float> docRates, docPrices;
    private SearchRecyclerAdapter searchRecyclerAdapter;
    private List<Integer> images, opinionCounters, rateCounters;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference specializations = db.collection("specialization");
    private final CollectionReference docHasSpec = db.collection("doctor_has_specialization");
    private final CollectionReference doctors = db.collection("doctor");
    private final CollectionReference clinics = db.collection("clinic");
    private final CollectionReference address = db.collection("address");
    private final CollectionReference appointments = db.collection("appointment");
    private final CollectionReference favouriteCol = db.collection("favourite");
    private final CollectionReference patients = db.collection("patient");
    private final CollectionReference reviews = db.collection("review");

   /* String s1[], s2[];
    int images[] = {R.drawable.ic_profile_blue,R.drawable.ic_profile_blue,
            R.drawable.ic_profile_lagoon, R.drawable.ic_profile_lagoon,
            R.drawable.ic_profile_blue, R.drawable.ic_profile_lagoon,
            R.drawable.ic_profile_lagoon, R.drawable.ic_profile_blue,
            R.drawable.ic_profile_blue, R.drawable.ic_profile_blue};*/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_docs);




        mFavDocRecycler = (RecyclerView) findViewById(R.id.favDocRecycler);

        /*s1 = getResources().getStringArray(R.array.doctors);
        s2 = getResources().getStringArray(R.array.description);

        FavDocAdapter favDocAdapter = new FavDocAdapter(this, s1, s2, images);
        mFavDocRecycler.setAdapter(favDocAdapter);
        mFavDocRecycler.setLayoutManager(new LinearLayoutManager(this));*/

        s1 = new ArrayList<>();
        s2 = new ArrayList<>();
        images = new ArrayList<>();
        docRates = new ArrayList<>();
        docPrices = new ArrayList<>();
        docNames = new ArrayList<>();
        docInfos = new ArrayList<>();
        docCMs = new ArrayList<>();
        docCities  = new ArrayList<>();
        favourites = new ArrayList<>();
        opinionCounters = new ArrayList<>();
        rateCounters = new ArrayList<>();
        docReviews = new ArrayList<>();

        searchRecyclerAdapter = new SearchRecyclerAdapter(this,s1, s2,
                images, docRates, docPrices, docNames, docInfos, docCMs,
                docCities, favourites, opinionCounters, rateCounters, docReviews);
        mFavDocRecycler.setAdapter(searchRecyclerAdapter);
        mFavDocRecycler.setLayoutManager(new LinearLayoutManager(this));

        findFavDocs();

    }

    private void findFavDocs(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String userId = user.getUid();

            patients.document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DocumentReference userRef = documentSnapshot.getReference();
                        favouriteCol.whereEqualTo("patient_id", userRef).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                        DocumentReference docRef = documentSnapshot.getDocumentReference("doctor_id");
                                        getDocData(docRef);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {//user has no fav docs
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                    }
                });
        }
    }

    private void getDocData(final DocumentReference docRef){
        docRef.get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    s1.add(docRef.getId());
                    Doctor doc = documentSnapshot.toObject(Doctor.class);
                    docNames.add(doc.getDegree() + " "
                            + doc.getFirst_name() + " "
                            + doc.getLast_name());
                    images.add(R.drawable.ic_profile_lagoon);
                    docRates.add(doc.getAverage_rate());
                    docPrices.add(doc.getAppointment_price());
                    docInfos.add(doc.getPersonal_info());
                    favourites.add(true);
                    searchRecyclerAdapter.notifyDataSetChanged();

                    final int docNum = s1.size() - 1;
                    getDocsSpec(docRef,docNum);
                    getDocsClinics(doc.getClinic_id());
                    getDocsReviews(docRef, docNum);
                }
            });
    }

    private void getDocsSpec(DocumentReference docRef, final int docNum){
        s2.add("");
        searchRecyclerAdapter.notifyDataSetChanged();

        docHasSpec
            .whereEqualTo("doctor_id",docRef)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        DocumentReference docSpecs = documentSnapshot
                                .getDocumentReference("specialization_id");
                        docSpecs.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String docSpec= documentSnapshot.getString("specialization_name");
                                String allSpecs = s2.get(docNum);
                                if(allSpecs.equals("")){
                                    allSpecs += docSpec;
                                }
                                else {
                                    allSpecs += ", " + docSpec;
                                }
                                if(allSpecs.length() > 30){
                                    for (int i = allSpecs.length(); i > 5; i--){
                                        if(allSpecs.substring(i-1, i).equals(",")){
                                            allSpecs = allSpecs.substring(0,i) + "\n" + allSpecs.substring(i+1, allSpecs.length());
                                        }
                                    }
                                }
                                s2.set(docNum, allSpecs);
                                searchRecyclerAdapter.notifyDataSetChanged();
                                }
                            });
                    }
                }
                }
            });
    }

    private void getDocsClinics(DocumentReference clinicRef){
        clinicRef.get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                docCMs.add(String.valueOf(documentSnapshot.get("clinic_name")));
                System.out.println("Clinic_name" + String.valueOf(documentSnapshot.get("clinic_name")));
                searchRecyclerAdapter.notifyDataSetChanged();
                DocumentReference clinicAddress = documentSnapshot.getDocumentReference("address_id");
                clinicAddress.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Address address = documentSnapshot.toObject(Address.class);
                      //  System.out.println(address.getCity() + address.getStreet() + address.getBuilding_number() + address.getApartment());
                        String cmAddress = "";
                        cmAddress += address.getCity() + ", "
                                + address.getStreet() + " " + address.getBuilding_number();
                        //if (address.getApartment()!=null){
                        //  cmAddress += "/" + address.getApartment();
                        //}

                        docCities.add(cmAddress);
                        searchRecyclerAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
    }

    private void getDocsReviews(DocumentReference docRef, final int docNum){
        //opinions
        opinionCounters.add(0);
        rateCounters.add(0);
        docReviews.add("");
        searchRecyclerAdapter.notifyDataSetChanged();

        appointments.whereEqualTo("doctor_id", docRef)
                .whereEqualTo("rated", true).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int count = 0;
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            count++;
                            System.out.println(docNum);
                            System.out.println(documentSnapshot.getId() + "->" +documentSnapshot.getData());
                            DocumentReference appointmentRef = documentSnapshot.getReference();
                            reviews.whereEqualTo("appointment_id", appointmentRef).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                                if(documentSnapshot.get("accepted").equals(true)){
                                                    System.out.println(documentSnapshot.get("review"));
                                                    String text = documentSnapshot.getString("review");
                                                    String author = "";
                                                    if(documentSnapshot.get("anonymous").equals(true)){
                                                        author = "Anonimowa";
                                                    }
                                                    else {
                                                        author = "Pacjent";
                                                    }
                                                    String review = docReviews.get(docNum);
                                                    if (review.equals("")){
                                                        review += author + "\n" + text;
                                                    }
                                                    else {
                                                        review += "\n" + "\n" + author + "\n" + text;
                                                    }
                                                    docReviews.set(docNum, review);
                                                    searchRecyclerAdapter.notifyDataSetChanged();
                                                }
                                            }

                                        }
                                    });

                        }
                        opinionCounters.set(docNum, count);
                        rateCounters.set(docNum, count);
                        searchRecyclerAdapter.notifyDataSetChanged();
                    }
                });
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