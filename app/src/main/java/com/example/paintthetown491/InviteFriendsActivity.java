package com.example.paintthetown491;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InviteFriendsActivity extends AppCompatActivity {


    private RecyclerView friendsRecycler;
    private InviteFriendsAdapter inviteFriendsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference userRef;
    private ArrayList<String> userIDs;
    private ArrayList<User> users;
    private Button inviteFriendBtn, cancelInviteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        // bind elements
        inviteFriendBtn = findViewById(R.id.invite_friend_btn);
        cancelInviteBtn = findViewById(R.id.cancel_invite);

        //holds the event IDs for each user
        userIDs = new ArrayList<String>();
        //holds the events loaded from firebase
        users = new ArrayList<User>();


        friendsRecycler = findViewById(R.id.friends_rv);
        friendsRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        inviteFriendsAdapter = new InviteFriendsAdapter(users);
        friendsRecycler.setLayoutManager(layoutManager);
        friendsRecycler.setAdapter(inviteFriendsAdapter);

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
        inviteFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: invite friend

            }
        });

        // cancel invite
        cancelInviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}