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

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.MyViewHolder> {


    String data1[], data2[];
    int images[];
    Context context;

    public SearchRecyclerAdapter(Context ct, String s1[], String s2[], int img[]){
        context = ct;
        data1 = s1;
        data2 = s2;
        images = img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.search_doc_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.mytextView1.setText((data1[position]));
        holder.myTextView2.setText(data2[position]);
        holder.myImage.setImageResource(images[position]);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorActivity.class);
                intent.putExtra("data1", data1[position]);
                intent.putExtra("data2", data2[position]);
                intent.putExtra("images", images[position]);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mytextView1, myTextView2;
        ImageView myImage;

        ConstraintLayout mainLayout;


        public MyViewHolder (@NonNull View itemView){
            super(itemView);

            mytextView1 = itemView.findViewById(R.id.searchDocName);
            myTextView2 = itemView.findViewById(R.id.searchDocSpec);
            myImage = itemView.findViewById(R.id.searchProfileImg);

            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
