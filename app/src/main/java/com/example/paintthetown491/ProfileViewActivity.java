package com.example.paintthetown491;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

    public class ProfileViewActivity extends Activity {

    public TextView profile_fn;
    public TextView profile_ln;
    public TextView profile_username;
    public ImageView profile_image;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);

        //binds the layout to this activity. You can find the xml in res.layout
        setContentView(R.layout.profile_view);


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

}}
