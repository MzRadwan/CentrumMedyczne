package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class BookAppointmentActivity extends AppCompatActivity {

    private String docName, docSpec, docCM, docCity, docId, docImg;

    private float docPrice;

    private List<String> visitDates;
    private List<String> visitRefs;
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
                //System.out.println("docRef" + doctorRef);
                appointmentCol.whereEqualTo("doctor_id",doctorRef)
                    .whereEqualTo("booked", false).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.getResult().isEmpty()){
                                    TextView noAppointments = findViewById(R.id.avaliableTextBooking);
                                    noAppointments.setText("Brak dostępnych wizyt");
                                }
                            }
                        })
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                visitRefs.add(documentSnapshot.getId());
                                visitDates.add(FormatData.reformatDateTime(documentSnapshot.getDate("appointment_start")));
                                avaliableAdapter.notifyDataSetChanged();
                            }
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
        docImg = getIntent().getStringExtra("docImg");
        docId = getIntent().getStringExtra("docId");
    }

    private void setData(){
        mDocName.setText(docName);
        mDocSpec.setText(docSpec);
        mDocCM.setText(docCM);
        mDocCity.setText(docCity);
        mDocPrice.setText(String.format("%.2f PLN",docPrice));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String noImage = "https://firebasestorage.googleapis.com/v0/b/centrum-medyczne-8367d.appspot.com/o/doctors%2F1606218227891.png?alt=media&token=73b5f128-40c4-4ff2-9179-4145c9daab39";

        if(!docImg.equals(noImage)) {
            StorageReference s = storage.getReferenceFromUrl(docImg);
            s.getBytes(1024 * 1024)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            mDocImage.setImageBitmap(bitmap);
                        }
                    });
        }

    }


}
