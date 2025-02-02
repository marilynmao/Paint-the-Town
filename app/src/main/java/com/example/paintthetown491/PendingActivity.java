package com.example.paintthetown491;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.HashMap;
import java.util.Map;

public class PendingActivity extends Fragment
{
    private RecyclerView pending;
    private TextView noPending;
    private PendingAdapter pendingAdapter;
    private DatabaseReference eventRef;
    private Map<String,String>pendingUsers;
    private ArrayList<User>users;

    //deletes the user ID from the pending list
    public void deleteUserID(String userID, Integer position)
    {

        //logged-in user ID
        String mainID=FirebaseDbSingleton.getInstance().user.getUid();
        //pending user ID to delete (depending on the userID key provided)
        String key=pendingUsers.get(userID);
        //deletes the pending user ID from the DB
        FirebaseDbSingleton.getInstance().dbRef.child("User").child(mainID).child("pending").child(key).removeValue();
        users.remove(position);
        pendingUsers.remove(userID);
        pendingAdapter.notifyItemRemoved(position);
        pendingAdapter.notifyItemRangeChanged(position,pendingUsers.size());

        //updates the requestors sentFriendRequests list.
        FirebaseDbSingleton.getInstance().dbRef.child("User").child(userID).child("sentFriendRequests").child(mainID).removeValue();
    }

    //moves the pending user ID to the friends list, removes it from pending
    public void addUserID(String userID,int position)
    {
        //logged-in user ID
        String mainID=FirebaseDbSingleton.getInstance().user.getUid();
        /*  generate pushkey for main user so that the id is ordered chronologically
            when added to list. (makes it easier to identify which was added most recently) */
        String pushKey1 = FirebaseDbSingleton.getInstance().dbRef.child("User").child(mainID).child("friends").push().getKey();
        // hash map holds the requestor's user ID to be added to the main user's friend list
        HashMap mainUserFriends = new HashMap();
        // put the push key and requestor's userID into hashmap
        mainUserFriends.put(pushKey1, userID);
        // update the the main user's friend list
        FirebaseDbSingleton.getInstance().dbRef.child("User").child(mainID).child("friends").updateChildren(mainUserFriends);

        /*  generate pushkey for requestor so that the id is ordered chronologically
            when added to list. (makes it easier to identify which was added most recently) */
        String pushKey2 = FirebaseDbSingleton.getInstance().dbRef.child("User").child(userID).child("friends").push().getKey();
        // hash map holds the main user ID to be added to the requestor's friend list
        HashMap requestorsFriends = new HashMap();
        // put the push key and main user ID into hashmap
        requestorsFriends.put(pushKey2, mainID);
        // update the the requestor's friend list
        FirebaseDbSingleton.getInstance().dbRef.child("User").child(userID).child("friends").updateChildren(requestorsFriends);

        //removes it from the recyclerview
        deleteUserID(userID,position);

        //updates the requestors sentFriendRequests list.
        FirebaseDbSingleton.getInstance().dbRef.child("User").child(userID).child("sentFriendRequests").child(mainID).removeValue();
    }

    //confirmation to delete request
    public void confirmationDelete(final int position)
    {
        //confirmation dialog before deleting a request
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm")
                .setMessage("Are you sure?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //remove it from the pending list in firebase
                        deleteUserID(users.get(position).getId(),position);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    //confirmation to accept request
    public void confirmationAccept(final int position)
    {
        //confirmation dialog before accepting a request
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm")
                .setMessage("Are you sure?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //first add it to friends in list in firebase
                        addUserID(users.get(position).getId(),position);
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
        users= new ArrayList<>();

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
            public void deleteOnClick(int position) { confirmationDelete(position); }

            //handles accepting the request
            @Override
            public void acceptOnClick(int position) { confirmationAccept(position); }
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
                    //hides the "no pending requests" message and shows the recycler
                    pending.setVisibility(View.VISIBLE);
                    noPending.setVisibility(View.INVISIBLE);

                    //iterate through each child returned
                    for (DataSnapshot e : snapshot.getChildren())
                    {
                        //place key and value in hashmap
                        pendingUsers.put(e.getValue(String.class),e.getKey());
                    }
                }
                //no pending requests, show message and hide recycler
                else
                {
                    pending.setVisibility(View.INVISIBLE);
                    noPending.setVisibility(View.VISIBLE);
                }
                //notifies the adapter of any changes
                pendingAdapter.notifyDataSetChanged();
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
                users.clear();
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
                ArrayList<String> participants = new ArrayList<>();
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
