package com.example.paintthetown491;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
                //removes any char other than numerical digits from the phone number.
                phoneNumber.setText(phoneNumber.getText().toString().replaceAll("[^0-9]", ""), TextView.BufferType.EDITABLE);

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

                //USERNAME
                //checks that user filled out box
                else if (userName.getText().toString().length() == 0)
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

                //PHONE NUMBER
                //checks phone number length
                else if (phoneNumber.getText().toString().length() != 10)
                {
                    //sets error dialogue
                    phoneNumber.setError("invalid number! Please re-enter 10 digits.");
                    //brings error message to focus
                    userName.requestFocus();
                }

                else {
                    //firebase method to create an account
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user.setFirstName(firstName.getText().toString());
                                        user.setLastName(lastName.getText().toString());
                                        user.setEmail(email.getText().toString());
                                        user.setUsername(userName.getText().toString());
                                        user.setPassword(password.getText().toString());
                                        user.setPhoneNumber(phoneNumber.getText().toString());

                                        dbRef.child(mAuth.getCurrentUser().getUid()).setValue(user);

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
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

        //notifies the user if they enter an invalid char while typing.
        firstName.addTextChangedListener(new TextWatcher() {

            @Override//unused
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("[a-zA-Z]+") && s.toString() != ""){
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
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 9 && s.length() > 0){
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