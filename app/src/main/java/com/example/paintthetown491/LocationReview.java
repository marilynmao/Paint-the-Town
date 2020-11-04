package com.example.paintthetown491;

public class LocationReview
{
    private String reviewerUserID;
    private String review;
    private String date;
    private float rating;

    public LocationReview(){}

    public LocationReview(String rev, String date, float rating)
    {
        this.reviewerUserID=FirebaseDbSingleton.getInstance().firebaseAuth.getUid();
        this.date=date;
        this.review=rev;
        this.rating=rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewerUserID() { return reviewerUserID; }

    public void setReviewerUserID(String reviewUserID) { this.reviewerUserID = reviewUserID; }
}
