package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookAppointmentActivity extends AppCompatActivity {

    private String docName, docSpec, docCM, docCity, docId;
    private int docImg;
    private float docPrice;

    private List<String> visitDates;
    private List<DocumentReference> visitRefs;
    private AvaliableAdapter avaliableAdapter;

    private TextView mDocName, mDocSpec, mDocCM, mDocCity, mDocPrice;
    private ImageView mDocImage;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference appointmentCol = db.collection("appointment");
    private final CollectionReference doctorCol = db.collection("doctor");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        mDocName = findViewById(R.id.docNameBooking);
        mDocSpec = findViewById(R.id.docSpecBooking);
        mDocCM = findViewById(R.id.docCMBooking);
        mDocCity = findViewById(R.id.docCMAddressBooking);
        mDocPrice = findViewById(R.id.docPriceBooking);
        mDocImage = findViewById(R.id.docImgBooking);

        getData();
        setData();
        createRecycler();
        getAppointments();
    }

    private void createRecycler(){
        RecyclerView avaliableRecycler = findViewById(R.id.avaliableRecycler);
        visitDates = new ArrayList<>();
        visitRefs = new ArrayList<>();
        avaliableAdapter = new AvaliableAdapter(this, visitDates, visitRefs);
        avaliableRecycler.setAdapter(avaliableAdapter);
        avaliableRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getAppointments(){
        System.out.println("docId" + docId);
        doctorCol.document(docId).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DocumentReference doctorRef = documentSnapshot.getReference();
                    System.out.println("docRef" + doctorRef);
                    appointmentCol.whereEqualTo("doctor_id",doctorRef)
                        .whereEqualTo("booked", false).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                   // Toast.makeText(BookAppointmentActivity.this,  documentSnapshot.getId(), Toast.LENGTH_SHORT).show();
                                  //  System.out.println(documentSnapshot.getId());
                                    //System.out.println(documentSnapshot.getData());
                                    visitRefs.add(documentSnapshot.getReference());
                                    visitDates.add(FormatData.reformatDateTime(documentSnapshot.getDate("appointment_start")));
                                    avaliableAdapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(BookAppointmentActivity.this, "Nie znaleziono wizyt", Toast.LENGTH_SHORT).show();
                                System.out.println("Nie znaleziono wizyt");
                            }
                        });
                }
            });
    }

    private void getData(){
        docName = getIntent().getStringExtra("docName");
        docSpec = getIntent().getStringExtra("docSpec");
        docCM = getIntent().getStringExtra("docCM");
        docCity = getIntent().getStringExtra("docCity");
        docPrice = getIntent().getFloatExtra("price", 0);
        docImg = getIntent().getIntExtra("docImg", 0);
        docId = getIntent().getStringExtra("docId");
    }

    private void setData(){
        mDocName.setText(docName);
        mDocSpec.setText(docSpec);
        mDocCM.setText(docCM);
        mDocCity.setText(docCity);
        mDocPrice.setText(String.format("%.2f", docPrice) + " PLN");
        mDocImage.setImageResource(docImg);
    }
}
