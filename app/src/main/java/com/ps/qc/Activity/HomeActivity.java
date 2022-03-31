package com.ps.qc.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ps.qc.Adapter.ItemRow_adapter;
import com.ps.qc.R;
import com.ps.qc.Model.Users;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView mainuserRecyclerview;
    FirebaseDatabase database;
    ItemRow_adapter itemRow_adapter;
    ArrayList<Users> usersArrayList;
    ImageView logoutIV, settings;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mainuserRecyclerview = findViewById(R.id.mainuserRecyclerview);
        logoutIV=findViewById(R.id.logoutIV);
        settings=findViewById(R.id.settings);
        linearLayout=findViewById(R.id.toolbar);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("user");
        usersArrayList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                  Users users=dataSnapshot.getValue(Users.class);
                  usersArrayList.add(users);

                }
                itemRow_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//     logoutIV.setOnClickListener(new View.OnClickListener() {
//         @Override
//         public void onClick(View view) {
//             Dialog dialog= new Dialog(HomeActivity.this);
//             dialog.setContentView(R.layout.dialog_layout);
//             Button cancelBT,okBT;
//             cancelBT=dialog.findViewById(R.id.cancelBT);
//              okBT=dialog.findViewById(R.id.okBT);
//
//
//
//              okBT.setOnClickListener(new View.OnClickListener() {
//                  @Override
//                  public void onClick(View view) {
//                      FirebaseAuth.getInstance().signOut();
//                      startActivity(new Intent(HomeActivity.this,RegisterActivity.class));
//                  }
//              });
//            cancelBT.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                }
//            });
//
//
//
//
//         }
//     });
        settings.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     startActivity( new Intent(getApplicationContext(),SettingsActivity.class));
                 }
             });
        logoutIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), " this is a logout", Toast.LENGTH_SHORT).show();
            }
        });

//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), " this is a settings", Toast.LENGTH_SHORT).show();
//            }
//        });







        mainuserRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        itemRow_adapter= new ItemRow_adapter(HomeActivity.this,usersArrayList);
        mainuserRecyclerview.setAdapter(itemRow_adapter);


        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(HomeActivity.this, RegisterActivity.class);
            startActivity(intent);
        }

    }
}