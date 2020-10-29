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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.MyViewHolder> {


    List<String> data1, data2;
    List<Integer> images;
    Context context;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference specializations = db.collection("specialization");
    private final CollectionReference docHasSpec = db.collection("doctor_has_specialization");
    private final CollectionReference doctors = db.collection("doctor");
    private final CollectionReference clinics = db.collection("clinic");
    private final CollectionReference address = db.collection("address");

    public SearchRecyclerAdapter(Context ct, List<String> s1, List<String> s2, List<Integer> img){
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        String docId = data1.get(position);

        doctors.document(docId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Doctor foundDoctor = documentSnapshot.toObject(Doctor.class);
                holder.myTextView1.setText(foundDoctor.getDegree() + " "
                        + foundDoctor.getFirst_name() + " "
                        + foundDoctor.getLast_name());
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
                                docSpecs.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String docSpec= documentSnapshot.getString("specialization_name");
                                            System.out.println(docSpec);
                                            String currentSpec = "";

                                            currentSpec = holder.myTextView2.getText().toString();
                                            holder.myTextView2.setText(docSpec);
                                            /*if(currentSpec.equals("")){
                                                holder.myTextView2.setText(docSpec);

                                            }
                                            else {
                                                holder.myTextView2.setText(currentSpec +", "+ docSpec);

                                            }*/

                                            }
                                        });
                                }
                            }
                            }
                        });
            }
        });


        //holder.myTextView2.setText(data2.get(position));
        holder.myImage.setImageResource(images.get(position));
        holder.searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorActivity.class);
                intent.putExtra("data1", data1.get(position));
                intent.putExtra("data2", data2.get(position));
                intent.putExtra("images", images.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myTextView1, myTextView2;
        ImageView myImage;

        ConstraintLayout searchLayout;


        public MyViewHolder (@NonNull View itemView){
            super(itemView);

            myTextView1 = itemView.findViewById(R.id.searchDocName);
            myTextView2 = itemView.findViewById(R.id.searchDocSpec);
            myImage = itemView.findViewById(R.id.searchProfileImg);

            searchLayout = itemView.findViewById(R.id.searchLayout);
        }
    }
}
