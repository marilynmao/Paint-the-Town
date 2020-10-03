package com.example.paintthetown491;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AccountActivity extends Fragment
{
    private ImageView profilePic;
    private Button uploadPic, editInfo, saveInfo;
    private EditText userFullName, userE, userName, phone;
    public static final int PICK_IMAGE = 1;
    private Uri imageURI;
    StorageReference mStorageRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.frag_account, container, false);

        //findViewById ties elements of the xml to the variables of this class
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

        System.out.println("check1");

        //gets a reference to the firebase storage that holds user icons.
        mStorageRef= FirebaseStorage.getInstance().getReference("Icons");
        System.out.println("check2");
        //loads user information based on firebase ID
        loadUserData(FirebaseDbSingleton.getInstance().user.getUid(), mStorageRef);

        //listener for button clicks attached
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
            //saves edits to the account info
            @Override
            public void onClick(View view)
            {
                //gets firebase instance, moves into the "user" table, into the specified user ID entry, and changes the respective values (email, username, phoneNumber)
                FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("email").setValue(userE.getText().toString());
                FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("username").setValue(userName.getText().toString());
                FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("phoneNumber").setValue(phone.getText().toString());
                saveInfo.setVisibility(View.INVISIBLE);
            }
        });
        editInfo=view.findViewById(R.id.editInfoBtn);
        editInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                userE.setEnabled(true);
                phone.setEnabled(true);
                editInfo.setVisibility(View.INVISIBLE);
                saveInfo.setVisibility(View.VISIBLE);
            }
        });
        System.out.println("check3");
        return view;

    }

    //called whenever the user has clicked on an image to upload from the gallery. Sets the imageView image using the image URI
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode == Activity.RESULT_OK)
        {
            imageURI=data.getData();
            try {
                //used to find the size of the image the user selects.
                AssetFileDescriptor afd = getActivity().getContentResolver().openAssetFileDescriptor(imageURI,"r");
                
                if(afd.getLength() > (5 * 1024 * 1024)) //5MB file size limit for a user icon
                {
                    Toast.makeText(getActivity(), "Image size too large (5MB maximum)", Toast.LENGTH_LONG).show();
                }
                else
                    {
                    profilePic.setImageURI(imageURI);
                    Fileuploader();
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //fills the fields in the account fragment with the user's info (based on userId)
    public void loadUserData(String userID, StorageReference fbsRef)
    {
        //looks in the "User" table for a user ID match
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

        //TODO check the firebase storage for the users icon if they have one
    }

    //function that sends the image the user selected to the firebase storage bucket.
    private void Fileuploader(){
        final StorageReference Ref=mStorageRef.child(FirebaseDbSingleton.getInstance().user.getUid()+"."+getExtension(imageURI));

        Ref.putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Task<Uri> downloadUrl = Ref.getDownloadUrl();
                        Toast.makeText(getActivity(), "Image uploaded Successfully",Toast.LENGTH_LONG ).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    //simple helper function that finds the file extension of a given uri.
    private String getExtension(Uri uri) {
        ContentResolver cr= getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

}
