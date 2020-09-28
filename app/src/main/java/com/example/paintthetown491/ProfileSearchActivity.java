package com.example.paintthetown491;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileSearchActivity extends AppCompatActivity {
    private static final String TAG = "PROFILE SEARCH ACTIVITY" ;
    private RecyclerView profRecyclerView;
    private ProfilesAdapter psAdapter;
    ArrayList<User> profList;

    String srchInput;
    TextView noResultsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_search);
        // get search input from search people field in HomeActivity class
        Intent intent = getIntent();
        srchInput = intent.getStringExtra("Search Input");

        // hide "no results found" text
        noResultsTextView = findViewById(R.id.noResults);
        noResultsTextView.setVisibility(View.GONE);

        profRecyclerView = findViewById(R.id.profile_search_rv);
        profRecyclerView.setHasFixedSize(true);
        profRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        profList = new ArrayList<>();
        psAdapter = new ProfilesAdapter(profList);
        profRecyclerView.setAdapter(psAdapter);
        psAdapter.setOnItemClickListener(new ProfilesAdapter.OnItemClickListener()
        {
            //handles what happens when an item from the recyclerview is clicked
            @Override
            public void onItemClick(int position)
            {
                //TODO:open up the event page
                System.out.println("CLICKED!");
            }
        });

        // find users in db whose first name is equal to search input (search by first name for now)
        Query query = FirebaseDbSingleton.getInstance().dbRef.child("User")
                .orderByChild("firstName")
                .equalTo(srchInput);

        // attach listener
        query.addValueEventListener(valueEventListener);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            // for testing purposes
            Log.d(TAG,"ON DATA CHANGE SUCCESS");
            profList.clear();
            if(dataSnapshot.exists()) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    // get user values
                    User userData = ds.getValue(User.class);
                    profList.add(userData);
                }
            psAdapter.notifyDataSetChanged();
            }

            // name doesn't exist in DB, update UI
            else
            {
                // hide recycler view
                profRecyclerView.setVisibility(View.GONE);
                // show results error
                noResultsTextView.setVisibility(View.VISIBLE);
                psAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d(TAG,"ERROR: " + databaseError.getMessage());

        }
    };
}