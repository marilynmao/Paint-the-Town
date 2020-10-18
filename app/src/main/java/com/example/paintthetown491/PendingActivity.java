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

public class PendingActivity extends Fragment
{
    private RecyclerView pending;
    private TextView noPending;
    private PendingAdapter pendingAdapter;
    private DatabaseReference eventRef;
    private ArrayList<String>pendingUserIDs;
    private ArrayList<User>users;

    public void removeItem(int position)
    {
        users.remove(position);
        pendingAdapter.notifyItemMoved(position,position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.frag_pending_requests, container, false);
        //binding xml elements to class members
        pending=view.findViewById(R.id.pending_requests);
        pending.setHasFixedSize(true);
        pending.setLayoutManager(new LinearLayoutManager(getActivity()));
        noPending=view.findViewById(R.id.no_pending_requests);

        //allocating memory for the list items to avoid null references
        pendingUserIDs=new ArrayList<String>();
        users=new ArrayList<User>();

        //using our adapter to display the necessary info
        pendingAdapter= new PendingAdapter(users);
        pending.setAdapter(pendingAdapter);

        //listener for each item in the recycler
        pendingAdapter.setOnItemClickListener(new PendingAdapter.OnItemClickListener()
        {
            //will handle opening user profiles
            @Override
            public void onItemClick(int position)
            {
                System.out.println("CLICKED");
            }

            //handles deleting the request
            @Override
            public void deleteOnClick(int position)
            {
                removeItem(position);
            }

            //handles accepting the request
            @Override
            public void acceptOnClick(int position)
            {

            }
        });

        //listener for the pending requests user IDs in firebase
        ValueEventListener pendingIdValListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                pendingUserIDs.clear();
                //check to see if firebase returned anything
                if (snapshot.exists())
                {
                    pending.setVisibility(View.VISIBLE);
                    noPending.setVisibility(View.INVISIBLE);

                    //iterate through each child returned
                    for (DataSnapshot e : snapshot.getChildren()) {
                        //holds the event ID as a string
                        String event = e.getValue(String.class);
                        //adds it to the list
                        pendingUserIDs.add(event);
                    }
                    //notifies the adapter of any changes
                    pendingAdapter.notifyDataSetChanged();
                }
                //no pending requests, show message and hide recycler
                else
                {
                    pending.setVisibility(View.INVISIBLE);
                    noPending.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        };

        //querying the specific user's pending requests
        Query query = FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("pending");
        //attaching the value listener
        query.addValueEventListener(pendingIdValListener);

        //now that we have the pending user IDs saved, we need to look at the User table for each of them
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
                        if (pendingUserIDs.contains(s))
                        {
                            // get user values
                            User userData = ds.getValue(User.class);
                            //add it to the arraylist that goes into the adapter
                            users.add(userData);
                        }
                    }
                }
                //notifies the adapter of any changes
                pendingAdapter.notifyDataSetChanged();
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
