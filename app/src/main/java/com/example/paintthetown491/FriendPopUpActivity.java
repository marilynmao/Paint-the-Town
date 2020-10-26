package com.example.paintthetown491;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FriendPopUpActivity extends Activity {
    public TextView profile_fn;
    public TextView profile_ln;
    public TextView profile_username;
    public ImageView profile_image;
    private String profile_ID;
    private Button removeFriend;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);

        //binds the layout to this activity. You can find the xml in res.layout
        setContentView(R.layout.profile_view_friend);

        profile_ID = getIntent().getStringExtra("id");

        profile_fn = findViewById(R.id.profile_view_fn);
        profile_fn.setText(getIntent().getStringExtra("firstname"));

        profile_ln = findViewById(R.id.profile_view_ln);
        profile_ln.setText(getIntent().getStringExtra("lastname"));

        profile_username = findViewById(R.id.profile_view_username);
        profile_username.setText(getIntent().getStringExtra("username"));

        profile_image = findViewById(R.id.profile_view_img);

        String profile_img_string = getIntent().getStringExtra("image");
        StorageReference path = FirebaseStorage.getInstance().getReference("Icons").child(profile_img_string);
        path.getBytes(5 * 1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profile_image.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failed to set profile pic");
            }
        });

        removeFriend = findViewById(R.id.profile_view_remove);
        removeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to remove this user from your friends list?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                removeFriend();//removes the friend from the friends list
                                finish(); //returns to the FriendsActivity screen
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    //method for removing a user from their friends list.
    private void removeFriend()
    {

        //Listener for finding and removing the selected friend from the friends list of the user.
        ValueEventListener userDeleteFriendListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //loop until the child with the correct ID is found then remove it.
                    for (DataSnapshot f : snapshot.getChildren())
                    {
                        if (f.getValue(String.class).equals(profile_ID)){
                            f.getRef().removeValue();
                            break; //id was found and removed so no more looping is required.
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        Query delQuery = FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("friends");
        delQuery.addListenerForSingleValueEvent(userDeleteFriendListener);

        //Listener for finding and removing the selected user from the friends list of the friend.
        ValueEventListener friendDeleteUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //loop until the child with the correct ID is found then remove it.
                for (DataSnapshot f : snapshot.getChildren())
                {
                    if (f.getValue(String.class).equals(FirebaseDbSingleton.getInstance().firebaseAuth.getUid())){
                        f.getRef().removeValue();
                        break; //id was found and removed so no more looping is required.
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        Query friendDelQuery = FirebaseDbSingleton.getInstance().dbRef.child("User").child(profile_ID).child("friends");
        friendDelQuery.addListenerForSingleValueEvent(friendDeleteUserListener);


    }

}
