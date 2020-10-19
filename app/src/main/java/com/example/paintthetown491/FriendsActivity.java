package com.example.paintthetown491;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//class that holds elements needed for our events fragment
public class FriendsActivity extends Fragment
{
    private RecyclerView friendsRecycler;
    private ProfilesAdapter profAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference eventRef;
    private ArrayList<String> userIDs;
    private ArrayList<User> users;
    TextView noResultsTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //the inflate() method takes the layout you wanna show as the first parameter
        final View view = inflater.inflate(R.layout.activity_profile_search, container, false);

        // hide "no results found" text
        noResultsTextView = view.findViewById(R.id.noResults);
        noResultsTextView.setVisibility(View.GONE);

        //holds the event IDs for each user
        userIDs = new ArrayList<String>();
        //holds the events loaded from firebase
        users = new ArrayList<User>();


        //events needs to be replaced by Users or userID
        friendsRecycler = view.findViewById(R.id.profile_search_rv);
        friendsRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        profAdapter = new ProfilesAdapter(users);
        friendsRecycler.setLayoutManager(layoutManager);
        friendsRecycler.setAdapter(profAdapter);

        // set on click listener for create event button to up create event page



        //listener for the Friends IDs in firebase
        ValueEventListener friendIdValListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                userIDs.clear();
                //check to see if firebase returned anything
                if (snapshot.exists())
                {
                    //iterate through each child returned
                    for (DataSnapshot f : snapshot.getChildren())
                    {
                        //holds the event ID as a string
                        String friendID = f.getValue(String.class);
                        //adds it to the list
                        userIDs.add(friendID);
                    }
                    //notifies the adapter of any changes
                    profAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        };

        //querying the specific user's list of friends
        Query query = FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("friends");
        //attaching the value listener
        query.addValueEventListener(friendIdValListener);

        //now that we have the event IDs saved, we need to look at the Event table for each of them
        eventRef = FirebaseDbSingleton.getInstance().dbRef.child("User");
        //listener for the values we want
        eventRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                //check that firebase returned something
                if (snapshot.exists())
                {
                    //iterate through each child returned
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        String s=ds.getKey();

                        if(ds.getKey().charAt(0)=='-')
                        {
                            //removes a "-" character appended to the beginning of each key
                            s = ds.getKey().substring(1);
                        }

                        //sees if the arraylist contains this child
                        if (userIDs.contains(s))
                        {
                            //create the friends object with the properties returned from firebase
                            User f = ds.getValue(User.class);
                            //add it to the arraylist that goes into the adapter
                            users.add(f);
                        }
                    }
                }
                //notifies the adapter of any changes
                profAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }

            //used to convert a Iterable (type returned from firebase) to an arraylist
            public ArrayList<String> getCollectionFromIterable(Iterable<DataSnapshot> itr)
            {
                ArrayList<String> participants = new ArrayList<String>();
                for (DataSnapshot id : itr)
                {
                    participants.add(id.toString());
                }
                return participants;
            }
        });




        return view;
    }
}
