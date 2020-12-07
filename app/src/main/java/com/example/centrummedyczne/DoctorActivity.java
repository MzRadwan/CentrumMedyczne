package com.example.centrummedyczne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static com.example.centrummedyczne.R.string.doctor_added_to_favorites;
import static com.example.centrummedyczne.R.string.doctor_deleted_from_favourites;

public class DoctorActivity extends AppCompatActivity {

    ImageView mainImageView, mHeartFull, mHeartBorder;
    TextView title, description, mDocPrice, mDocInfo, mDocCM,
            mDocCity, mRateCount, mOpinionCount, mRateText,
            mOpinionsDisplay, mOpinionsHeader;
    TextView mDocRate;

    private Button mMoreOpinions;

    private String data1, data2, name, info, docCM, docCity, docReview, myImage;
    private boolean isFav;
    private float rate, price;
    private int  rateCount, opinionCount;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference favourites = db.collection("favourite");
    private final CollectionReference patients = db.collection("patient");
    private final CollectionReference doctors = db.collection("doctor");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        mainImageView = findViewById(R.id.mainImageView);
        title = findViewById(R.id.title);
        description = findViewById(R.id.docSpecDocActivity);
        mDocRate = findViewById(R.id.docRateTextView);
        mDocPrice = findViewById(R.id.docPriceDocActivity);
        mDocInfo = findViewById(R.id.docInfoDocAcitivity);
        mDocCM = findViewById(R.id.docCMDocA);
        mDocCity = findViewById(R.id.docCMAddressDocA);
        mHeartFull = findViewById(R.id.favImgFullDocA);
        mHeartBorder = findViewById(R.id.favImgBorderDocA);
        mRateCount = findViewById(R.id.rateCounterTextDocA);
        mOpinionCount = findViewById(R.id.opinionCounterDocA);
        mRateText = findViewById(R.id.rateAvgTextDocA);
        mMoreOpinions = findViewById(R.id.showMoreOpinionsDocA);
        mOpinionsDisplay = findViewById(R.id.opinionsDisplayDocA);
        mOpinionsHeader = findViewById(R.id.opinionsHeaderDocA);


