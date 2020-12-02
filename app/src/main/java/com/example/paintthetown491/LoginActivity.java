package com.example.paintthetown491;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    private TextView accountExists, recoverAccount;
    private FirebaseAuth fAuth;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //findViewById ties elements of the xml to the variables of this class
        final EditText userEmail = findViewById(R.id.userEmail);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);


        //uses the singleton class to prevent making multiple instances of the same class throughout the project
        fAuth=FirebaseAuth.getInstance();
        accountExists=findViewById(R.id.accountExists);
        accountExists.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),CreateAccountActivity.class));
            }
        });

        recoverAccount=findViewById(R.id.RecoverAccount);
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

        TextWatcher afterTextChangedListener = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        };

        userEmail.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {

                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(TextUtils.isEmpty(passwordEditText.getText()))
                {
                    passwordEditText.setError("Password required");
                    return;
                }

                if(TextUtils.isEmpty(userEmail.getText()))
                {
                    userEmail.setError("Password required");
                    return;
                }

                login(userEmail.getText().toString(),passwordEditText.getText().toString());


            }
        });
    }

    //logs the user in
    private void login(String userEmail, String userPassword)
    {
        final LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
        loadingDialog.startLoading();
        fAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    loadingDialog.stopLoading();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else
                {
                    loadingDialog.stopLoading();
                    Toast.makeText(LoginActivity.this,"Wrong credentials!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateUiWithUser() {

        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), "welcome", Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
