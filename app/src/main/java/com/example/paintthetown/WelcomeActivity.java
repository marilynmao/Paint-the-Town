package com.example.paintthetown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.paintthetown.ui.login.LoginActivity;
//import com.example.paintthetown.ui.signup.CreateAccountActivity;


public class WelcomeActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
    public void loginScreen(View view){
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void signupScreen(View view){
        intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

}

