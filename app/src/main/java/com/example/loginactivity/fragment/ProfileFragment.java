package com.example.loginactivity.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.loginactivity.FragmentCallback;
import com.example.loginactivity.LoginActivity;
import com.example.loginactivity.MainActivity;
import com.example.loginactivity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    TextView txtName, txtEmail;
    ImageView imgAvatar;
    Button btnSignout, btnUpdate;
    private FragmentCallback callBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (FragmentCallback) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.fragment_profile, container, false);

        initUI(mview);
        initListener();
        showUserInformation();

        return mview;
    }

    private void initUI(View mview) {
        txtName = mview.findViewById(R.id.txtName);
        txtEmail = mview.findViewById(R.id.txtEmail);
        imgAvatar = mview.findViewById(R.id.img_avatar);
        btnSignout = mview.findViewById(R.id.btn_signOut);
        btnUpdate = mview.findViewById(R.id.btn_update);
    }

    private void initListener() {
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBack!=null) {
                    callBack.goTo(3);
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
                txtName.setVisibility(View.GONE);
            } else {
                txtName.setText(name);
            }
            txtEmail.setText(email);
            Glide.with(this).load(photoUrl).error(R.drawable.ic_avatar_default).into(imgAvatar);
        }
    }
}