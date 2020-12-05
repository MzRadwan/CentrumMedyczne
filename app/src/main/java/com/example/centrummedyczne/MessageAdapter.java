package com.example.centrummedyczne;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{

    private List<Message> messageList;

    Context context;

    public MessageAdapter(Context ct, List<Message> messageList){
        context = ct;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.message_row, parent, false);
        RecyclerView.LayoutParams lp= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        LinearLayoutCompat.LayoutParams params = (LinearLayoutCompat.LayoutParams) holder.messageRowLayout.getLayoutParams();
        Message message = messageList.get(position);
        if (message.isTo_doctor()){
            params.gravity = Gravity.END;
            params.rightMargin = 0;
            params.leftMargin = 200;
            holder.mText.setBackgroundResource(R.drawable.sky_message_background);
        }
        else {
            params.gravity = Gravity.START;
            params.leftMargin = 0;
            params.rightMargin = 200;
            holder.mText.setBackgroundResource(R.drawable.white_message_background);
        }
        holder.messageRowLayout.setLayoutParams(params);
        holder.mText.setText(message.getText());

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mText;
        LinearLayoutCompat messageRowLayout;

        public MyViewHolder (@NonNull View itemView){
            super(itemView);

            mText = itemView.findViewById(R.id.messageTextRow);

            messageRowLayout = itemView.findViewById(R.id.messageRowLayout);
        }
    }

}
