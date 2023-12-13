package com.example.loginactivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.loginactivity.models.Topic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class UpdateProfileActivity extends AppCompatActivity {
    TextView edtName, edtMail;
    Button btnBack;
    CardView displayName_card, mail_card;
    LinearLayout password_card;
    Uri mUri, avatarUrl;
    ImageView imgAvatar;

    StorageReference storageReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initUI();
        initListener();
        showUserInformation();

    }

    private void initUI() {
        edtName = findViewById(R.id.edt_name);
        edtMail = findViewById(R.id.edt_mail);
        btnBack = findViewById(R.id.btn_backToProfile);
        imgAvatar = findViewById(R.id.imageAvatar);
//        displayName_card = findViewById(R.id.diplayName_card);
//        mail_card = findViewById(R.id.mail_card);
        password_card = findViewById(R.id.password_card);
    }

    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        edtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateDisplayName();
            }
        });
        edtMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickReAuthenticateForEmail();
            }
        });
        password_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickReAuthenticateForPassword();
            }
        });
    }

    private void onClickReAuthenticateForPassword() {
        final Dialog dialog = new Dialog(UpdateProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.password_edit_profile_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        EditText txtCurrentPassword = dialog.findViewById(R.id.current_password);
        EditText txtNewPassword = dialog.findViewById(R.id.new_password);
        EditText txtNewPasswordConfirm = dialog.findViewById(R.id.new_password_confirm);


        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnApply = dialog.findViewById(R.id.btnApply);

        btnCancel.setOnClickListener(view -> dialog.dismiss());
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = txtCurrentPassword.getText().toString().trim();
                String newPassword = txtNewPassword.getText().toString().trim();
                String newPasswordConfirm = txtNewPasswordConfirm.getText().toString().trim();
                if (currentPassword.isEmpty() || newPassword.isEmpty() || newPasswordConfirm.isEmpty()){
                    Toast.makeText(UpdateProfileActivity.this, "Please enter all information.", Toast.LENGTH_SHORT).show();
                }
                else if(!newPassword.equals(newPasswordConfirm)){
                    Toast.makeText(UpdateProfileActivity.this, "Paasword does not match.", Toast.LENGTH_SHORT).show();
                }
                else{
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        //update password
                                        user.updatePassword(newPassword)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            dialog.dismiss();
                                                            Toast.makeText(UpdateProfileActivity.this, "Change password successful.", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            //bat loi khi change password (mat khau yeu chang han)
                                                            Toast.makeText(UpdateProfileActivity.this, task.getException().getMessage(),
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                    else{
                                        // Re-authenticate thất bại
                                        // Xử lý trường hợp re-authenticate không thành công
                                        Toast.makeText(UpdateProfileActivity.this, "Wrong password provided, please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        dialog.show();
    }


    private void onClickReAuthenticateForEmail() {
        final Dialog dialog = new Dialog(UpdateProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_profile_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        EditText edt_password = dialog.findViewById(R.id.edt_new_information);
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);

        txtTitle.setText("Enter your password to continue");

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnApply = dialog.findViewById(R.id.btnApply);

        btnCancel.setOnClickListener(view -> dialog.dismiss());
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edt_password.getText().toString().trim();
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    onClickUpdateEmail();
                                } else {
                                    // Re-authenticate thất bại
                                    // Xử lý trường hợp re-authenticate không thành công
                                    Toast.makeText(UpdateProfileActivity.this, "Re-authentication failed. Please enter the correct password.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        dialog.show();
    }

    private void onClickUpdateEmail() {
        final Dialog dialog = new Dialog(UpdateProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_profile_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        EditText edt_New = dialog.findViewById(R.id.edt_new_information);

        edt_New.setText(user.getEmail());

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnApply = dialog.findViewById(R.id.btnApply);

        btnCancel.setOnClickListener(view -> dialog.dismiss());
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_Email = edt_New.getText().toString().trim();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");

                user.updateEmail(new_Email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Sau khi cập nhật email trên authentication thành công, cập nhật lên realtime
                                    String key = user.getUid();
                                    userRef.child(key + "/verify").setValue(false);
                                    userRef.child(key + "/email").setValue(new_Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialog.dismiss();
                                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseAuth.getInstance().signOut();
                                                    Toast.makeText(UpdateProfileActivity.this, "Please verify your email again to continue!", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(UpdateProfileActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                        }
                                    });

                                } else {
                                    // Cập nhật không thành công do một số nguyên nhân
                                    Exception exception = task.getException();
                                    if (exception instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(UpdateProfileActivity.this, "Email address is already in use by another account", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(UpdateProfileActivity.this, "Error updating user email address", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
        dialog.show();
    }

    private void onClickUpdateDisplayName() {
        final Dialog dialog = new Dialog(UpdateProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_profile_dialog);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        EditText edt_New = dialog.findViewById(R.id.edt_new_information);

        edt_New.setText(user.getDisplayName());

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnApply = dialog.findViewById(R.id.btnApply);

        btnCancel.setOnClickListener(view -> dialog.dismiss());
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentName = user.getDisplayName();
                String new_DisplayName = edt_New.getText().toString().trim();

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");

                userRef.orderByChild("displayName").equalTo(new_DisplayName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //van cho phep dat lai ten cu~
                        if (dataSnapshot.exists() && !user.getDisplayName().equals(new_DisplayName)) {
                            // displayName đã tồn tại
                            Toast.makeText(UpdateProfileActivity.this, "This display name has been taken by another user!", Toast.LENGTH_SHORT).show();
                        } else {
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snap: snapshot.getChildren()){
                                        if(snap.getKey().equals(user.getUid())){
                                            String key = user.getUid();

                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(new_DisplayName)
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            userRef.child(key + "/displayName").setValue(new_DisplayName).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    showUserInformation();
                                                                    dialog.dismiss();
                                                                    Toast.makeText(UpdateProfileActivity.this, "Successfully updated display name!", Toast.LENGTH_LONG).show();
                                                                    DatabaseReference topicRef = FirebaseDatabase.getInstance().getReference("Topic");
                                                                    // cap nhat table Topic
                                                                    topicRef.orderByChild("owner").equalTo(currentName).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            for(DataSnapshot snap : snapshot.getChildren()){
                                                                                //name mới trên authen
                                                                                snap.child("owner").getRef().setValue(user.getDisplayName()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        DatabaseReference folderRef = FirebaseDatabase.getInstance().getReference("Folder");
                                                                                        // cap nhat table folder
                                                                                        folderRef.orderByChild("owner").equalTo(currentName).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                for(DataSnapshot snap : snapshot.getChildren()){
                                                                                                    snap.child("owner").getRef().setValue(user.getDisplayName());
                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                                            }
                                                                                        });

                                                                                    }
                                                                                });
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        dialog.show();
    }

    private void onClickUpdateUserName_Img() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String displayName = edtName.getText().toString().trim();
                Uri photoUrl = mUri;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null){
            return;
        }
        String fullName = edtName.getText().toString().trim();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateProfileActivity.this, "Update profile success !",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void showUserInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        else {
            String name = user.getDisplayName();
            String email = user.getEmail();
            edtName.setText(name);
            edtMail.setText(email);
            Intent intent = getIntent();
            avatarUrl = Uri.parse(intent.getStringExtra("avatarURL"));
            Glide.with(this).load(avatarUrl).error(R.drawable.ic_avatar_default).into(imgAvatar);
        }
    }
}