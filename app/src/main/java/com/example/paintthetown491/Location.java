package com.example.paintthetown491;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Location
{
    private String locationID;
    private ArrayList<String> reviews;
    private int rating;

    public Location(String locationID,int rating,ArrayList<String>reviews)
    {
        this.locationID=locationID;
        this.rating=rating;
        this.reviews=reviews;
    }

    public void setLocationID(String ID)
    {
        this.locationID=ID;
    }

    public void setRating(int rating)
    {
        if(rating<=5)
        {
            this.rating = rating;
        }
    }

    public void setReviews(ArrayList<String> reviews)
    {
        this.reviews=reviews;
    }

    public ArrayList<String> getReviews()
    {
        return reviews;
    }

    public String getLocationID()
    {
        return locationID;
    }

    public int getRating()
    {
        return rating;
    }
}
