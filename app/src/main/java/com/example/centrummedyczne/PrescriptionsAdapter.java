package com.example.centrummedyczne;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PrescriptionsAdapter extends RecyclerView.Adapter<PrescriptionsAdapter.MyViewHolder>{

    private List<String> issueDates, expireDates, docNames, prescDrugs;

    Context context;

    public PrescriptionsAdapter(Context ct, List<String> issueDates, List<String> expireDates,
                                List<String> docNames, List<String> prescDrugs){
        context = ct;
        this.issueDates = issueDates;
        this.expireDates = expireDates;
        this.docNames = docNames;
        this.prescDrugs = prescDrugs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.prescription_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.mIssueDate.setText(issueDates.get(position));
        holder.mExpireDate.setText(expireDates.get(position));
        holder.mDocName.setText(docNames.get(position));
        holder.mPrescDrug.setText(prescDrugs.get(position));

    }

    @Override
    public int getItemCount() {
        return issueDates.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mIssueDate, mExpireDate, mDocName, mPrescDrug;
        ConstraintLayout mPrescLayout;

        public MyViewHolder (@NonNull View itemView){
            super(itemView);

            mIssueDate = itemView.findViewById(R.id.issueDatePrescRow);
            mExpireDate = itemView.findViewById(R.id.expireDatePrescRow);
            mDocName = itemView.findViewById(R.id.docNamePrescRow);
            mPrescDrug =itemView.findViewById(R.id.medsDetailsPrescRow);

            mPrescLayout = itemView.findViewById(R.id.prescriptionLayout);
        }
    }

}
