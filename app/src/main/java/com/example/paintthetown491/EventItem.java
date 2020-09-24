package com.example.paintthetown491;

//the class that holds all the data needed for an event
public class EventItem
{
    private int view;
    private String eventName, eventDate, eventCreator;

    public EventItem(int source, String name, String date, String creator /* additional parameters go here when we determine what they are */)
    {
        view=source;
        eventName=name;
        eventDate=date;
        eventCreator=creator;
    }

    public int getImage()
    {
        return view;
    }

    public String getEventName()
    {
        return eventName;
    }

    public String getEventDate()
    {
        return eventDate;
    }

    public String getEventCreator()
    {
        return eventCreator;
    }
}
