package com.example.paintthetown491;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class User
{
    private static User instance=null;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String phoneNumber;
    private String icon;
    private String id;
    private ArrayList<String> friends = new ArrayList<>();
    private ArrayList<String> events = new ArrayList<>();
    private ArrayList<String> savedEvents = new ArrayList<>();
    private ArrayList<String> pastEvents = new ArrayList<>();
    private ArrayList<String> sentFriendRequests = new ArrayList<>();
    private ArrayList<String> pending=new ArrayList<>();
    public User() { }

    //creates a new instance of the user class if it doesn't already exist. Returns the same instance if it does exist.
    public static User getInstance()
    {
        if(instance==null)
            instance= new User();
        return instance;
    }

    public void userPopulate() {
        id = FirebaseDbSingleton.getInstance().firebaseAuth.getUid();
        DatabaseReference dbRef = FirebaseDbSingleton.getInstance().dbRef.child("User").child(id);
        ValueEventListener UserLis =  new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    email = (String) snapshot.child("email").getValue();
                    firstName = (String) snapshot.child("firstName").getValue();
                    icon = (String) snapshot.child("icon").getValue();
                    lastName = (String) snapshot.child("lastName").getValue();
                    firstName = (String) snapshot.child("firstName").getValue();
                    phoneNumber = (String) snapshot.child("phoneNumber").getValue();
                    username = (String) snapshot.child("username").getValue();
                    id = (String) snapshot.child("id").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dbRef.addListenerForSingleValueEvent(UserLis);
    }


    public void setEventList(ArrayList<String>sEvents){ savedEvents=sEvents; }

    public void setPastEventList(ArrayList<String>pEvents){pastEvents = pEvents;}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIcon(){return icon;}

    public void setIcon(String icon){this.icon = icon;}

    public void setId(String id) {this.id = id;}

    public String getId(){return id;}
    
    public void addSentFriendRequest(String id){sentFriendRequests.add(id);}

    public Boolean sentFriendRequestsContains(String id){return sentFriendRequests.contains(id);}

    public void deleteSentFriendRequestList(){sentFriendRequests.clear();}

    public void addFriend(String username) {friends.add(username); }

    public void removeFriend(String username) {friends.remove(username); }

    public void addEvent(String eventID) {events.add(eventID); }

    public void removeEvent(String eventID) { events.remove(eventID); }

    public void saveEvent(String eventID) { savedEvents.add(eventID); }

    public void removeSavedEvent(String eventID) { savedEvents.remove(eventID); }

    public void addPastEvent(String eventID) { pastEvents.add(eventID); }

    public  void removePastEvent(String eventID) { pastEvents.remove(eventID); }

}