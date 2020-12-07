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
import java.util.List;

public class FavDocsActivity extends AppCompatActivity {


    private List<String> s1, s2, docNames, images, docInfos, docCMs, docCities, docReviews;
    private List<Boolean> favourites;
    private List<Float> docRates, docPrices;
    private SearchRecyclerAdapter searchRecyclerAdapter;
    private List<Integer>  opinionCounters, rateCounters;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference docHasSpec = db.collection("doctor_has_specialization");

    private final CollectionReference appointments = db.collection("appointment");
    private final CollectionReference favouriteCol = db.collection("favourite");
    private final CollectionReference patients = db.collection("patient");
    private final CollectionReference reviews = db.collection("review");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_docs);

        createFavoriteRecycler();

        findFavDocs();

    }

    private void createFavoriteRecycler(){
        RecyclerView mFavDocRecycler = (RecyclerView) findViewById(R.id.favDocRecycler);

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
                docCities, favourites,  rateCounters, docReviews, opinionCounters);
        mFavDocRecycler.setAdapter(searchRecyclerAdapter);
        mFavDocRecycler.setLayoutManager(new LinearLayoutManager(this));
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
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {//user has no fav docs
                                    if (task.getResult().isEmpty()){
                                        TextView mNoFavDocs = findViewById(R.id.noFavDocsFound);
                                        mNoFavDocs.setVisibility(View.VISIBLE);
                                    }
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                        DocumentReference docRef = documentSnapshot.getDocumentReference("doctor_id");
                                        getDocData(docRef);
                                    }
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
                     final Doctor doc = documentSnapshot.toObject(Doctor.class);
                    docNames.add(doc.getDegree() + " "
                            + doc.getFirst_name() + " "
                            + doc.getLast_name());
                    images.add(doc.getPhoto_url());
                    docRates.add(doc.getAverage_rate());
                    docPrices.add(doc.getAppointment_price());
                    docInfos.add(doc.getPersonal_info());
                    favourites.add(true);

                    final int docNum = s1.size() - 1;
                    docCMs.add("");
                    docCities.add("");
                    opinionCounters.add(0);
                    rateCounters.add(0);
                    docReviews.add("");
                    searchRecyclerAdapter.notifyDataSetChanged();
                    getDocsSpec(docRef,docNum);
                    getDocsClinics(doc.getClinic_id(), docNum);
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
                                if(allSpecs.equals("")) allSpecs += docSpec;
                                else allSpecs += ", " + docSpec;
                                s2.set(docNum, allSpecs);
                                searchRecyclerAdapter.notifyDataSetChanged();
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
               // System.out.println("Clinic_name" + String.valueOf(documentSnapshot.get("clinic_name")));
                searchRecyclerAdapter.notifyDataSetChanged();
                DocumentReference clinicAddress = documentSnapshot.getDocumentReference("address_id");
                clinicAddress.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Address address = documentSnapshot.toObject(Address.class);
                        docCities.set(docNum,address.getFullAddress());
                        searchRecyclerAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
    }

    private void getDocsReviews(DocumentReference docRef, final int docNum){
        //opinions
                appointments.whereEqualTo("doctor_id", docRef)
            .whereEqualTo("rated", true).get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    DocumentReference appointmentRef = documentSnapshot.getReference();
                    reviews.whereEqualTo("appointment_id", appointmentRef).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                int rateCount = rateCounters.get(docNum);
                                rateCount++;
                                rateCounters.set(docNum, rateCount);
                                searchRecyclerAdapter.notifyDataSetChanged();
                                if (documentSnapshot.getString("review") != null && !documentSnapshot.getString("review").trim().equals("")
                                && documentSnapshot.getBoolean("accepted").equals(true)){
                                    int opinionCount = opinionCounters.get(docNum);
                                    opinionCount++;
                                    opinionCounters.set(docNum, opinionCount);
                                    String review = docReviews.get(docNum);
                                    if (!review.equals("")) review += "\n\n";
                                    review += documentSnapshot.getString("review");
                                    docReviews.set(docNum, review);
                                    searchRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                            }
                        });
                }
                }
            });
    }


    public void onClickAccountFav(View view){
        Intent intent = new Intent(view.getContext(),PatientAccountActivity.class);
        finish();
        startActivity(intent);
    }

    public void onClickSearchFav(View view){
        Intent intent = new Intent(view.getContext(),StartActivity.class);
        finish();
        startActivity(intent);
    }


}