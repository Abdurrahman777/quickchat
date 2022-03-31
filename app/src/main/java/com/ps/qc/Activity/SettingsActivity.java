package com.ps.qc.Activity;

import static com.ps.qc.Activity.ChatActivity.sImage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.ps.qc.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
   CircleImageView imageIV;
   TextView nameTV, statusTV;
   Button saveBT;

   FirebaseAuth auth;
   FirebaseDatabase database;
   FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    imageIV=findViewById(R.id.imageIV);
    nameTV=findViewById(R.id.nameTV);
    statusTV=findViewById(R.id.statusTV);
    saveBT=findViewById(R.id.saveBT);

    auth=FirebaseAuth.getInstance();
    database=FirebaseDatabase.getInstance();
    storage=FirebaseStorage.getInstance();


        DatabaseReference reference= database.getReference().child("user").child(auth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String email=snapshot.child("email").getValue().toString();
                String name=snapshot.child("name").getValue().toString();
                String status=snapshot.child("status").getValue().toString();
                String image=snapshot.child("imageUri").getValue().toString();


                nameTV.setText(name);
                statusTV.setText(status);
                Picasso.get().load(image).into(imageIV);







            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    saveBT.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    });

    }
}