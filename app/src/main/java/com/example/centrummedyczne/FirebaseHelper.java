package com.example.centrummedyczne;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference drugCol = db.collection("drug");
    private CollectionReference appoinmentCol = db.collection("appointment");
    private static CollectionReference patientCol = db.collection("patient");
    /*

    public static DocumentReference getPatientRef(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String userId = user.getUid();
            patientCol.document(userId).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            DocumentReference userRef = documentSnapshot.getReference();
                            return userRef;


                        }
                    });
        }
    } */
}



