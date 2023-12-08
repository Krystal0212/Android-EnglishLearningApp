package com.example.loginactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginactivity.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword, edtdisplayName, edtPasswordConfirm;
    Button btnSignup;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initUI();
        initListener();
    }
    private void initUI() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtPasswordConfirm = findViewById(R.id.edtConfirmPassword);
        btnSignup = findViewById(R.id.btnSignUp);
        edtdisplayName = findViewById(R.id.edtName);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initListener() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignup();
            }
        });
    }

    private void onClickSignup() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String passwordConfirm = edtPasswordConfirm.getText().toString().trim();
        String displayName = edtdisplayName.getText().toString().trim();
        Uri photoUrl =  Uri.parse( "https://firebasestorage.googleapis.com/v0/b/finaltermandroid-ba01a.appspot.com/o/icons8-avatar-64.png?alt=media&token=efb2e06d-589a-40f0-96a0-a1eddfdbb352" );
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        if(email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() || displayName.isEmpty()){
            Toast.makeText(SignUpActivity.this, "Please fill out all fields !",
                    Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        } else if (!password.equals(passwordConfirm)) {
            Toast.makeText(SignUpActivity.this, "Password does not matched !",
                    Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        else{
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");
            userRef.orderByChild("displayName").equalTo(displayName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // displayName đã tồn tại
                        Toast.makeText(SignUpActivity.this, "This display name has been taken by another user!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        // Tạo người dùng mới
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            user.sendEmailVerification()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(SignUpActivity.this, "Verification mail has been sent!", Toast.LENGTH_LONG).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SignUpActivity.this, "Failed: email not sent!", Toast.LENGTH_LONG).show();
                                                            //restart this activity
                                                            overridePendingTransition(0, 0);
                                                            finish();
                                                            overridePendingTransition(0, 0);
                                                            startActivity(getIntent());

                                                        }
                                                    });
                                            //cap nhat thong tin user len authentication
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(displayName)
                                                    .setPhotoUri(photoUrl)
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            //cap nhat thong tin user len realtime db
                                                            if (task.isSuccessful()) {

                                                                //user tím này là user từ authentication
                                                                String key = user.getUid();
                                                                User newUser = new User(user.getEmail(), user.getDisplayName(), "https://firebasestorage.googleapis.com/v0/b/finaltermandroid-ba01a.appspot.com/o/icons8-avatar-64.png?alt=media&token=efb2e06d-589a-40f0-96a0-a1eddfdbb352", false, key);
                                                                userRef.child(key).setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        FirebaseAuth.getInstance().signOut();
                                                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(SignUpActivity.this, "Error checking Display Name existence", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}