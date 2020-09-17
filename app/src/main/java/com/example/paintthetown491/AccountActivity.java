package com.example.paintthetown491;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends Fragment
{
    private ImageView profilePic;
    private Button uploadPic, editInfo, saveInfo;
    private EditText userFullName, userE, userName, phone;
    public static final int PICK_IMAGE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.frag_account, container, false);
        profilePic=view.findViewById(R.id.profilePic);
        uploadPic=view.findViewById(R.id.uploadPic);
        userFullName=view.findViewById(R.id.userName);
        userFullName.setEnabled(false);
        userE=view.findViewById(R.id.userEmail);
        userE.setEnabled(false);
        userName=view.findViewById(R.id.userN);
        userName.setEnabled(false);
        phone=view.findViewById(R.id.phoneNumber);
        phone.setEnabled(false);
        loadUserData(FirebaseDbSingleton.getInstance().user.getUid());
        uploadPic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select a picture"),PICK_IMAGE);
            }
        });
        saveInfo=view.findViewById(R.id.saveInfoBtn);
        saveInfo.setVisibility(View.INVISIBLE);
        saveInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });
        editInfo=view.findViewById(R.id.editInfoBtn);
        editInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                userFullName.setEnabled(true);
                userE.setEnabled(true);
                userName.setEnabled(true);
                phone.setEnabled(true);
                saveInfo.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    //fills the fields in the account fragment with the user's info (based on userId)
    public void loadUserData(String userID)
    {
        FirebaseDbSingleton.getInstance().dbRef.child("User").child(userID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                userFullName.setText(dataSnapshot.child("firstName").getValue().toString()+dataSnapshot.child("lastName").getValue().toString());
                userE.setText(dataSnapshot.child("email").getValue().toString());
                userName.setText(dataSnapshot.child("username").getValue().toString());
                phone.setText(dataSnapshot.child("phoneNumber").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }
}
