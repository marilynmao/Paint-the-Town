package com.example.paintthetown491;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateAccountActivity extends AppCompatActivity
{
    private EditText password, email, firstName, lastName, userName, phoneNumber,userPasswordVerify;
    private TextView accountExists, recoverAccount;
    private Button loginButton, creationNext;
    private ImageButton creationBack;
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

        // findViewById ties elements of the xml to the variables of this class
        password=findViewById(R.id.UserPassword);
        userPasswordVerify=findViewById(R.id.userPasswordVerify);
        email=findViewById(R.id.UserEmail);
        loginButton=findViewById(R.id.LoginButton);
        userName=findViewById(R.id.userName);
        firstName=findViewById(R.id.userFirstName);
        lastName=findViewById(R.id.userLastName);
        phoneNumber=findViewById(R.id.phoneNumber);
        accountExists=findViewById(R.id.AccountExists);
        creationNext=findViewById(R.id.creationNext);
        recoverAccount=findViewById(R.id.RecoverAccount);
        creationBack=findViewById(R.id.creationBack);

        userName.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        userPasswordVerify.setVisibility(View.GONE);
        loginButton.setVisibility(View.GONE);
        creationBack.setVisibility(View.GONE);

        //creates a user object
        user = User.getInstance();

        //handles create action
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //USERNAME
                //checks that user filled out box
                if (userName.getText().toString().length() == 0)
                {
                    //sets error dialogue
                    userName.setError("username is required!");
                    //brings error message to focus
                    userName.requestFocus();
                }

                //PASSWORD
                //checks password length
                else if(password.getText().toString().length() < 9)
                {
                    //sets error dialogue
                    password.setError("Password must be at least 9 characters");
                }

                //verifies the password they wanted is correctly entered by the user
                else if(!(userPasswordVerify.getText().toString().equals(password.getText().toString()))){
                    userPasswordVerify.setError("Re-entered password does not match!");
                }

                else {
                    //firebase method to create an account
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        //sets the members of the user class
                                        user.setFirstName(firstName.getText().toString());
                                        user.setLastName(lastName.getText().toString());
                                        user.setEmail(email.getText().toString());
                                        user.setUsername(userName.getText().toString());
                                        user.setPhoneNumber(phoneNumber.getText().toString());
                                        user.setEventList(new ArrayList<String>(Arrays.asList("0")));
                                        //inserts the user into the DB
                                        dbRef.child(mAuth.getCurrentUser().getUid()).setValue(user);

                                        //takes the user to the home page
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);

                                        //notifies that the account was created successfully
                                        Toast.makeText(CreateAccountActivity.this, "SUCCESS!", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        //System.out.print("FAILED!");
                                        //Toast.makeText(getApplicationContext(),"SOMETHING WENT WRONG!",Toast.LENGTH_LONG);
                                    }
                                }
                            });
                }
            }
        });

        //Checks the first four inputs of the user than takes them to the next "page" by showing the
        //remaining input fields and the register button.
        creationNext.setOnClickListener(new View.OnClickListener()
        {
            @Override

            //public void onClick(View v)
            //{
             //   //removes any char other than numerical digits from the phone number.
             //   phoneNumber.setText(phoneNumber.getText().toString().replaceAll("[^0-9]", ""), TextView.BufferType.EDITABLE);

            public void onClick(View v) {

                if (phoneNumber.getText().toString() != "") {
                    //removes any char other than numerical digits from the phone number.
                    phoneNumber.setText(phoneNumber.getText().toString().replaceAll("[^0-9]", ""), TextView.BufferType.EDITABLE);
                }

                //FIRSTNAME
                //checks that user filled out box
                if (firstName.getText().toString().length() == 0)
                {
                    //alerts user the field is required
                    firstName.setError("first name is required!");
                    //brings message to focus
                    firstName.requestFocus();
                }

                //LASTNAME
                //checks that user filled out box
                else if (lastName.getText().toString().length() == 0)
                {
                    //alerts user the field is required
                    lastName.setError("Last name is required!");
                    //brings error message to focus
                    lastName.requestFocus();
                }

                //EMAIL
                //checks that email address is valid
                else if(!email.getText().toString().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"))
                {
                    //sets error dialogue
                    email.setError("invalid email! Please re-enter with proper address.");
                    //brings error message to focus
                    email.requestFocus();
                }

                //PHONE NUMBER
                //checks phone number length
                else if (phoneNumber.getText().toString().length() != 10)
                {
                    //sets error dialogue
                    phoneNumber.setError("invalid number! Please re-enter 10 digits.");
                    //brings error message to focus
                    userName.requestFocus();
                }

                else
                    {
                    firstName.setVisibility(View.GONE);
                    lastName.setVisibility(View.GONE);
                    email.setVisibility(View.GONE);
                    phoneNumber.setVisibility(View.GONE);
                    creationNext.setVisibility(View.GONE);

                    userName.setVisibility(View.VISIBLE);
                    password.setVisibility(View.VISIBLE);
                    userPasswordVerify.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.VISIBLE);
                    creationBack.setVisibility(View.VISIBLE);
                }
            }
        });

        //Button listener for when the user wants to go back to the "page" with the first four inputs
        creationBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                userName.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                userPasswordVerify.setVisibility(View.GONE);
                loginButton.setVisibility(View.GONE);
                creationBack.setVisibility(View.GONE);

                firstName.setVisibility(View.VISIBLE);
                lastName.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                phoneNumber.setVisibility(View.VISIBLE);
                creationNext.setVisibility(View.VISIBLE);
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

        recoverAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //the field where user will enter the email
                final EditText emailRecovery=new EditText(view.getContext());

                //dialog box that will be shown to the user
                AlertDialog.Builder resetPassDialog=new AlertDialog.Builder(view.getContext());
                resetPassDialog.setMessage("Enter your email to receive a reset link");
                resetPassDialog.setView(emailRecovery);
                resetPassDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        //attempts to send a recovery email to the specified email, attaches onSuccess and OnFailure listeners to handle both scenarios
                        FirebaseDbSingleton.getInstance().firebaseAuth.sendPasswordResetEmail(emailRecovery.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(getApplicationContext(),"Check your email for a recovery link :)", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(getApplicationContext(),"Email not found :(", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                //closes the dialog box
                resetPassDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                //shows the dialog box
                resetPassDialog.create().show();

                //solution using activities
                //Intent intent=new Intent(getApplicationContext(),RecoverAccountActivity.class);
                //startActivity(intent);
            }
        });

        //notifies the user if they enter an invalid char while typing.
        firstName.addTextChangedListener(new TextWatcher()
        {
            @Override//unused
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (!s.toString().matches("[a-zA-Z]+") && s.toString() != ""){
                    firstName.setError("First name can only contain letters A-Z");
                }
            }

            @Override//unused
            public void afterTextChanged(Editable s) {}
        });

        //notifies the user if they enter an invalid char while typing.
        lastName.addTextChangedListener(new TextWatcher() {

            @Override//unused
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (!s.toString().matches("[a-zA-Z]+") && s.toString() != "")
                {
                    lastName.setError("Last name can only contain letters A-Z");
                }
            }

            @Override//unused
            public void afterTextChanged(Editable s) {}
        });

        //notifies the user while there password is less than 9 chars while typing.
        password.addTextChangedListener(new TextWatcher() {
            @Override//unused
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() < 9 && s.length() > 0)
                {
                    password.setError("Password must be at least 9 characters");
                }
            }

            @Override//unused
            public void afterTextChanged(Editable s) {}
        });

        //notifies the user if they enter anything other than a number while typing.
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override//unused
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("[0-9]+")){
                    phoneNumber.setError("Please only enter numerical digits (0-9)");
                }
                if (s.length() > 10){
                    phoneNumber.setError("Phone number must be a maximum of 10 digits long");
                }
            }

            @Override//unused
            public void afterTextChanged(Editable s) {}
        });
    }
}