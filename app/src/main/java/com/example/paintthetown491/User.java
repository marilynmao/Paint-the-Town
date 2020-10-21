package com.example.paintthetown491;
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
    private ArrayList<String> friends = new ArrayList<String>();
    private ArrayList<String> events = new ArrayList<String>();
    private ArrayList<String> savedEvents = new ArrayList<String>();
    private ArrayList<String> pastEvents = new ArrayList<String>();
    private ArrayList<String>pending=new ArrayList<String>();
    public User() { }

    //creates a new instance of the user class if it doesn't already exist. Returns the same instance if it does exist.
    public static User getInstance()
    {
        if(instance==null)
            instance= new User();
        return instance;
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


    public void addFriend(String username) {friends.add(username); }

    public void removeFriend(String username) {friends.remove(username); }

    public void addEvent(String eventID) {events.add(eventID); }

    public void removeEvent(String eventID) { events.remove(eventID); }

    public void saveEvent(String eventID) { savedEvents.add(eventID); }

    public void removeSavedEvent(String eventID) { savedEvents.remove(eventID); }

    public void addPastEvent(String eventID) { pastEvents.add(eventID); }

    public  void removePastEvent(String eventID) { pastEvents.remove(eventID); }

}