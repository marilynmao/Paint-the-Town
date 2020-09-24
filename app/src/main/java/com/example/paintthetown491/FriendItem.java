package com.example.paintthetown491;

public class FriendItem { private int view;
    private String Name, UserID;

    public FriendItem(int source, String name, String userID/* additional parameters go here when we determine what they are */)
    {
        view=source;
        Name= name;
        UserID = userID;

    }

    public int getImage()
    {
        return view;
    }

    public String getFriendName()
    {
        return Name;
    }

    public String getUserID()
    {
        return UserID;
    }

}
