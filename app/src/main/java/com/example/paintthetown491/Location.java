package com.example.paintthetown491;

import java.io.Serializable;
import java.util.ArrayList;

public class Location implements Serializable
{
    private String locationID;
    private String locationName;
    private String locationInfo;
    private String imageUrl;
    private String phone;
    private String latitude;
    private String longitude;
    private String price;
    private ArrayList<String> reviews;
    private float rating;

    public Location(String locationID, String locName, String imgUrl, String locationInfo,float rating, String phone, String longitude, String latitude, String price)
    {
        this.locationID=locationID;
        this.rating=rating;
        this.locationName=locName;
        this.imageUrl=imgUrl;
        this.phone=phone;
        this.price=price;
        this.longitude=longitude;
        this.latitude=latitude;
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

    public void setPhone(String phone) {this.phone=phone;}

    public String getPhone(){return this.phone;}

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

    public float getRating()
    {
        return rating;
    }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
