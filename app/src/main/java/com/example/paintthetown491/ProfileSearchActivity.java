package com.example.paintthetown491;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileSearchActivity extends AppCompatActivity {
    private RecyclerView profRecyclerView;
    private ProfileSearchAdapter psAdapter;
    ArrayList<ProfileSearchItem> profList;

    String srchString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_search);
        // get search input from search people field in HomeActivity class
        Intent intent = getIntent();
        srchString = intent.getStringExtra("Search Input");

        // testing
        Toast.makeText(this, srchString, Toast.LENGTH_SHORT).show();

        profRecyclerView = findViewById(R.id.profile_search_rv);
        profRecyclerView.setHasFixedSize(true);
        profRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        profList = new ArrayList<>();
        psAdapter = new ProfileSearchAdapter(profList);
        profRecyclerView.setAdapter(psAdapter);
        psAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener()
        {
            //handles what happens when an item from the recyclerview is clicked
            @Override
            public void onItemClick(int position)
            {
                //TODO:open up the event page
                System.out.println("CLICKED!");
            }
        });

        // testing recycler view with dummy data (WORKS)
//        profList.add(new ProfileSearchItem("bob", "b", "000000000"));
//        profList.add(new ProfileSearchItem("yuh", "yuh", "1111111"));


        // find users in db whose first name is equal to search input
        Query query = FirebaseDbSingleton.getInstance().dbRef.child("User")
                .orderByChild("firstName")
                .equalTo(srchString);


        query.addValueEventListener(valueEventListener);

    }
    // WORK IN PROGRESS - currently, data is not being retrieved and displayed in search results
    // listener to read values from db
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            profList.clear();
            if(dataSnapshot.exists()) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    ProfileSearchItem userData = ds.getValue(ProfileSearchItem.class);
                    profList.add(userData);
                }
            psAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}