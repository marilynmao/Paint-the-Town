package com.example.paintthetown491;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private TextView accountExists;
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
        accountExists=findViewById(R.id.AccountExists);

        user = new User();

        //handles create action
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //firebase method to create an account
                mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if(task.isSuccessful())
                                {
                                    //FIRSTNAME
                                    //checks that user filled out box
                                    if (firstName.getText().toString().length() > 0)
                                    {
                                        //sets firstname if requirements are met
                                        user.setFirstName(firstName.getText().toString());
                                    }
                                    else
                                        {
                                        //alerts user the field is required
                                        firstName.setError("first name is required!");
                                        //brings message to focus
                                        firstName.requestFocus();
                                        }

                                    //LASTNAME
                                    //checks that user filled out box
                                    if (lastName.getText().toString().length() > 0)
                                    {
                                        //sets lastname if requirements are met
                                        user.setLastName(lastName.getText().toString());
                                    }
                                    else
                                        {
                                        //alerts user the field is required
                                        lastName.setError("Last name is required!");
                                        //brings error message to focus
                                        lastName.requestFocus();
                                        }

                                    //EMAIL
                                    //checks that email address is valid before adding
                                    if(email.getText().toString().contains("@") & email.getText().toString().contains("."))
                                    {
                                        //sets email if requirements are met
                                        user.setEmail(email.getText().toString());
                                    }
                                    else
                                        {
                                            //sets error dialogue
                                            email.setError("invalid email! Please re-enter with proper address.");
                                            //brings error message to focus
                                            email.requestFocus();
                                        }

                                    //USERNAME
                                    //checks that user filled out box
                                    if (userName.getText().toString().length() == 0)
                                    {
                                        //sets error dialogue
                                        userName.setError("username is required!");
                                        //brings error message to focus
                                        userName.requestFocus();
                                    }
                                    else
                                        {
                                            //sets username if requirements are met
                                            user.setUsername(userName.getText().toString());
                                        }

                                    //PASSWORD
                                    //checks password length
                                    if(password.getText().toString().length() > 8)
                                    {
                                        //sets password if requirements are met
                                        user.setPassword(password.getText().toString());
                                        //brings error message to focus
                                        password.requestFocus();
                                    }
                                    else
                                        {
                                            //sets error dialogue
                                            password.setError("Password must be at least 9 characters");
                                        }

                                    //PHONE NUMBER
                                    //checks phone number length
                                    if (phoneNumber.getText().toString().length() == 10)
                                    {
                                        //sets phone number if valid
                                        user.setPhoneNumber(phoneNumber.getText().toString());
                                    }
                                    else
                                        {
                                        //sets error dialogue
                                        phoneNumber.setError("invalid number! Please re-enter 10 digits.");
                                            //brings error message to focus
                                            userName.requestFocus();
                                        }

                                    dbRef.child(mAuth.getCurrentUser().getUid()).setValue(user);
                                    
                                    Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
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

        //switches to activity for user to login
        accountExists.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}