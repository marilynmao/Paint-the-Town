package com.example.paintthetown491;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InviteFriendsActivity extends AppCompatActivity
{
    private ConstraintLayout confirmation_pop_up;
    private RecyclerView friendsRecycler;
    private InviteFriendsAdapter inviteFriendsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference userRef;
    private ArrayList<String> userIDs;
    private ArrayList<User> users;
    private ArrayList<String> checkedUsers;
    private Button inviteFriendBtn, cancelInviteBtn;
    String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        //event ID provided by popup
        eventID=getIntent().getExtras().getString("eventID");

        // bind elements
        inviteFriendBtn = findViewById(R.id.invite_friend_btn);
        cancelInviteBtn = findViewById(R.id.cancel_invite);
        confirmation_pop_up=findViewById(R.id.confirmation);

        //make popup invisible initially
        confirmation_pop_up.setVisibility(View.INVISIBLE);

        //holds the event IDs for each user
        userIDs = new ArrayList<String>();
        //holds the events loaded from firebase
        users = new ArrayList<User>();

        //holds the list of user IDs that the user checked
        checkedUsers=new ArrayList<>();

        friendsRecycler = findViewById(R.id.friends_rv);
        friendsRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        inviteFriendsAdapter = new InviteFriendsAdapter(users);
        friendsRecycler.setLayoutManager(layoutManager);
        friendsRecycler.setAdapter(inviteFriendsAdapter);

        inviteFriendsAdapter.setOnItemClickListener(new InviteFriendsAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                //System.out.println("ONCLICK");
            }

            @Override
            public void onCheck(int position)
            {
                //add user ID to list of checked friends
                checkedUsers.add(users.get(position).getId());
                //System.out.println("CHECKED!");
            }

            @Override
            public void onUnCheck(int position)
            {
                //removes user ID from list of checked friends (so that it doesn't get sent an invite)
                checkedUsers.remove(users.get(position).getId());
                //System.out.println("UNCHECKED!");
            }
        });

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
                    inviteFriendsAdapter.notifyDataSetChanged();
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

        userRef = FirebaseDbSingleton.getInstance().dbRef.child("User");
        //listener for the values we want
        userRef.addValueEventListener(new ValueEventListener()
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
                inviteFriendsAdapter.notifyDataSetChanged();
           }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        });

        // send invite
        inviteFriendBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //iterate through list of checked users and send each one an invite
                for(int i=0;i<checkedUsers.size();i++)
                {
                    sendInvite(checkedUsers.get(i));
                    addToEventPendingList(checkedUsers.get(i));
                }
                //make popup visible
                confirmation_pop_up.setVisibility(View.VISIBLE);

                //hide it again after some time
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        confirmation_pop_up.setVisibility(View.INVISIBLE);
                        finish();
                    }
                }, 4000);
            }
        });

        // cancel invite
        cancelInviteBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    //saves eventID to specified user's list of pending event invites
    private void sendInvite(String userId)
    {
        FirebaseDbSingleton.getInstance().dbRef.child("User").child(userId).child("pendingEInvites").child(eventID.substring(1)).setValue(eventID.substring(1));
    }

    //add a user's ID to the list of pending attendees for an event
    private void addToEventPendingList(String userId)
    {
        FirebaseDbSingleton.getInstance().dbRef.child("Event").child(eventID).child("pendingInvites").child(userId).setValue(userId);
    }
}