package com.example.paintthetown491;

import java.util.List;

//the class that holds all the data needed for an event
public class EventItem
{
    private int view;
    private String eventName, eventDate, eventCreator, eventTime, eventLocation, eventInfo;
    private List<String>participantIds;


    public EventItem(int source,String name, String date, String creator, List<String>pIds, String time, String location, String info /* additional parameters go here when we determine what they are */)
    {
        eventName=name;
        eventDate=date;
        eventCreator=creator;
        participantIds=pIds;
        eventTime=time;
        eventLocation=location;
        eventInfo=info;
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

    public String getEventTime()
    {
        return eventTime;
    }

    public String getEventLocation()
    {
        return eventLocation;
    }

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
    public void setEventParticipants(List<String> eventP)
    {
        participantIds=eventP;
    }

    public void setEventTime(String eventT)
    {
        eventTime=eventT;
    }

    public void setEventLocation(String eventL)
    {
        eventLocation=eventL;
    }

    public void setEventInfo(String eventI)
    {
        eventInfo=eventI;
    }
}
