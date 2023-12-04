package com.example.loginactivity.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.loginactivity.FragmentCallback;
import com.example.loginactivity.LoginActivity;
import com.example.loginactivity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateProfileFragment extends Fragment {
    EditText edtName, edtMail;
    ImageView imgAvatar;

    Button btnConfirm,btnBack;

    private FragmentCallback callBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callBack = (FragmentCallback) context;
        } catch (ClassCastException castException) {
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.fragment_update_profile, container, false);

        initUI(mview);
        initListener();
        showUserInformation();

        return mview;
    }

    private void initUI(View mview) {
        edtName = mview.findViewById(R.id.edt_name);
        edtMail = mview.findViewById(R.id.edt_mail);
        imgAvatar = mview.findViewById(R.id.edt_avatar);
        btnBack = mview.findViewById(R.id.btn_back);
        btnConfirm = mview.findViewById(R.id.btn_confirm);
    }

    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    callBack.goBack();
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
            Uri photoUrl = user.getPhotoUrl();

            if(name == null){
                edtName.setText("");
            } else {
                edtName.setText(name);
            }
            edtMail.setText(email);
            Glide.with(this).load(photoUrl).error(R.drawable.ic_avatar_default).into(imgAvatar);
        }
    }
}