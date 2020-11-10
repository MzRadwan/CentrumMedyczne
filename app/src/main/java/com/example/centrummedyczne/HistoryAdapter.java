package com.example.centrummedyczne;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{

    private List<String> docNames, docSpecs, appointmentDates,
            docCms, docAddresses, patientNotes, opinions;
    private List<Float> rates;
    private List<Integer> docImages;

    Context context;

    public HistoryAdapter(Context ct, List<String> docNames,List<String> docSpecs,
                          List<String> appointmentDates, List<String> docCms,
                          List<String> docAddresses, List<String> patientNotes,
                          List<String> opinions, List<Float> rates, List<Integer> docImages){
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

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.history_visit_row, parent, false);
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
        holder.mDocCm.setText(docCms.get(position));
        holder.mCMAddress.setText(docAddresses.get(position));

    }

    @Override
    public int getItemCount() {
        return docNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDocName, mDocSpec, mDocCm, mVisitDate,
                mCMAddress, mPatientNote, mOpinion;
        ConstraintLayout historyLayout;
        ImageView mProfileImg;
        RatingBar mRate;

        public MyViewHolder (@NonNull View itemView){
            super(itemView);

            mDocName = itemView.findViewById(R.id.docNameHistory);
            mDocSpec = itemView.findViewById(R.id.docSpecHistory);
            mDocCm = itemView.findViewById(R.id.docCMHistory);
            mVisitDate = itemView.findViewById(R.id.visitDateHistory);
            mCMAddress = itemView.findViewById(R.id.cmAddressHistory);
            mProfileImg = itemView.findViewById(R.id.docImgHistory);
            mPatientNote = itemView.findViewById(R.id.patientsNote);
            mOpinion = itemView.findViewById(R.id.yourReview);
            mRate = itemView.findViewById(R.id.ratingBarHistory);

            historyLayout = itemView.findViewById(R.id.historyLayout);
        }
    }

}
