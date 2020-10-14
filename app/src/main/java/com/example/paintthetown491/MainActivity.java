package com.example.paintthetown491;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //elements that exist on the layout
    DrawerLayout dLayout;
    ActionBarDrawerToggle actionToggle;
    Toolbar toolBar;
    NavigationView navView;
    FragmentManager fragManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);
        toolBar=findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        dLayout=findViewById(R.id.drawer);
        navView=findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(this);

        actionToggle=new ActionBarDrawerToggle(this,dLayout,toolBar,R.string.open,R.string.close);
        dLayout.addDrawerListener(actionToggle);
        actionToggle.setDrawerIndicatorEnabled(true);
        actionToggle.syncState();

        //loads fragment
        fragManager=getSupportFragmentManager();
        fragmentTransaction=fragManager.beginTransaction();
        fragmentTransaction.add(R.id.container_frag, new HomeActivity());
        fragmentTransaction.commit();
    }

    //selects an item from the navigation panel
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        //user selected the home item from the navigation panel
        if(item.getItemId()==R.id.home)
        {
            fragManager=getSupportFragmentManager();
            fragmentTransaction=fragManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_frag,new HomeActivity());
            fragmentTransaction.commit();
        }

        //user selected the account item from the navigation panel
        if(item.getItemId()==R.id.account)
        {
            fragManager=getSupportFragmentManager();
            fragmentTransaction=fragManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_frag,new AccountActivity());
            fragmentTransaction.commit();
        }

        //user selected the events item from the navigation panel
        if(item.getItemId()==R.id.events)
        {
            fragManager=getSupportFragmentManager();
            fragmentTransaction=fragManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_frag,new EventsActivity());
            fragmentTransaction.commit();
        }

        //user selected the logout item from the navigation panel
        if(item.getItemId()==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),CreateAccountActivity.class));
            finish();
        }
        return true;
    }
}