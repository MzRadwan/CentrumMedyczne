package com.example.centrummedyczne;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{

    private List<String> docIds, docNames, docSpecs, docCMs, docCMAddress;
    private List<Integer> docImgs;

    Context context;

    public MessageAdapter(Context ct, List<String> docIds, List<String> docNames,
                              List<String> docSpecs, List<String> docCMs,
                              List<String> docCMAddress, List<Integer> docImgs){
        context = ct;
        this.docIds = docIds;
        this.docNames = docNames;
        this.docSpecs = docSpecs;
        this.docCMAddress = docCMAddress;
        this.docCMs = docCMs;
        this.docImgs = docImgs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.ask_doc_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.mDocName.setText(docNames.get(position));
        if (docSpecs.size() == docIds.size())
            holder.mDocSpec.setText(docSpecs.get(position));
        if (docCMs.size() == docIds.size())
            holder.mDocCM.setText(docCMs.get(position));
        if (docCMAddress.size() == docIds.size())
            holder.mDocCity.setText(docCMAddress.get(position));


    }

    @Override
    public int getItemCount() {
        return docIds.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDocName, mDocSpec, mDocCM, mDocCity;
        ConstraintLayout askDocLayout;
        ImageView mDocImg;


        public MyViewHolder (@NonNull View itemView){
            super(itemView);

            mDocName = itemView.findViewById(R.id.askDocName);
            mDocSpec = itemView.findViewById(R.id.askDocSpec);
            mDocCM = itemView.findViewById(R.id.askDocCM);
            mDocCity = itemView.findViewById(R.id.askDocCityAddress);
            mDocImg = itemView.findViewById(R.id.askProfileImg);

            askDocLayout = itemView.findViewById(R.id.askDocLayout);
        }
    }

}
