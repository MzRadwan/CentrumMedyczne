package com.example.centrummedyczne;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlannedVisitAdapter extends RecyclerView.Adapter<PlannedVisitAdapter.MyViewHolder>{

    private List<String> appointmentDates, docNames, docSpecs, docCMs, docCMCities, visitRefs;
    private List<Integer> docImgs;
    Context context;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference appointmentCol = db.collection("appointment");

    public PlannedVisitAdapter(Context ct, List<String> appointmentDates, List<String> docNames,
                               List<String> docSpecs, List<String> docCMs, List<String> docCMCities,
                               List<String> visitRefs, List<Integer> docImgs){
        context = ct;
        this.appointmentDates = appointmentDates;
        this.docNames = docNames;
        this.docSpecs = docSpecs;
        this.docCMs = docCMs;
        this.docCMCities =docCMCities;
        this.visitRefs = visitRefs;
        this.docImgs = docImgs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.planned_visit_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.mDateTime.setText(appointmentDates.get(position));
        holder.mDocName.setText(docNames.get(position));
        holder.mDocSpec.setText(docSpecs.get(position));
        holder.mDocCM.setText(docCMs.get(position));
        holder.mDocCity.setText(docCMCities.get(position));
        holder.mDocImg.setImageResource(docImgs.get(position));
        holder.mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mCancel.setVisibility(View.GONE);
                holder.mAreYouSure.setVisibility(View.VISIBLE);
                holder.mCancelConfirm.setVisibility(View.VISIBLE);
                holder.mCancelConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelAppointment(visitRefs.get(position));
                    }
                });
                holder.mDoNotCancel.setVisibility(View.VISIBLE);
                holder.mDoNotCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.mCancel.setVisibility(View.VISIBLE);
                        holder.mAreYouSure.setVisibility(View.GONE);
                        holder.mCancelConfirm.setVisibility(View.GONE);
                        holder.mDoNotCancel.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    private void cancelAppointment(String visitId){
        Map<String, Object> visit = new HashMap<>();
        visit.put("booked", false);
        visit.put("patient_id", null);
        visit.put("notification_sent", false);
        appointmentCol.document(visitId).update(visit)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Wizyta odwołana", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PlannedVisitsActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return visitRefs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDocName, mDocSpec, mDateTime, mDocCM, mDocCity, mAreYouSure;
        ConstraintLayout plannedLayout;
        ImageView mDocImg;
        Button mCancel, mCancelConfirm, mDoNotCancel;

        public MyViewHolder (@NonNull View itemView){
            super(itemView);

            mDocName = itemView.findViewById(R.id.plannedDocName);
            mDocSpec = itemView.findViewById(R.id.plannedDocSpec);
            mDateTime = itemView.findViewById(R.id.plannedVisitDatetime);
            mDocCM = itemView.findViewById(R.id.plannedDocCM);
            mDocCity = itemView.findViewById(R.id.plannedDocCityAddress);
            mDocImg = itemView.findViewById(R.id.plannedProfileImg);
            mCancel = itemView.findViewById(R.id.cancelPlannedVisit);
            mCancelConfirm = itemView.findViewById(R.id.cancelConfirmPlannedVisit);
            mDoNotCancel = itemView.findViewById(R.id.doNotCancelVisit);
            mAreYouSure = itemView.findViewById(R.id.areYouSureText);

            plannedLayout = itemView.findViewById(R.id.plannedLayout);
        }
    }

}
