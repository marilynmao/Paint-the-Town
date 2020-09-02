package com.example.paintthetown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.paintthetown.ui.login.LoginActivity;

/*
    WelcomeActivity is the main screen that handles
    navigating users to Login / Create Account screen
 */

public class WelcomeActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // testing firebase connection
        Toast.makeText(WelcomeActivity.this, "firebase connection successful", Toast.LENGTH_LONG).show();
    }

    // starts LoginActivity when login button is clicked
    public void loginScreen(View view){
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // starts createAccountActivity when sign up button is clicked
    public void signupScreen(View view){
        intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }
}

