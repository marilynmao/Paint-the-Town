package com.example.paintthetown491;

import android.widget.ImageView;
import java.util.ArrayList;

public class Location
{
    private String locationID;
    private String locationName;
    private String locationInfo;
    private String imageUrl;
    private ArrayList<String> reviews;
    private int rating;

    public Location(String locationID, String locName, String imgUrl, String locationInfo,int rating)
    {
        this.locationID=locationID;
        this.rating=rating;
        this.locationName=locName;
        this.imageUrl=imgUrl;
        this.locationInfo=locationInfo;
    }

    public String getLocationName() {
        return locationName;
    }


    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
