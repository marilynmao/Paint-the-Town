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

import java.util.HashMap;

public class ProfileViewActivity extends Activity {

    public TextView profile_fn;
    public TextView profile_ln;
    public TextView profile_username;
    public ImageView profile_image;
    private Button addFriend;
    private String profile_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);

        //binds the layout to this activity. You can find the xml in res.layout
        setContentView(R.layout.profile_view);

        profile_ID = getIntent().getStringExtra("id");

        profile_fn = findViewById(R.id.profile_view_fn);
        profile_fn.setText(getIntent().getStringExtra("firstname"));

        profile_ln = findViewById(R.id.profile_view_ln);
        profile_ln.setText(getIntent().getStringExtra("lastname"));

        profile_username = findViewById(R.id.profile_view_username);
        profile_username.setText(getIntent().getStringExtra("username"));

        profile_image = findViewById(R.id.profile_view_img);

        addFriend = findViewById(R.id.profile_view_add);
        addFriend.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                new AlertDialog.Builder(v.getContext())
                                                        .setTitle("Confirm")
                                                        .setMessage("Would you like to send this user a friend request?")
                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                                                        {
                                                            public void onClick(DialogInterface dialog, int whichButton)
                                                            {
                                                                addFriend();//removes the friend from the friends list
                                                                finish(); //returns to the FriendsActivity screen
                                                            }})
                                                        .setNegativeButton(android.R.string.no, null).show();
                                            }
                                        });


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

}
        private void addFriend()
        {
            //logged-in user ID
            String mainID=FirebaseDbSingleton.getInstance().user.getUid();
            /*  generate pushkey for main user so that the id is ordered chronologically
            when added to list. (makes it easier to identify which was added most recently) */
            String pushKey1 = FirebaseDbSingleton.getInstance().dbRef.child("User").child(mainID).child("pending").push().getKey();
            // hash map holds the requestor's user ID to be added to the main user's friend list
            HashMap profileMap = new HashMap();

            profileMap.put(pushKey1, mainID);

            FirebaseDbSingleton.getInstance().dbRef.child("User").child(profile_ID).child("pending").updateChildren(profileMap);


        }}
