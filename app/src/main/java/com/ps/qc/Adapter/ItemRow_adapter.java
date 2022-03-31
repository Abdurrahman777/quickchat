package com.ps.qc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ps.qc.Activity.ChatActivity;
import com.ps.qc.Model.Users;
import com.ps.qc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemRow_adapter extends RecyclerView.Adapter<ItemRow_adapter.ViewHolder> {
  Context mContext;
    ArrayList<Users>usersArrayList;

//    public ItemRow_adapter(HomeActivity homeActivity, ArrayList<Users> usersArrayList) {
//    }

    public  ItemRow_adapter(Context mContext,ArrayList<Users>usersArrayList)
    {
       this.mContext=mContext;
       this.usersArrayList=usersArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

     View view= LayoutInflater.from(mContext).inflate(R.layout.item_user_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     Users users=usersArrayList.get(position);
     holder.nameTV.setText(usersArrayList.get(position).getName());
     holder.statusTV.setText(usersArrayList.get(position).getStatus());
     Picasso.get().load(usersArrayList.get(position).getImageUri()).into(holder.imageIV);

     holder.itemrow.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent= new Intent(mContext, ChatActivity.class);
            intent.putExtra("name",users.getName());
            intent.putExtra("ReciverImage",users.getImageUri());
            intent.putExtra("uid",users.getUid());

              mContext.startActivity(intent);
         }
     });





    }

    @Override
    public int getItemCount()
    {
        return usersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIV;
        TextView nameTV,statusTV;
        LinearLayout itemrow;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageIV=itemView.findViewById(R.id.imageIV);
            nameTV=itemView.findViewById(R.id.nameTV);
            statusTV=itemView.findViewById(R.id.statusTV);
            itemrow=itemView.findViewById(R.id.itemrow);


        }
    }
}
