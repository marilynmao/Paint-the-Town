package com.example.paintthetown491;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

//the class that holds all the data needed for an event
public class Event
{
    private int view;
    private String eventName, eventDate, eventCreator, eventTime, eventInfo;
    private ArrayList<String>participantIds;
    private ArrayList<String> eventLocation;
    // excludes eventId field from posting to db
    @Exclude
    public String eventId;

    public Event(String eId, String name, String date, String creator, ArrayList<String>pIds, String time, ArrayList<String> location, String info /* additional parameters go here when we determine what they are */)
    {
        eventId=eId;
        eventName=name;
        eventDate=date;
        eventCreator=creator;
        participantIds=pIds;
        eventTime=time;
        eventLocation=location;
        eventInfo=info;
    }

    @Exclude
    public String getEventId() { return eventId; }

    //returns the list of user IDs for people in the event
    public ArrayList<String> getParticipantList(){return participantIds;}

    public int getImage()
    {
        return view;
    }

    //gets the event name
    public String getEventName()
    {
        return eventName;
    }

    //gets the event date
    public String getEventDate()
    {
        return eventDate;
    }

    //gets the event creator
    public String getEventCreator()
    {
        return eventCreator;
    }

    public String getEventTime()
    {
        return eventTime;
    }

    public ArrayList<String> getEventLocation() { return eventLocation; }

    public String getEventInfo()
    {
        return eventInfo;
    }

    //sets the event name
    public void setEventName(String eventN)
    {
        eventName=eventN;
    }

    //sets the event date
    public void setEventDate(String eventD)
    {
        eventDate=eventD;
    }

    //sets the event creator
    public void setEventCreator(String eventC)
    {
        eventCreator=eventC;
    }

    //sets the event participants (by IDs)
    public void setEventParticipants(ArrayList<String> eventP)
    {
        participantIds=eventP;
    }

    public void setEventTime(String eventT)
    {
        eventTime=eventT;
    }

    public void setEventLocation(ArrayList<String> eventL)
    {
        eventLocation=eventL;
    }

    public void setEventInfo(String eventI)
    {
        eventInfo=eventI;
    }

    @Exclude
    public void setEventId(String evntID) { eventId=evntID; }
}
