package com.example.centrummedyczne;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.MyViewHolder> {


    List<String> data1, data2, docNames, docInfos, docCMs, docCities, docReviews;
    List<Integer> images, rateCounters, opinionCounters;
    List<Float> docRates, docPrices;
    List<Boolean> favourites;

    Context context;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference docHasSpec = db.collection("doctor_has_specialization");
    private final CollectionReference doctors = db.collection("doctor");


    public SearchRecyclerAdapter(Context ct, List<String> s1, List<String> s2,
                                 List<Integer> img, List<Float> docRates, List<Float> docPrices,
                                 List<String> docNames, List<String> docInfos, List<String> docCMs,
                                 List<String> docCities, List<Boolean> favourites,
                                 List<Integer> opinionCounters, List<Integer> rateCounters, List<String> docReviews){
        context = ct;
        data1 = s1;
        data2 = s2;
        images = img;
        this.docRates = docRates;
        this.docPrices = docPrices;
        this.docNames = docNames;
        this.docInfos = docInfos;
        this.docCMs = docCMs;
        this.favourites = favourites;
        this.docCities = docCities;
        this.rateCounters = rateCounters;
        this.opinionCounters = opinionCounters;
        this.docReviews = docReviews;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.search_doc_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, final int position) {

       /* final String docId = data1.get(position);
        docHasSpecs = new HashMap<>();

        doctors.document(docId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Doctor foundDoctor = documentSnapshot.toObject(Doctor.class);

                DocumentReference doctorRef = documentSnapshot.getReference();
                docHasSpec
                    .whereEqualTo("doctor_id",doctorRef)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                DocumentReference docSpecs = documentSnapshot
                                        .getDocumentReference("specialization_id");
                                }
                            }
                            }
                        });
            }
        });*/

        holder.myTextView1.setText(docNames.get(position));
        holder.myTextView2.setText(data2.get(position));
        holder.myImage.setImageResource(images.get(position));
        holder.mDocRate.setRating(docRates.get(position));
        holder.mDocPrice.setText("Cena za wizytę: " + String.format("%.2f", docPrices.get(position)) + " PLN");
        if(docCMs.size() == docNames.size())
            holder.mDocCM.setText(docCMs.get(position));

        if(docCities.size() == docNames.size())
            holder.mDocCity.setText(docCities.get(position));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            holder.mFullHeart.setVisibility(View.GONE);
            holder.mHeartBorder.setVisibility(View.GONE);
        }

        if (favourites.size() == docNames.size()){
            if(favourites.get(position) )
                holder.mFullHeart.setVisibility(View.VISIBLE);
        }

       /* if (rateCounters.get(position) == 0) { // no ratings

        }

*/
        holder.searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorActivity.class);
                intent.putExtra("data1", data1.get(position));
                intent.putExtra("data2", data2.get(position));
                intent.putExtra("images", images.get(position));
                intent.putExtra("rate", docRates.get(position));
                intent.putExtra("price",docPrices.get(position));
                intent.putExtra("name", docNames.get(position));
                intent.putExtra("info", docInfos.get(position));
                intent.putExtra("cm", docCMs.get(position));
                intent.putExtra("city", docCities.get(position));
                intent.putExtra("isFav", favourites.get(position));
                intent.putExtra("rateCounter", rateCounters.get(position));
                intent.putExtra("opinionCounter", opinionCounters.get(position));
                intent.putExtra("docReviews", docReviews.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myTextView1, myTextView2, mDocPrice, mDocCM,
                mDocCity;
        RatingBar mDocRate;
        ImageView myImage, mFullHeart, mHeartBorder;

        ConstraintLayout searchLayout;


        public MyViewHolder (@NonNull View itemView){
            super(itemView);

            myTextView1 = itemView.findViewById(R.id.searchDocName);
            myTextView2 = itemView.findViewById(R.id.searchDocSpec);
            myImage = itemView.findViewById(R.id.searchProfileImg);
            mDocRate = itemView.findViewById(R.id.searchRateAvgDocRow);
            mDocPrice = itemView.findViewById(R.id.docPriceSearchActivity);
            mDocCM = itemView.findViewById(R.id.searchDocCM);
            mDocCity = itemView.findViewById(R.id.searchDocCityAddress);
            mFullHeart = itemView.findViewById(R.id.favImgFullSearch);
            mHeartBorder = itemView.findViewById(R.id.favImgBorderSearch);

            searchLayout = itemView.findViewById(R.id.searchLayout);
        }
    }
}
