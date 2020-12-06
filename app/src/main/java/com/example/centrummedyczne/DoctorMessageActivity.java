package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorMessageActivity extends AppCompatActivity {

    private TextView mDocName, mDocSpec;
    private ImageView mDocImg;


    private String docName, docSpec, docId;
    private int docImg;

    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private RecyclerView mMessageRecycler;

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
        getUsersMessages();
    }

    private void createMessageRecycler(){
        messageList = new ArrayList<>();
        mMessageRecycler = findViewById(R.id.messageRecycler);
        messageAdapter = new MessageAdapter(this, messageList);
        mMessageRecycler.setAdapter(messageAdapter);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));

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
            Date date = new Date(System.currentTimeMillis());
            newMessage.put("date", date);
           // newMessage.put("date", FieldValue.serverTimestamp());
            newMessage.put("to_doctor", true);
            newMessage.put("text", messageEdit.getText().toString());
            newMessage.put("doctor_id", docRef);
            newMessage.put("patient_id",userRef);
            messageCol.add(newMessage)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(DoctorMessageActivity.this, "Wiadomość wysłana", Toast.LENGTH_SHORT).show();
                    messageEdit.setText("");
                }
            });
        }
    }

    private void listenForNewMessages(DocumentReference userRef, DocumentReference docRef){
        messageCol.whereEqualTo("patient_id",userRef)
            .whereEqualTo("doctor_id", docRef)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(DoctorMessageActivity.this, "Message error:" + error, Toast.LENGTH_SHORT).show();
                    }

                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            Message message = dc.getDocument().toObject(Message.class);
                            if(!messageList.contains(message)){
                                if(message.getDate() == null){
                                    Date date = new Date(System.currentTimeMillis());
                                    message.setDate(date);
                                }
                                messageList.add(message);
                                orderMessages();
                               // messageAdapter.notifyItemInserted(messageList.size() - 1);
                                //mMessageRecycler.scrollToPosition(messageList.size() - 1);
                            }
                        }
                    }
                }
            });
    }

    private void getUsersMessages(){
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
                                //getMessageTexts(userRef, docRef);
                                listenForNewMessages(userRef, docRef);
                            }
                        });
                    }
                });
        }
    }

    private void getMessageTexts(final DocumentReference userRef, final DocumentReference docRef){
        messageCol.whereEqualTo("patient_id",userRef)
            .whereEqualTo("doctor_id", docRef).get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        Message message = documentSnapshot.toObject(Message.class);
                        messageList.add(message);
                    }
                    orderMessages();
                    listenForNewMessages(userRef, docRef);
                }
            });
    }

    private void orderMessages(){
        for (int i = 0; i < messageList.size(); i++){
            boolean swap = false;
            for (int j = 0; j < messageList.size() - i -1; j++){
                Message currentMessage = messageList.get(j);
                Message nextMessage = messageList.get(j + 1);
                if (currentMessage.getDate().after(nextMessage.getDate())){
                    swap = true;
                    messageList.set(j,nextMessage);
                    messageList.set(j + 1, currentMessage);
                }
            }
            if (!swap) {
                break;
            }
        }
        for (Message m : messageList){
            System.out.println(m.getDate().toString() + m.getText());
        }
        messageAdapter.notifyDataSetChanged();
        mMessageRecycler.scrollToPosition(messageList.size() - 1);
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