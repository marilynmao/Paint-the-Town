package com.example.paintthetown491;
import java.util.ArrayList;
public class User
{
    private String firstName, lastName, email, username, phoneNumber,id;
    private ArrayList<String> friends = new ArrayList<String>();
    private ArrayList<String> events = new ArrayList<String>();
    private ArrayList<String> savedEvents = new ArrayList<String>();
    private ArrayList<String> pastEvents = new ArrayList<String>();
    private ArrayList<String>pending=new ArrayList<String>();
    public User() { }

    public void setEventList(ArrayList<String>sEvents){ savedEvents=sEvents;
    }
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

    public String getId(){return id;}

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

    public void addFriend(String username) {friends.add(username); }

    public void removeFriend(String username) {friends.remove(username); }

    public void addEvent(String eventID) {events.add(eventID); }

    public void removeEvent(String eventID) { events.remove(eventID); }

    public void saveEvent(String eventID) { savedEvents.add(eventID); }

    public void removeSavedEvent(String eventID) { savedEvents.remove(eventID); }

    public void addPastEvent(String eventID) { pastEvents.add(eventID); }

    public  void removePastEvent(String eventID) { pastEvents.remove(eventID); }

}