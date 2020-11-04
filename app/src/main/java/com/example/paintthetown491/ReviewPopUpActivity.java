package com.example.paintthetown491;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;

public class ReviewPopUpActivity extends Activity {

    private EditText reviewText;
    private RatingBar rating;
    private Button postBtn, cancelBtn;
    private String locationID;
    private ArrayList<String> userReview;
    private float userRating = 0;
    private Calendar date;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_input_pop_up);

        reviewText = findViewById(R.id.reviewInput);
        rating = findViewById(R.id.locationRatingBar);
        postBtn = findViewById(R.id.postReview);
        cancelBtn = findViewById(R.id.cancelReview);

        rating.setNumStars(5);

        date = Calendar.getInstance();

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationID = getIntent().getStringExtra("locationID");
                userRating = rating.getRating();
                String review = reviewText.getText().toString();


                if (userRating == 0){
                    Toast.makeText(ReviewPopUpActivity.this, "Please select a star rating.", Toast.LENGTH_LONG).show();
                }
                else if (review.equals(null)){
                    reviewText.setError("Please write your review here.");
                }
                else{
                    //TODO check if the location is already in the DB or not


                    //TODO if it is not add it to the DB


                    userReview.add(review);
                    userReview.add(String.valueOf(userRating));
                    userReview.add(date.MONTH + "/" + date.DATE + "/" + date.YEAR);

                    FirebaseDbSingleton.getInstance().dbRef.child("Location").child(locationID).child(FirebaseDbSingleton.getInstance().firebaseAuth.getUid()).setValue(userReview);
                    Toast.makeText(ReviewPopUpActivity.this, "Review published!",Toast.LENGTH_SHORT);
                    finish();
                }


            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

