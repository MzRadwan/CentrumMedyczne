package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private AutoCompleteTextView mSpecs, mCities;
    private String chosenCity, chosenSpec;

    private List<String> specs, cities;
    private ArrayAdapter <String> specsAdapter,citiesAdapter;

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


    private RecyclerView mRecyclerView;

    private List<String> s1, s2, docNames, docInfos, docCMs, docCities, docReviews;
    private List<Boolean> favourites;
    private List<Float> docRates, docPrices;
    private SearchRecyclerAdapter searchRecyclerAdapter;
    private List<Integer> images, opinionCounters, rateCounters;

    /*,
            R.drawable.ic_profile_lagoon, R.drawable.ic_profile_lagoon,
            R.drawable.ic_profile_blue, R.drawable.ic_profile_lagoon,
            R.drawable.ic_profile_lagoon, R.drawable.ic_profile_blue,
            R.drawable.ic_profile_blue, R.drawable.ic_profile_blue};*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        isUserLogged();

        mSpecs = (AutoCompleteTextView) findViewById(R.id.specializationTextView);
        mCities = (AutoCompleteTextView) findViewById(R.id.cityTextView);

        Intent intent = getIntent();
        chosenSpec = intent.getStringExtra("specialization");
        mSpecs.setHint(chosenSpec);
        chosenCity = intent.getStringExtra("city");
        mCities.setHint(chosenCity);

        mSpecs = (AutoCompleteTextView) findViewById(R.id.specializationTextView);
        specs = new ArrayList<>();
        specsAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, specs);
        mSpecs.setAdapter(specsAdapter);

        cities = new ArrayList<>();
        citiesAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, cities);
        mCities.setAdapter(citiesAdapter);



        //search text views settings
        setSpecHints();
        setCityHints();

        //search results display

        mRecyclerView = findViewById(R.id.docRecyler);

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
        mRecyclerView.setAdapter(searchRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //search results
        if(!chosenSpec.equals("Dowolna"))
            docSearch();
        else
            citySearch();
    }

    // spec hints from Firestore
    private void setSpecHints(){
        specializations.get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Specialization specialization = documentSnapshot.toObject(Specialization.class);
                    String s_name = specialization.getSpecialization_name();
                    specs.add(s_name);
                    specsAdapter.notifyDataSetChanged();
                }
                }
            });
    }

    private void setCityHints(){
        //cities hint from Firestore
        clinics.get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {
                    for (final QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        Clinic clinic = documentSnapshot.toObject(Clinic.class);
                        DocumentReference addressRef = clinic.getAddress_id();
                        String addressId = addressRef.getPath().substring(8);
                        address.document(addressId);
                        addressRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Address address = documentSnapshot.toObject(Address.class);
                                String city = address.getCity();
                                if(!cities.contains(city)){
                                    cities.add(city);
                                    citiesAdapter.notifyDataSetChanged();
                                }
                            }
                        });

                    }
                }
            });
    }

    public void onClickSortFilter(View v){
        Intent intent1 = new Intent(v.getContext(), SortFilterActivity.class);
        startActivityForResult(intent1, 1);
    }

    private void isUserLogged(){
        Button mLoginButton = (Button) findViewById(R.id.loginButtonSearch);
        ImageView mAccount = (ImageView) findViewById(R.id.accountImageSearch);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            mLoginButton.setVisibility(View.GONE);

        } else {
            // No user is signed in
            mAccount.setVisibility(View.GONE);
        }
    }

    private void getDoctorsData(final DocumentReference doctorRef){
        doctorRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final Doctor foundDoctor = documentSnapshot.toObject(Doctor.class);
                        System.out.println(doctorRef.getId());
                        s1.add(doctorRef.getId());
                        docNames.add(foundDoctor.getDegree() + " "
                                + foundDoctor.getFirst_name() + " "
                                + foundDoctor.getLast_name());
                        //s2.add(chosenSpec);
                        images.add(R.drawable.ic_profile_lagoon);
                        docRates.add(foundDoctor.getAverage_rate());
                        docPrices.add(foundDoctor.getAppointment_price());
                        docInfos.add(foundDoctor.getPersonal_info());
                        searchRecyclerAdapter.notifyDataSetChanged();

                        final int docNum = s1.size() - 1;


                        //specs

                        s2.add("");
                        searchRecyclerAdapter.notifyDataSetChanged();

                        docHasSpec
                            .whereEqualTo("doctor_id",doctorRef)
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



                        //opinions
                        opinionCounters.add(0);
                        rateCounters.add(0);
                        docReviews.add("");
                        searchRecyclerAdapter.notifyDataSetChanged();

                        appointments.whereEqualTo("doctor_id", doctorRef)
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



                        //check if isFav
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user == null){
                            favourites.add(false);
                            searchRecyclerAdapter.notifyDataSetChanged();
                        }

                        else {
                            String userId = user.getUid();
                            patients.document(userId).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    DocumentReference userRef = documentSnapshot.getReference();
                                    favouriteCol.whereEqualTo("patient_id", userRef)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            boolean isFav = false;
                                            for (QueryDocumentSnapshot documentSnapshot1:queryDocumentSnapshots){
                                                if (documentSnapshot1.getDocumentReference("doctor_id")
                                                        .equals(doctorRef))
                                                    isFav = true;
                                            }
                                            favourites.add(isFav);
                                            searchRecyclerAdapter.notifyDataSetChanged();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            favourites.add(false);
                                            searchRecyclerAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    }
                                });
                        }


                        //clinics data

                        //DocumentReference docClinicRef = foundDoctor.getClinic_id();
                        DocumentReference docClinicRef = documentSnapshot.getDocumentReference("clinic_id");
                        //System.out.println("Clinic_ref" );
                        //System.out.println(foundDoctor.getClinic_id().toString());
                        //System.out.println(documentSnapshot.getDocumentReference("clinic_id").toString());
                        docClinicRef.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                //System.out.println("Clinic_data" );
                               // System.out.println(documentSnapshot.getId() +
                                 //" => " + documentSnapshot.getData());
                                docCMs.add(String.valueOf(documentSnapshot.get("clinic_name")));
                                //System.out.println("Clinic_name" + String.valueOf(documentSnapshot.get("clinic_name")));
                                searchRecyclerAdapter.notifyDataSetChanged();

                                DocumentReference clinicAddress = documentSnapshot.getDocumentReference("address_id");
                                clinicAddress.get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                Address address = documentSnapshot.toObject(Address.class);
                                                //System.out.println(address.getCity() + address.getStreet() + address.getBuilding_number() + address.getApartment());
                                                String cmAddress = "";
                                                cmAddress += address.getCity() + ", "
                                                        + address.getStreet() + " " + address.getBuilding_number();
                                                //if (!address.getApartment()!=null){
                                                  //  cmAddress += "/" + address.getApartment();
                                                //}

                                                docCities.add(cmAddress);
                                                searchRecyclerAdapter.notifyDataSetChanged();
                                            }
                                        });
                                }
                            });
                    }
                });
    }


    private void docSearch(){
        System.out.println(chosenSpec.toUpperCase());
        specializations
            .whereEqualTo("specialization_name",chosenSpec)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        docHasSpec
                            .whereEqualTo("specialization_id", document.getReference())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        DocumentReference doctorRef = documentSnapshot
                                                .getDocumentReference("doctor_id");
                                        if(!chosenCity.equals("Dowolna"))
                                            checkDocsCity(doctorRef);
                                        else
                                            getDoctorsData(doctorRef);
                                    }
                                }
                            }
                        });
                    }
                } else {
                    System.out.println("Search" + "Error getting documents: " + task.getException());
                }
                }
            });
    }

    private void citySearch(){
        doctors
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        checkDocsCity(documentSnapshot.getReference());
                    }
                }
                }
            });
    }

    private void checkDocsCity(final DocumentReference doctorRef){
        //search based on city
        doctorRef.get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                DocumentReference docsClinic = documentSnapshot
                                                .getDocumentReference("clinic_id");
                docsClinic.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DocumentReference clinicAddress = documentSnapshot
                                                .getDocumentReference("address_id");
                        clinicAddress.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Address address = documentSnapshot.toObject(Address.class);
                                if (chosenCity.equals(address.getCity()))
                                    getDoctorsData(doctorRef);
                                }
                            });
                        }
                    });
                }
            });


    }

    public void onClickLogin(View view){
        Intent intent = new Intent(view.getContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){//defines parent activity
            if(resultCode == RESULT_OK){
                String sortOption = data.getStringExtra("sortOption");
                String sortDirection = data.getStringExtra("sortDirection");
                int filter = data.getIntExtra("waitTimeMax",1);
                Toast.makeText(this, "Sortowanie " + sortDirection.toLowerCase() + " wg " + sortOption.toLowerCase()+Integer.toString(filter), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onClickSearckAgain(View view){
        Intent intent = new Intent(view.getContext(), SearchResultActivity.class);
        String specializaion = mSpecs.getText().toString();
        String city = mCities.getText().toString();
        if(specializaion.equals("") && city.equals("")){
            Toast.makeText(this, "Podaj miasto lub specjalizaję", Toast.LENGTH_SHORT).show();
        }
        else {
            if(specializaion.equals(""))
                intent.putExtra("specialization", "Dowolna");
            else
                intent.putExtra("specialization", specializaion);

            if(city.equals(""))
                intent.putExtra("city", "Dowolna");
            else
                intent.putExtra("city", city);

            startActivity(intent);
        }
    }
}