package com.example.centrummedyczne;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    private Button mLoginButton, mSearchButton;
    private String specs[], cities[];
    private ImageView mAccount;

    private String spec1[];

    AutoCompleteTextView mSpecs, mCities;

    ArrayAdapter<String> specsAdapter;
    List<String> s1;

    private List<Specialization> s;
    private Specialization specialization;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference specializations = db.collection("specialization");

    private static final String TAG = "StartActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mLoginButton = (Button) findViewById(R.id.loginButtonStart);
        mAccount = (ImageView) findViewById(R.id.accountImageStart);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            mLoginButton.setVisibility(View.GONE);

        } else {
            // No user is signed in
            mAccount.setVisibility(View.GONE);
        }

       /* specializations
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> s1 = new ArrayList<>();
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //Toast.makeText(StartActivity.this, document.get("specialization_name").toString(),Toast.LENGTH_LONG).show();

                               /* String s = document.getString("specialization_name");
                                s1.add(s);
                                addToSpecs(s, i);
                                i++;*/


/*
                            }
                            for (String st : s1 ){
                                System.out.println(st);
                                for(int j = 0; j < specs.length; j++ ){
                                    System.out.println(specs[j]+"0");
                                }
                            }
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });*/












        String[] el = getSpec1();
        //for (int i = 0; i < el.length; i++) {
          //  System.out.println("1+" + el[i]);
       // }
        //mSpecs = (AutoCompleteTextView) findViewById(R.id.specsAutoComplete);

        /*s = new ArrayList<>();
        //specialization = new Specialization();

        specializations
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<Specialization> l = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()){

                        l.add(document.toObject(Specialization.class));
                        specialization = document.toObject(Specialization.class);
                        specialization.setS_name(document.toObject(Specialization.class).);
                        //System.out.println(specialization.getS_name()+"\n\n\n");

                        System.out.println(document.toObject(Specialization.class).getS_name().toString());

                    }
                    s = l;



                }
            }
        });

        //System.out.println(specialization.getS_name());

        /*int i = 0;
        for (Specialization ls : getS()){
            specs[i] = ls.getS_name().toString();
            i++;
        }*/

        specs = getResources().getStringArray(R.array.specs);
        cities = getResources().getStringArray(R.array.cities);
        s1 = new ArrayList<>();
        specsAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, s1);
         mSpecs = (AutoCompleteTextView) findViewById(R.id.specsAutoComplete);
        mSpecs.setAdapter(specsAdapter);

         ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, cities);
         mCities = (AutoCompleteTextView) findViewById(R.id.citiesAutoComplete);
        mCities.setAdapter(citiesAdapter);


        specializations.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String data = "";

                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Specialization specialization = documentSnapshot.toObject(Specialization.class);
                    specialization.setDocID(documentSnapshot.getId());


                    String docId = specialization.getDocID();
                    String s_name = specialization.getSpecialization_name();
                    s1.add(s_name);
                    //specsAdapter.add(s_name);
                    specsAdapter.notifyDataSetChanged();
                    String s_details = specialization.getSpecialization_details();
                    data += "ID: " + docId + "\nsName: " + s_name + "\nsDetails: " + s_details + "\n\n";
                }
                System.out.println(data);
                setSpec1(s1.toArray(new String[s1.size()]));
                String[] el = getSpec1();
                //specs = getSpec1();


                for(int i= 0 ; i< getSpec1().length; i++){
                    System.out.println("1+" + el[i]);
                }
            }
        });

        mSearchButton = (Button) findViewById(R.id.docSearchButton);

    }


    public void onClickSearch(View view){
        Intent intent = new Intent(view.getContext(), SearchResultActivity.class);
        String specializaion = mSpecs.getText().toString();
        intent.putExtra("specialization", specializaion);
        String city = mCities.getText().toString();
        intent.putExtra("city", city);
        startActivity(intent);
    }
    public void setSpec1(String[] spec1) {
        this.spec1 = spec1;
    }

    public String[] getSpec1(){
        return spec1;
    }

    public void onClickLogin(View view){
        Intent intent = new Intent(view.getContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void addToSpecs(String s, int i){
        specs[i] = s;
    }
}