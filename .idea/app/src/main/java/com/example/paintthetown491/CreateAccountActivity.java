package com.example.paintthetown491;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity
{
    private EditText password, email, firstName, lastName, userName, phoneNumber;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mAuth=FirebaseAuth.getInstance();
        // get instance of database reference to insert data
        dbRef = FirebaseDatabase.getInstance().getReference().child("User");

        password=findViewById(R.id.UserPassword);
        email=findViewById(R.id.UserEmail);
        loginButton=findViewById(R.id.LoginButton);
        userName=findViewById(R.id.userName);
        firstName=findViewById(R.id.userFirstName);
        lastName=findViewById(R.id.userLastName);
        phoneNumber=findViewById(R.id.phoneNumber);

        user = new User();
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if(task.isSuccessful())
                                {
                                    user.setFirstName(firstName.getText().toString());
                                    user.setLastName(lastName.getText().toString());
                                    user.setEmail(email.getText().toString());
                                    user.setUsername(userName.getText().toString());
                                    user.setPassword(password.getText().toString());
                                    user.setPhoneNumber(phoneNumber.getText().toString());
                                    dbRef.push().setValue(user);
                                    Toast.makeText(CreateAccountActivity.this,"SUCCESS!", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    System.out.print("FAILED!");
                                    //Toast.makeText(getApplicationContext(),"SOMETHING WENT WRONG!",Toast.LENGTH_LONG);
                                }
                            }
                        });
            }
        });

    }
}