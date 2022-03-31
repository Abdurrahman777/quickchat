package com.ps.qc.Adapter;

import static com.ps.qc.Activity.ChatActivity.rImage;
import static com.ps.qc.Activity.ChatActivity.sImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.ps.qc.Activity.ChatActivity;
import com.ps.qc.Model.MessegesDTO;
import com.ps.qc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter {

    Context mContext;
    ArrayList<MessegesDTO>messegesDTOArrayList;

    public MessageAdapter(Context mContext, ArrayList<MessegesDTO> messegesDTOArrayList) {
        this.mContext = mContext;
        this.messegesDTOArrayList = messegesDTOArrayList;
    }

    int ITEM_SEND=1;
    int ITEM_RECEIVE=2;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_SEND)
        {
            View view= LayoutInflater.from(mContext).inflate(R.layout.senderlayout,parent,false);
            return  new SenderViewHolder(view);
        }
        else
        {

            View view= LayoutInflater.from(mContext).inflate(R.layout.receiverlayout,parent,false);
            return  new ReceiverViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

     MessegesDTO messegesDTO=messegesDTOArrayList.get(position);
     if (holder.getClass()==SenderViewHolder.class)
     {
         SenderViewHolder viewHolder=( SenderViewHolder)   holder;
     viewHolder.nameTV.setText(messegesDTO.getMessage());

         Picasso.get().load(sImage).into(viewHolder.imageIV);


     }
     else
     {

         ReceiverViewHolder viewHolder=( ReceiverViewHolder)   holder;

         viewHolder.nameTV2.setText(messegesDTO.getMessage());
         Picasso.get().load(rImage).into(viewHolder.imageIV2);
     }





    }

    @Override
    public int getItemCount() {
        return messegesDTOArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

        MessegesDTO messegesDTO= messegesDTOArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messegesDTO.getSenderId()))
        {
            return  ITEM_SEND;
        }
        else
        {
            return  ITEM_RECEIVE;
        }
    }


    class SenderViewHolder extends  RecyclerView.ViewHolder
    {
        CircleImageView imageIV;
        TextView  nameTV;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIV=itemView.findViewById(R.id.imageIV);
            nameTV=itemView.findViewById(R.id.nameTV);


        }
    }

    class ReceiverViewHolder extends  RecyclerView.ViewHolder
    {
        CircleImageView imageIV2;
        TextView  nameTV2;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
           imageIV2=itemView.findViewById(R.id.imageIV2);
           nameTV2=itemView.findViewById(R.id.nameTV2);


        }
    }

}
