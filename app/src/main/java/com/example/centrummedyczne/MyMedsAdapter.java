package com.example.centrummedyczne;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyMedsAdapter extends RecyclerView.Adapter<MyMedsAdapter.MyViewHolder>{

    List<String> medNames, medDetails, medForms;

    Context context;

    public MyMedsAdapter(Context ct, List<String> medNames, List<String> medDetails, List<String> medForms){
        context = ct;
        this.medNames = medNames;
        this.medDetails = medDetails;
        this.medForms = medForms;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.my_med_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.mMedName.setText(medNames.get(position));
        holder.mMedDetails.setText(medDetails.get(position));
        holder.mMedForms.setText(medForms.get(position));

    }

    @Override
    public int getItemCount() {
        return medNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mMedName, mMedDetails, mMedForms;
        ConstraintLayout myMedLayout;

        public MyViewHolder (@NonNull View itemView){
            super(itemView);

            mMedName = itemView.findViewById(R.id.medsNameMyMedRow);
            mMedDetails = itemView.findViewById(R.id.medsDetailsMyMedRow);
            mMedForms = itemView.findViewById(R.id.medsDetails2MyMedRow);

            myMedLayout = itemView.findViewById(R.id.myMedLayout);
        }
    }

}
