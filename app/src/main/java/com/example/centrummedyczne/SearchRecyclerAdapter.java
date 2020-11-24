package com.example.centrummedyczne;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.MyViewHolder> {


    List<String> data1, data2, images, docNames, docInfos, docCMs, docCities, docReviews;
    List<Integer>  rateCounters;
    List<Float> docRates, docPrices;
    List<Boolean> favourites;

    Context context;

    public SearchRecyclerAdapter(Context ct, List<String> s1, List<String> s2,
                                 List<String> img, List<Float> docRates, List<Float> docPrices,
                                 List<String> docNames, List<String> docInfos, List<String> docCMs,
                                 List<String> docCities, List<Boolean> favourites,
                                 List<Integer> rateCounters, List<String> docReviews){
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
        //this.opinionCounters = opinionCounters;
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        holder.myTextView1.setText(docNames.get(position));
        holder.myTextView2.setText(data2.get(position));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        String noImage = "https://firebasestorage.googleapis.com/v0/b/centrum-medyczne-8367d.appspot.com/o/doctors%2F1606218227891.png?alt=media&token=73b5f128-40c4-4ff2-9179-4145c9daab39";

        if(!images.get(position).equals(noImage)) {
            StorageReference s = storage.getReferenceFromUrl(images.get(position));
            s.getBytes(1024 * 1024)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            holder.myImage.setImageBitmap(bitmap);
                        }
                    });
        }
        // holder.myImage.setImageResource(images.get(position));
        holder.mDocRate.setRating(docRates.get(position));
        holder.mDocPrice.setText("" + String.format("Cena za wizytę: %.2f PLN", docPrices.get(position)) + "");
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
               // intent.putExtra("opinionCounter", opinionCounters.get(position));
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
