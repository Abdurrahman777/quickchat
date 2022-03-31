package com.ps.qc.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ps.qc.R;
import com.ps.qc.Model.Users;

public class RegisterActivity extends AppCompatActivity {

    EditText enternameET, enteremailET, enterPasswordET, enterCPasswordET;
    Button RegisterBT;
    TextView signInTV;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;
    ImageView profileimage;
    Uri imageUri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageURI;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        enternameET = findViewById(R.id.enternameET);
        enteremailET = findViewById(R.id.enteremailET);
        enterPasswordET = findViewById(R.id.enterPasswordET);
        enterCPasswordET = findViewById(R.id.enterCPasswordET);
        RegisterBT = findViewById(R.id.RegisterBT);
        signInTV = findViewById(R.id.signInTV);
        profileimage = findViewById(R.id.profileimage);

        signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        RegisterBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name = enternameET.getText().toString();
                String email = enteremailET.getText().toString();
                String password = enterPasswordET.getText().toString();
                String Cpassword = enterCPasswordET.getText().toString();

                 String status="hey im using this application";



                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(Cpassword) || TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter valid Data", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!email.matches(emailPattern)) {
                    enteremailET.setError("Invalid Email");
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                } else if (password.length() < 6) {
                    enterPasswordET.setError("Invalid Password");
                    Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                } else if (!Cpassword.matches(password)) {
                    enterPasswordET.setError("Password do not match");
                    Toast.makeText(getApplicationContext(), "Password do not match", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                } else if (name.length() < 1) {
                    Toast.makeText(getApplicationContext(), "enter name", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                } else {

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();

                                DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                                if (imageUri != null) {
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful())
                                                progressDialog.dismiss();

                                            {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI = uri.toString();
                                                        Users users = new Users(auth.getUid(), name, email, imageURI,status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    progressDialog.dismiss();
                                                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "error in code ", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {
//                                  imageURI=uri.toString();
                                    String status="hey im using this application";
                                    imageURI = "https://firebasestorage.googleapis.com/v0/b/quickchat-6223e.appspot.com/o/user.png?alt=media&token=5d5366ea-96a3-4a9f-9c6d-dbbddfa87ab4";
                                    Users users = new Users(auth.getUid(), name, email, imageURI,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                            } else {
                                                Toast.makeText(getApplicationContext(), "error in code ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "somthing went rong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data != null) {
                imageUri = data.getData();
                profileimage.setImageURI(imageUri);
            }
        }
    }
}