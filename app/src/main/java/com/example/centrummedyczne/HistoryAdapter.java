package com.example.centrummedyczne;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

import static com.example.centrummedyczne.R.*;
import static com.example.centrummedyczne.R.color.*;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{

    private List<String> docNames, docSpecs, appointmentDates,
            docCms, docAddresses, patientNotes, opinions;
    private List<Float> rates;
    private List<Integer> docImages;
    private List<String> visitRefs;

    Context context;

    public HistoryAdapter(Context ct, List<String> docNames,List<String> docSpecs,
                          List<String> appointmentDates, List<String> docCms,
                          List<String> docAddresses, List<String> patientNotes,
                          List<String> opinions, List<Float> rates,
                          List<Integer> docImages, List<String> visitRefs){
        context = ct;
        this.docNames = docNames;
        this.docSpecs = docSpecs;
        this.appointmentDates = appointmentDates;
        this.docCms = docCms;
        this.docAddresses = docAddresses;
        this.patientNotes = patientNotes;
        this.opinions = opinions;
        this.rates = rates;
        this.docImages = docImages;
        this.visitRefs = visitRefs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(layout.history_visit_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.mVisitDate.setText(appointmentDates.get(position));
        holder.mDocName.setText(docNames.get(position));
        if (docSpecs.size() == docNames.size())
            holder.mDocSpec.setText(docSpecs.get(position));
        if (docImages.size() == docNames.size())
            holder.mProfileImg.setImageResource(docImages.get(position));
        if (patientNotes.size() == docNames.size())
            holder.mPatientNote.setText(patientNotes.get(position));
        holder.mDocCm.setText(docCms.get(position));
        holder.mCMAddress.setText(docAddresses.get(position));

        if (opinions.size() == docNames.size()){
            if(opinions.get(position).equals("Nie wystawiono opinii")) {
                holder.mOpinion.setVisibility(View.GONE);
                holder.mOpinionHeader.setText("Nie wystawiono opinii");
                holder.mAddReview.setVisibility(View.VISIBLE);
            }
            else if(!opinions.get(position).equals(" ")){
                holder.mOpinion.setText(opinions.get(position));
                holder.mAddReview.setVisibility(View.GONE);
                holder.mOpinion.setVisibility(View.VISIBLE);

            }
        }

        System.out.println("His Rate:" + rates.get(position));
        if (rates.size() == docNames.size()){
            if(rates.get(position) == -1
                    ) {
                holder.mRateHeader.setText("Nie wystawiono oceny");
                holder.mRate.setVisibility(View.GONE);
                holder.mAddReview.setVisibility(View.VISIBLE);
            }
            else {
                holder.mRate.setVisibility(View.VISIBLE);
                holder.mRate.setRating(rates.get(position));
                holder.mAddReview.setVisibility(View.GONE);
            }
        }

        holder.mAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RateVisitActivity.class);
                intent.putExtra("appointment_id", visitRefs.get(position));
                intent.putExtra("docName", docNames.get(position));
                intent.putExtra("docSpec", docSpecs.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return docNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDocName, mDocSpec, mDocCm, mVisitDate,
                mCMAddress, mPatientNote, mOpinion, mRateHeader, mOpinionHeader;
        ConstraintLayout historyLayout;
        ImageView mProfileImg;
        RatingBar mRate;
        Button mAddReview;

        public MyViewHolder (@NonNull View itemView){
            super(itemView);

            mDocName = itemView.findViewById(id.docNameHistory);
            mDocSpec = itemView.findViewById(id.docSpecHistory);
            mDocCm = itemView.findViewById(id.docCMHistory);
            mVisitDate = itemView.findViewById(id.visitDateHistory);
            mCMAddress = itemView.findViewById(id.cmAddressHistory);
            mProfileImg = itemView.findViewById(id.docImgHistory);
            mPatientNote = itemView.findViewById(id.noteHistory);
            mOpinion = itemView.findViewById(id.opinionTextHistory);
            mRateHeader = itemView.findViewById(id.yourRating);
            mOpinionHeader = itemView.findViewById(id.yourReview);
            mRate = itemView.findViewById(id.ratingBarHistory);

            mAddReview = itemView.findViewById(id.rateVisitNowHistory);

            historyLayout = itemView.findViewById(id.historyLayout);
        }
    }

}
