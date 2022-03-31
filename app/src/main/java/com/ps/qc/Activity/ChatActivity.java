package com.ps.qc.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ps.qc.Adapter.MessageAdapter;
import com.ps.qc.Model.MessegesDTO;
import com.ps.qc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
  private  String TAG="ChatActivity";
      String ReceiverName;
      String ReceiverImage;
      String ReceiverUID;
      String SenderUID;
     ImageView profileimage;
     TextView receiver_name;

       CardView sendBT;
       EditText editmessege;
       RecyclerView messgeRV;

     FirebaseDatabase database;
     FirebaseAuth firebaseAuth;


    public static  String sImage;
     public static  String rImage;

     MessageAdapter messageAdapter;


      String senderRoom,receiverRoom;
     ArrayList<MessegesDTO>messegesDTOArrayList;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
         ReceiverName=getIntent().getStringExtra("name");
         ReceiverImage=getIntent().getStringExtra("ReciverImage");
         ReceiverUID=getIntent().getStringExtra("uid");
         messegesDTOArrayList= new ArrayList<>();




        database=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();


        profileimage= findViewById(R.id.profileimage);
        receiver_name=findViewById(R.id.receiver_name);
         sendBT=findViewById(R.id.sendBT);
         editmessege=findViewById(R.id.editmessege);
        messgeRV=findViewById(R.id.messgeRV);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messgeRV.setLayoutManager(linearLayoutManager);
         messageAdapter = new MessageAdapter(ChatActivity.this,messegesDTOArrayList);
        messgeRV.setAdapter(messageAdapter);





        Picasso.get().load(ReceiverImage).into(profileimage);
        receiver_name.setText(""+ReceiverName);

       SenderUID=firebaseAuth.getUid();

       senderRoom=SenderUID+ReceiverUID;
       receiverRoom=ReceiverUID+SenderUID;



    DatabaseReference reference=database.getReference().child("user").child(firebaseAuth.getUid());

        DatabaseReference chatreference=database.getReference().child("chats").child(senderRoom).child("messages");

           chatreference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {

                   messegesDTOArrayList.clear();
                   for (DataSnapshot datasnapshot:snapshot.getChildren())
                   {
                     MessegesDTO messegesDTO=datasnapshot.getValue(MessegesDTO.class);
                       messegesDTOArrayList.add(messegesDTO);
                   }
                   messageAdapter.notifyDataSetChanged();
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });

        reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
           sImage= snapshot.child("imageUri").getValue().toString();
           rImage=ReceiverImage;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    });

 sendBT.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
            String message=editmessege.getText().toString();
             messegesDTOArrayList.clear();
            if (message.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Please Enter Valid Message", Toast.LENGTH_SHORT).show();
                return;
            }
            editmessege.setText("");
            Date date= new Date();
//              Message messages=  new Message(messages,firebaseAuth.getUid(),date.getTime());
               MessegesDTO messages= new MessegesDTO(message,SenderUID,date.getTime());
         database=FirebaseDatabase.getInstance();
         database.getReference().child("chats")
                  .child(senderRoom)
                 .child("messages")
                 .push()
                 .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 database.getReference().child("chats")
                         .child(receiverRoom)
                         .child("messages")
                         .push()
                         .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {

                     }
                 });

             }
         });
    }
});

    }
}