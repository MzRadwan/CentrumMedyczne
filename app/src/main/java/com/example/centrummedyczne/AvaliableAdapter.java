package com.example.centrummedyczne;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class AvaliableAdapter extends RecyclerView.Adapter<AvaliableAdapter.MyViewHolder>{

    private List<String> visitDates;
    private List<String> visitRefs;

    Context context;

    public AvaliableAdapter(Context ct, List<String> visitDates, List<String> visitRefs){
        context = ct;
        this.visitDates = visitDates;
        this.visitRefs = visitRefs;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.avaliable_appointment_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.mVisitDate.setText(visitDates.get(position));
        holder.mVisitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AppointmentConfirmActivity.class);
                intent.putExtra("visitId", visitRefs.get(position));
                intent.putExtra("visitDate", visitDates.get(position));
                context.startActivity(intent);
                //holder.mVisitDate.setBackgroundResource(R.drawable.sky_button);
               // holder.mBookingButton.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return visitRefs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        Button mVisitDate, mBookingButton;
        ConstraintLayout avaliableLayout;

        public MyViewHolder (@NonNull View itemView){
            super(itemView);

            mVisitDate = itemView.findViewById(R.id.avaliableAppointmentButton);
           // mBookingButton = itemView.findViewById(R.id.bookVisitBooking);

            avaliableLayout = itemView.findViewById(R.id.avaliableAppointmentLayout);
        }
    }

}
