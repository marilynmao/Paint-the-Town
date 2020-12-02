package com.example.paintthetown491;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //elements that exist on the layout
    DrawerLayout dLayout;
    ActionBarDrawerToggle actionToggle;
    Toolbar toolBar;
    NavigationView navView;
    FragmentManager fragManager;
    FragmentTransaction fragmentTransaction;
    NotificationCompat.Builder notificationBuilder;
    long prevRequestValue;

    //notification for new request
    public void notificationNewRequest()
    {
        createNotificationChannelNewReq();
        // build the notification to send
        notificationBuilder = new NotificationCompat.Builder(
                MainActivity.this, "New Request Notification"
        )
                .setContentTitle("Request received")    // notification title
                .setSmallIcon(R.drawable.pending_friend)       // icon
                .setContentText("You have received a new request! :)")     // notification body text
                .setAutoCancel(true);                   // enable swiping notification
        NotificationManagerCompat nManager = NotificationManagerCompat.from(MainActivity.this);
        nManager.notify(1, notificationBuilder.build());
    }

    //check if new friend requests are received
    public void checkNewFriendReq()
    {
        FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("pending").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                //check that firebase returned something
                if (snapshot.exists())
                {
                    //initial size of pending list is set
                    if(prevRequestValue==0)
                    {
                        prevRequestValue=snapshot.getChildrenCount();
                    }

                    //if the list contains more than the previous value, output a notification
                    if(prevRequestValue<snapshot.getChildrenCount())
                    {
                        prevRequestValue=snapshot.getChildrenCount();
                        notificationNewRequest();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    //creates a one time listener for populating the user's list of sent friend requests.
    public void getSentPendingFriendRequests(){

        FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().firebaseAuth.getUid()).child("sentFriendRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User.getInstance().deleteSentFriendRequestList();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        addToSentFriendRequests(((String) ds.getValue()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //small helper function for populating the user's list of sent friend requests (might be by-passable)
    public void addToSentFriendRequests(String id){
        User.getInstance().addSentFriendRequest(id);
    }

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

        // create notification channel to send notification
        createNotificationChannel();

        // generate a dummy key to get current time  since push().getkey() is based on a timestamp (won't be saved in db)
        String startKey = FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("friends").push().getKey();
        /*  query compares the get the push().getkey() timestamp with all the keys timestamp from the specific
            user's list of friends to get the most recently added key. This will prevent the onChildAdded from
            triggering on the start of the app.
         */
        Query userFriendListquery = FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("friends").orderByKey().startAt(startKey);
        // attaching child event listener to detect when a new friend is added to user's friend list
        userFriendListquery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // get newly added friendID
                String newFriendID = snapshot.getValue().toString();
                // call get friend to get friend's name from db
                getFriend(newFriendID);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //listener for new requests
        checkNewFriendReq();

        //listener for the sent friend requests list.
        getSentPendingFriendRequests();
    }

    // get friend's name
    private void getFriend(String newFriendID)
    {
        // listener to get friend's full name
        ValueEventListener friendValListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    // get user data
                    User friendData = snapshot.getValue(User.class);
                    // get friend's full name to display
                    String friendFullName = friendData.getFirstName() + " " + friendData.getLastName();
                    // send notification with friend's name
                    sendNotification(friendFullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        // queries user node for friendID
        Query friendQuery = FirebaseDbSingleton.getInstance().dbRef.child("User").child(newFriendID);
        friendQuery.addListenerForSingleValueEvent(friendValListener);
    }

    // send notification for accepted request
    private void sendNotification(String friendFullName)
    {
        // message to be displayed in notification
        String acceptedRequestMsg = "You are now friends with: " + friendFullName;

        // build the notification to send
        notificationBuilder = new NotificationCompat.Builder(
                MainActivity.this, "Accepted Friend Notification"
        )
                .setContentTitle("New Friend!")    // notification title
                .setSmallIcon(R.drawable.friends)       // icon
                .setContentText(acceptedRequestMsg)     // notification body text
                .setAutoCancel(true);                   // enable swiping notification
        NotificationManagerCompat nManager = NotificationManagerCompat.from(MainActivity.this);
        nManager.notify(1, notificationBuilder.build());
    }

    // create notification channel for accepted requests
    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel nChannel = new NotificationChannel("Accepted Friend Notification", "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(nChannel);
        }
    }

    // create notification channel for new requests
    private void createNotificationChannelNewReq()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel nChannel = new NotificationChannel("New Request Notification", "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(nChannel);
        }
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

        if(item.getItemId()==R.id.createEvent)
        {
            fragManager=getSupportFragmentManager();
            fragmentTransaction=fragManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_frag,new CreateEventActivity());
            fragmentTransaction.commit();
        }

        //user selected the events item from the navigation panel
        if(item.getItemId()==R.id.events)
        {
            fragManager=getSupportFragmentManager();
            fragmentTransaction=fragManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_frag,new EventsTabsFragment());
            fragmentTransaction.commit();
        }

        //user selected the friends item from the navigation panel
        if(item.getItemId()==R.id.friends)
        {
            fragManager=getSupportFragmentManager();
            fragmentTransaction=fragManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_frag,new FriendsActivity());
            fragmentTransaction.commit();
        }

        //user selected the past events item from the navigation panel
        if(item.getItemId()==R.id.pastEvents)
        {
            fragManager=getSupportFragmentManager();
            fragmentTransaction=fragManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_frag,new PastEventsActivity());
            fragmentTransaction.commit();
        }

        //user selected the favorite events item from the navigation panel
        if(item.getItemId()==R.id.favoriteEvents)
        {
            fragManager=getSupportFragmentManager();
            fragmentTransaction=fragManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_frag,new favoriteEventsActivity());
            fragmentTransaction.commit();
        }

        //user selected the logout item from the navigation panel
        if(item.getItemId()==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),CreateAccountActivity.class));
            finish();
        }

        //user selected the pending requests item from the navigation panel
        if(item.getItemId()==R.id.pending_requests)
        {
            fragManager=getSupportFragmentManager();
            fragmentTransaction=fragManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_frag, new PendingActivity());
            fragmentTransaction.commit();
        }

        return true;
    }
}