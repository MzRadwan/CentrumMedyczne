package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MainActivity";
    TextView mText1;
    Button mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText1 = (TextView) findViewById(R.id.text1);
        mAddButton =(Button) findViewById(R.id.addButton);


        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> address = new HashMap<>();
                address.put("apartment",null);
                address.put("building_number", "114");
                address.put("city","Sosnowiec");
                address.put("postal_code","75-102");
                address.put("street","Sympatyczna");


                db.collection("address")
                        .add(address)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "added, ID = " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG,"Error " ,e);
                            }
                        });
            }
        });


       /* db.collection("specialization")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                mText1.setText(document.getData().toString());
                            }
                        }
                        else{
                            Log.w(TAG, "Error: ", task.getException());
                        }
                    }
                });

        */

        //CollectionReference addressesRef = db.collection("address");
      //  Query query = addressesRef.whereEqualTo("city","Lublin");


        //Query query = db.collection("address").whereEqualTo("city","Lublin");
        //query.get().addOnCompleteListener()


        /*db.collection("address")
                .whereEqualTo("city","Lublin")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                mText1.setText(document.getId() + document.getData().toString());
                            }
                        }
                        else {
                            mText1.setText("Error: " + task.getException());
                        }
                    }
                });*/



    }
}