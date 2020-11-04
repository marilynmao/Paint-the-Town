package com.example.paintthetown491;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class Location implements Serializable
{
    private String locationID;
    @Exclude
    private String locationName;
    @Exclude
    private String locationInfo;
    @Exclude
    private String imageUrl;
    @Exclude
    private String phone;
    @Exclude
    private String latitude;
    @Exclude
    private String longitude;
    @Exclude
    private String price;
    @Exclude
    private ArrayList<LocationReview> reviews;
    private float rating;

    public Location(String locationID, String locName, String imgUrl, String locationInfo,float rating, String phone, String longitude, String latitude, String price)
    {
        this.locationID=locationID;
        this.rating=rating;
        this.locationName=locName;
        this.imageUrl=imgUrl;
        this.rating=rating;
        this.phone=phone;
        this.price=price;
        this.longitude=longitude;
        this.latitude=latitude;
        this.locationInfo=locationInfo;
    }

    //GETTERS
    @Exclude
    public String getLocationName() {
        return locationName;
    }

    @Exclude
    public String getLocationInfo() {
        return locationInfo;
    }

    public String getLocationID()
    {
        return locationID;
    }

    public float getRating()
    {
        return rating;
    }

    @Exclude
    public String getImageUrl() { return imageUrl; }

    @Exclude
    public String getPrice() { return price; }

    @Exclude
    public String getLongitude() { return longitude; }

    public ArrayList<LocationReview> getReviews()
    {
        return reviews;
    }

    @Exclude
    public String getLatitude() { return latitude; }

    @Exclude
    public String getPhone(){return this.phone;}

    //SETTERS
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

    public void setPhone(String phone) {this.phone=phone;}

    public void setRating(int rating)
    {
        if(rating<=5)
        {
            this.rating = rating;
        }
    }

    public void setReviews(ArrayList<LocationReview> reviews)
    {
        this.reviews=reviews;
    }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public void setPrice(String price) { this.price = price; }
}
