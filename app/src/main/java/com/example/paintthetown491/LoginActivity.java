package com.example.paintthetown491;

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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    private TextView accountExists;
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