        getData();
        setData();
    }

    private void getData() {
        if(getIntent().hasExtra("images")
                && getIntent().hasExtra("data1")
                && getIntent().hasExtra("data2")){
            data1 = getIntent().getStringExtra("data1");
            data2 = getIntent().getStringExtra("data2");
            name = getIntent().getStringExtra("name");
            myImage = getIntent().getStringExtra("images");
            rate = getIntent().getFloatExtra("rate", 0);
            price = getIntent().getFloatExtra("price", 0);
            info = getIntent().getStringExtra("info");
            docCM = getIntent().getStringExtra("cm");
            docCity = getIntent().getStringExtra("city");
            isFav = getIntent().getBooleanExtra("isFav", false);
            rateCount = getIntent().getIntExtra("rateCounter", 0);
            opinionCount = getIntent().getIntExtra("opinionCounter", 0);
            docReview = getIntent().getStringExtra("docReviews");

        }
        else{
            Toast.makeText(this,"No data", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData(){
        isUserLogged();
        title.setText(name);
        description.setText(data2);
        mDocInfo.setText(info);

        mDocPrice.setText(String.format("%.2f PLN", price));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        String noImage = "https://firebasestorage.googleapis.com/v0/b/centrum-medyczne-8367d.appspot.com/o/doctors%2F1606218227891.png?alt=media&token=73b5f128-40c4-4ff2-9179-4145c9daab39";

        if(!myImage.equals(noImage)) {
            StorageReference s = storage.getReferenceFromUrl(myImage);
            s.getBytes(1024 * 1024)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            mainImageView.setImageBitmap(bitmap);
                        }
                    });
        }
        mDocCM.setText(docCM);
        mDocCity.setText(docCity);

        displayOpinionsAndRates();

    }

    private void isUserLogged(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ImageView mAccountImage = findViewById(R.id.accountImageDocA);
        ImageView mSearchImage = findViewById(R.id.searchIconDocA);
        Button mLogin = findViewById(R.id.loginButtonDocA);

        if(user == null){
            mHeartFull.setVisibility(View.GONE);
            mHeartBorder.setVisibility(View.GONE);
            mAccountImage.setVisibility(View.GONE);
            mLogin.setVisibility(View.VISIBLE);
            mSearchImage.setVisibility(View.GONE);
        }
        else {
            updateFavHeart();
            mAccountImage.setVisibility(View.VISIBLE);
            mLogin.setVisibility(View.GONE);
            mSearchImage.setVisibility(View.VISIBLE);
        }
    }

    private void displayOpinionsAndRates(){
        if(rateCount == 0){
            mRateCount.setVisibility(View.GONE);
            mDocRate.setVisibility(View.GONE);
            mRateText.setText(R.string.no_rates);

        }
        else {
            mRateCount.setText(""+rateCount+" ocen");
            mDocRate.setText(String.valueOf(rate));
        }


        if (opinionCount == 0){
            mOpinionsDisplay.setVisibility(View.GONE);
            mMoreOpinions.setVisibility(View.GONE);
            mOpinionCount.setVisibility(View.GONE);
            mOpinionsHeader.setText(R.string.no_opinions);
        }

        else {
            mOpinionCount.setText(""+opinionCount+" opinii");
            mOpinionsDisplay.setText(" " +docReview);
        }
    }

    private void updateFavHeart(){
        if(isFav){
            mHeartBorder.setVisibility(View.GONE);
            mHeartFull.setVisibility(View.VISIBLE);
            mHeartFull.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();

                patients.document(userId).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final DocumentReference userRef = documentSnapshot.getReference();
                        String docId = data1;
                        doctors.document(docId).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                final DocumentReference doctorRef = documentSnapshot.getReference();
                                favourites.whereEqualTo("patient_id", userRef)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots){
                                            if (documentSnapshot1.getDocumentReference("doctor_id").equals(doctorRef)){
                                                DocumentReference favRef = documentSnapshot1.getReference();
                                                favRef.delete();
                                                Toast.makeText(DoctorActivity.this,
                                                        doctor_deleted_from_favourites, Toast.LENGTH_SHORT).show();

                                                isFav = false;
                                                updateFavHeart();
                                            }
                                        }
                                        }
                                    });

                                    }
                                });
                        }
                    });

                }
            });
        }
        else {
            mHeartFull.setVisibility(View.GONE);
            mHeartBorder.setVisibility(View.VISIBLE);
            mHeartBorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = user.getUid();
                    patients.document(userId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                            final DocumentReference userRef = documentSnapshot.getReference();
                            String docId = data1;
                            doctors.document(docId).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    final DocumentReference doctorRef = documentSnapshot.getReference();
                                    Map<String, Object> fav = new HashMap<>();
                                    fav.put("doctor_id", doctorRef);
                                    fav.put("patient_id", userRef);
                                    favourites.add(fav)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            Toast.makeText(DoctorActivity.this,
                                                    doctor_added_to_favorites, Toast.LENGTH_SHORT).show();
                                            isFav = true;
                                            updateFavHeart();
                                            }
                                            });

                                    }
                                });
                            }
                        });
                }
            });
        }
    }

    public void onClickBookAppointment(View view){
        Intent intent = new Intent(DoctorActivity.this, BookAppointmentActivity.class);
        intent.putExtra("docId", data1);
        intent.putExtra("docName", name);
        intent.putExtra("docSpec", data2);
        intent.putExtra("docCM", docCM);
        intent.putExtra("docCity", docCity);
        intent.putExtra("price", price);
        intent.putExtra("docImg", myImage);
        startActivity(intent);
    }

    public void onClickSearchDocActivity(View view){
        Intent intent = new Intent(DoctorActivity.this, StartActivity.class);
        finish();
        startActivity(intent);
    }

    public void onClickAccountDocActivity(View view){
        Intent intent = new Intent(DoctorActivity.this, PatientAccountActivity.class);
        finish();
        startActivity(intent);
    }

    public void onClickBackToDocsList(View view){
        finish();
    }
}