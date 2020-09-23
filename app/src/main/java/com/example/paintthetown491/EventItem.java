package com.example.paintthetown491;

import android.util.EventLog;
import android.widget.TextView;

public class EventItem
{
    private int view;
    private String text1, text2;

    public EventItem(int source, String text, String otherText)
    {
        view=source;
        text1=text;
        text2=otherText;
    }

    public int getImage()
    {
        return view;
    }

    public String getText1()
    {
        return text1;
    }

    public String getText2()
    {
        return text2;
    }
}
