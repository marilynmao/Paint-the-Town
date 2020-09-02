package com.example.paintthetown;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {
    EditText firstNameText, lastNameText, emailText, usernameText, passwordText, phoneNumberText;
    Button rgstrBtn;
    DatabaseReference dbRef;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // reference text fields
        firstNameText = (EditText) findViewById(R.id.userFirstName);
        lastNameText = (EditText) findViewById(R.id.userLastName);
        emailText = (EditText) findViewById(R.id.emailAddress);
        usernameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);
        phoneNumberText = (EditText) findViewById(R.id.phoneNumber);
        rgstrBtn = (Button) findViewById(R.id.registerButton);

        user = new User();

        // get instance of database reference to insert data
        dbRef = FirebaseDatabase.getInstance().getReference().child("User");

       // inserts user data into database when user clicks register
        rgstrBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // set user data
                user.setFirstName(firstNameText.getText().toString());
                user.setLastName(lastNameText.getText().toString());
                user.setEmail(emailText.getText().toString());
                user.setUsername(usernameText.getText().toString());
                user.setPassword(passwordText.getText().toString());
                user.setPhoneNumber(phoneNumberText.getText().toString());
                // insert data
                dbRef.push().setValue(user);
                // testing
                Toast.makeText(CreateAccountActivity.this,"Data successfully inserted", Toast.LENGTH_LONG).show();
            }
        });

    }
}