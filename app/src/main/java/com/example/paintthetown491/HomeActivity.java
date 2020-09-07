package com.example.paintthetown491;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
{
    private Button signoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        signoutButton=findViewById(R.id.logoutButton);
        signoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                logOut(view);
            }
        });
    }

    public void logOut(View view)
    {
        //signs the user out
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),CreateAccountActivity.class));
        finish();
    }
}