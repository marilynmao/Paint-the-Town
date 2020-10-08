package com.example.paintthetown491;

import java.util.List;

//the class that holds all the data needed for an event
public class EventItem
{
    private int view;
    private String eventName, eventDate, eventCreator;
    private List<String>participantIds;

    public EventItem(String name, String date, String creator, List<String>pIds /* additional parameters go here when we determine what they are */)
    {
        eventName=name;
        eventDate=date;
        eventCreator=creator;
        participantIds=pIds;
    }

    //returns the list of user IDs for people in the event
    public List<String> getParticipantList(){return participantIds;};

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
    public void setEventParticipants(List<String> eventP)
    {
        participantIds=eventP;
    }
}
