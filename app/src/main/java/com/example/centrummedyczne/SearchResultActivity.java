package com.example.centrummedyczne;

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
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private AutoCompleteTextView mSpecs, mCities;
    private Button mSortFilter, mLoginButton;

    private ImageView mAccount;

    private List<String> specs;
    private List<String> cities;
    private ArrayAdapter <String> specsAdapter;
    private ArrayAdapter <String> citiesAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference specializations = db.collection("specialization");
    private CollectionReference clinics = db.collection("clinic");
    private CollectionReference address = db.collection("address");


    RecyclerView mRecyclerView;

    String s1[], s2[];
    int images[] = {R.drawable.ic_profile_blue,R.drawable.ic_profile_blue,
            R.drawable.ic_profile_lagoon, R.drawable.ic_profile_lagoon,
            R.drawable.ic_profile_blue, R.drawable.ic_profile_lagoon,
            R.drawable.ic_profile_lagoon, R.drawable.ic_profile_blue,
            R.drawable.ic_profile_blue, R.drawable.ic_profile_blue};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mLoginButton = (Button) findViewById(R.id.loginButtonSearch);
        mAccount = (ImageView) findViewById(R.id.accountImageSearch);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            mLoginButton.setVisibility(View.GONE);

        } else {
            // No user is signed in
            mAccount.setVisibility(View.GONE);
        }


        //search text views settings

        mSpecs = (AutoCompleteTextView) findViewById(R.id.specializationTextView);
        mCities = (AutoCompleteTextView) findViewById(R.id.cityTextView);

        Intent intent = getIntent();
        String chosenSpec = intent.getStringExtra("specialization");
        mSpecs.setHint(chosenSpec);
        String chosenCity = intent.getStringExtra("city");
        mCities.setHint(chosenCity);

        specs = new ArrayList<>();
        specsAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, specs);
        mSpecs.setAdapter(specsAdapter);

        cities = new ArrayList<>();
        citiesAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, cities);
        mCities.setAdapter(citiesAdapter);

        // spec hints from Firestore

        specializations.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Specialization specialization = documentSnapshot.toObject(Specialization.class);
                    //specialization.setDocID(documentSnapshot.getId());
                    String s_name = specialization.getSpecialization_name();
                    specs.add(s_name);
                    specsAdapter.notifyDataSetChanged();
                }
            }
        });

        //cities hint from Firestore
        clinics.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {
                        for (final QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Clinic clinic = documentSnapshot.toObject(Clinic.class);
                            DocumentReference addressRef = clinic.getAddress_id();
                            String addressId = addressRef.getPath().substring(8);
                            System.out.println(addressId);
                            address.document(addressId);
                            addressRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Address address = documentSnapshot.toObject(Address.class);
                                        String city = address.getCity();
                                        cities.add(city);
                                        citiesAdapter.notifyDataSetChanged();
                                }
                            });

                        }
                    }
                });


        //search results

        mRecyclerView = findViewById(R.id.docRecyler);

        s1 = getResources().getStringArray(R.array.doctors);
        s2 = getResources().getStringArray(R.array.description);

        SearchRecyclerAdapter searchRecyclerAdapter = new SearchRecyclerAdapter(this,s1, s2, images);
        mRecyclerView.setAdapter(searchRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSortFilter = (Button) findViewById(R.id.sortFilterButton);
        mSortFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), SortFilterActivity.class);
                startActivityForResult(intent1, 1);
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
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClickSearckAgain(View view){
        Intent intent = new Intent(view.getContext(), SearchResultActivity.class);
        String specializaion = mSpecs.getText().toString();
        intent.putExtra("specialization", specializaion);
        String city = mCities.getText().toString();
        intent.putExtra("city", city);
        startActivity(intent);
    }
}