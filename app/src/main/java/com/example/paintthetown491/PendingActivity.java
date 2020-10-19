package com.example.paintthetown491;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;

public class PendingActivity extends Fragment
{
    private RecyclerView pending;
    private TextView noPending;
    private PendingAdapter pendingAdapter;
    private DatabaseReference eventRef;
    private Map<String,Integer>pendingUsers;
    private ArrayList<User>users;

    //deletes the user ID from list
    public void deleteUserID(String userID)
    {
        //if(userID.charAt(0)!='-')
        //{
        //   userID="-"+userID;
        //}
        //logged-in user ID
        String mainID=FirebaseDbSingleton.getInstance().user.getUid();
        //pending user ID to delete (depending on the userID key provided)
        String key=pendingUsers.get(userID).toString();
        //deletes the pending user ID from the DB
        FirebaseDbSingleton.getInstance().dbRef.child("User").child(mainID).child("pending").child(key).removeValue();
        //notifies adapter of changes
        pendingAdapter.notifyDataSetChanged();
    }

    //confirmation to accept request
    public void confirmationDelete(final int position)
    {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm")
                .setMessage("Are you sure?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        deleteUserID(users.get(position).getId());
                        users.remove(position);
                        pendingAdapter.notifyDataSetChanged();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    //confirmation to accept request
    public void confirmationAccept(int position)
    {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm")
                .setMessage("Are you sure?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.frag_pending_requests, container, false);
        //binding xml elements to class members
        pending=view.findViewById(R.id.pending_requests);
        pending.setHasFixedSize(true);
        pending.setLayoutManager(new LinearLayoutManager(getActivity()));
        noPending=view.findViewById(R.id.no_pending_requests);

        //allocating memory for the list items to avoid null references
        pendingUsers=new HashMap<>();
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
                System.out.println("DELETE");
                confirmationDelete(position);
            }

            //handles accepting the request
            @Override
            public void acceptOnClick(int position)
            {
                System.out.println("ACCEPT");
                confirmationAccept(position);
            }
        });

        //listener for the pending requests user IDs in firebase
        ValueEventListener pendingIdValListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                pendingUsers.clear();
                //check to see if firebase returned anything
                if (snapshot.exists())
                {
                    pending.setVisibility(View.VISIBLE);
                    noPending.setVisibility(View.INVISIBLE);

                    //iterate through each child returned
                    for (DataSnapshot e : snapshot.getChildren())
                    {
                        //place key and value in hashmap
                        pendingUsers.put(e.getValue(String.class),Integer.parseInt(e.getKey()));
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

                        //sees if the hashmap contains the ID as a key
                        if (pendingUsers.containsKey(s))
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
