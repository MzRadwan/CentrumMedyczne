package com.example.centrummedyczne;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DoctorMessageActivity extends AppCompatActivity {

    private TextView mDocName, mDocSpec;
    private ImageView mDocImg;

    private String docName, docSpec, docId;
    private int docImg;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final CollectionReference patientCol = db.collection("patient");
    private final CollectionReference messageCol = db.collection("message");
    private final CollectionReference doctorCol = db.collection("doctor");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_message);

        mDocName = findViewById(R.id.docNameMsg);
        mDocSpec = findViewById(R.id.docSpecMsg);
        mDocImg = findViewById(R.id.docImgMsg);

        getData();
        setData();
        createMessageRecycler();
        getMessages();
    }

    private void createMessageRecycler(){

    }

    public void sendMessage(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            String userId = user.getUid();
            patientCol.document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                    final DocumentReference userRef = documentSnapshot.getReference();
                    doctorCol.document(docId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                DocumentReference docRef = documentSnapshot.getReference();
                                createMessage(userRef, docRef);
                            }
                        });

                    }
                });
        }
    }

    private void createMessage(DocumentReference userRef, DocumentReference docRef){
        final EditText messageEdit = findViewById(R.id.messageEdit);
        String messageText = messageEdit.getText().toString();
        if (!messageText.trim().equals("")){
            Map<String, Object> newMessage = new HashMap<>();
            newMessage.put("date", FieldValue.serverTimestamp());
            newMessage.put("to_doctor", true);
            newMessage.put("text", messageEdit.getText().toString());
            newMessage.put("doctor_id", docRef);
            newMessage.put("patient_id",userRef);
            messageCol.add(newMessage)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(DoctorMessageActivity.this, "Message send", Toast.LENGTH_SHORT).show();
                    messageEdit.setText("");
                }
            });
        }

    }

    private void getMessages(){


    }


    private void getData(){
        docName = getIntent().getStringExtra("docName");
        docSpec = getIntent().getStringExtra("docSpec");
        docImg = getIntent().getIntExtra("docImg", 0);
        docId = getIntent().getStringExtra("docId");

    }

    private void setData(){
        mDocName.setText(docName);
        mDocSpec.setText(docSpec);
        mDocImg.setImageResource(docImg);
    }
}