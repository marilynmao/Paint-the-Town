package com.example.paintthetown491;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
{
    DrawerLayout dLayout;
    ActionBarDrawerToggle actionToggle;
    Toolbar toolBar;
    NavigationView navView;
    private Button signoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dLayout=findViewById(R.id.drawer);
        toolBar=findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        navView=findViewById(R.id.navView);
        actionToggle=new ActionBarDrawerToggle(this,dLayout,toolBar,R.string.open,R.string.close);
        dLayout.addDrawerListener(actionToggle);
        actionToggle.setDrawerIndicatorEnabled(true);
        actionToggle.syncState();

        //signoutButton=findViewById(R.id.logoutButton);
        //signoutButton.setOnClickListener(new View.OnClickListener()
        //{
        //    @Override
        //    public void onClick(View view)
        //    {
        //        logOut(view);
        //    }
        //});
    }

    public void logOut(View view)
    {
        //signs the user out
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),CreateAccountActivity.class));
        finish();
    }
}