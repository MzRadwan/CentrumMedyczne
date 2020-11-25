package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;

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

public class StartActivity extends AppCompatActivity {

    private AutoCompleteTextView mSpecs, mCities;

    private List<String> specs;
    private List<String> cities;
    private ArrayAdapter<String> specsAdapter;
    private ArrayAdapter<String> citiesAdapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference specializations = db.collection("specialization");
    private final CollectionReference clinics = db.collection("clinic");
    private final CollectionReference address = db.collection("address");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        userOrGuest();

        findSpecs();
        findCities();

    }

    private void userOrGuest(){
        Button mLoginButton = (Button) findViewById(R.id.loginButtonStart);
        ImageView mAccount = (ImageView) findViewById(R.id.accountImageStart);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            mLoginButton.setVisibility(View.GONE);

        } else {
            // No user is signed in
            mAccount.setVisibility(View.GONE);
        }
    }

    private void findSpecs(){
        mSpecs = (AutoCompleteTextView) findViewById(R.id.specsAutoComplete);

        specs = new ArrayList<>();

        specsAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, specs);
        mSpecs.setAdapter(specsAdapter);

        specializations.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Specialization specialization = documentSnapshot.toObject(Specialization.class);
                            String s_name = specialization.getSpecialization_name();
                                specs.add(s_name);
                                specsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void findCities(){

        mCities = findViewById(R.id.citiesAutoComplete);

        cities = new ArrayList<>();

        citiesAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, cities);
        mCities.setAdapter(citiesAdapter);

        //cities hint from Firestore
        clinics.get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {
                for (final QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    DocumentReference addressRef = documentSnapshot.getDocumentReference("address_id");
                    addressRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                       // Address address = documentSnapshot.toObject(Address.class);
                        String city = documentSnapshot.getString("city");
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

    public void onClickAccount(View view){
        Intent intent = new Intent(view.getContext(), PatientAccountActivity.class);
        finish();
        startActivity(intent);
    }

    public void onClickSearch(View view){

        String specializaion = mSpecs.getText().toString();
        String city = mCities.getText().toString();

        if(specializaion.equals("") && city.equals("")){
            Toast.makeText(StartActivity.this, "Podaj specjalizację lub miasto",
                    Toast.LENGTH_SHORT).show();
        }

        else{
            Intent intent = new Intent(view.getContext(), SearchResultActivity.class);
            if(specializaion.equals(""))
                intent.putExtra("specialization", "Dowolna");
            else {
                String chosenSpec = specializaion.substring(0,1).toUpperCase()
                        .concat(specializaion.substring(1,specializaion.length()).toLowerCase());
                intent.putExtra("specialization", chosenSpec);
            }

            if(city.equals(""))
                intent.putExtra("city", "Dowolna");
            else {
                String chosenCity = city.substring(0,1).toUpperCase()
                        .concat(city.substring(1,city.length()).toLowerCase());
                intent.putExtra("city", chosenCity);
            }

            startActivity(intent);
            finish();
        }

    }

    public void onClickLogin(View view){
        Intent intent = new Intent(view.getContext(), LoginActivity.class);

        startActivity(intent);
        finish();
    }

}