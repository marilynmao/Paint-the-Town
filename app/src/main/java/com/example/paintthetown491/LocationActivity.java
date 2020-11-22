package com.example.paintthetown491;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LocationActivity extends Activity
{
    private ConstraintLayout mainLayout;
    private ConstraintLayout reviewPopUp;
    private TextView review_date;
    private TextView review_review;
    private TextView review_reviewer;
    private RatingBar review_rating;
    private String loc_id;
    private TextView loc_name;
    private TextView loc_address;
    private TextView phone;
    private TextView noReviews;
    private TextView price;
    private RatingBar rating;
    private RecyclerView reviewsR;
    private ArrayList<LocationReview>reviews;
    private ImageView loc_pic;
    private Button reviewBtn, addToEvent, closeRev;
    private LocationReviewAdapter locationReviewAdapter=null;

    public void listenForReviews()
    {
        //listener for the reviews in firebase
        ValueEventListener reviewsListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                reviews.clear();
                //check to see if firebase returned anything
                if (snapshot.exists())
                {
                    //hides the "no reviews" message and shows the recycler
                    reviewsR.setVisibility(View.VISIBLE);

                    //iterate through each child returned
                    for (DataSnapshot e : snapshot.getChildren())
                    {
                        LocationReview rev=e.getValue(LocationReview.class);
                        reviews.add(rev);
                    }
                }
                //no reviews, show message and hide recycler
                else
                {
                    reviewsR.setVisibility(View.INVISIBLE);
                }
                //notifies the adapter of any changes
                locationReviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        };

        Query query=FirebaseDbSingleton.getInstance().dbRef.child("Location").child(loc_id).child("reviews");
        query.addValueEventListener(reviewsListener);
    }

    //looks up the user associated with the review
    private void lookUpReviewer(String id)
    {
        if(id.charAt(0)=='-')
        {
            id=id.substring(1);
        }

        FirebaseDbSingleton.getInstance().dbRef.child("User").child(id).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    User user=snapshot.getValue(User.class);
                    review_reviewer.setText("Reviewed by: "+user.getFirstName().toString()+user.getLastName().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //binds the layout to this activity. You can find the xml in res.layout
        setContentView(R.layout.location_activity);

        //allocating memory for reviews
        reviews=new ArrayList<LocationReview>();

        //adding sample reviews
        //reviews.add(new LocationReview(FirebaseDbSingleton.getInstance().user.getUid(),"good place333333333333333333!","12/33/1222",4));
        //reviews.add(new LocationReview(FirebaseDbSingleton.getInstance().user.getUid(),"good place!","12/33/1222",4));
        //reviews.add(new LocationReview("MGbBq2IUhyqbFSnFkpk","good place!","12/33/1222",4));
        //reviews.add(new LocationReview(FirebaseDbSingleton.getInstance().user.getUid(),"good place!","12/33/1222",4));

        //binds the xml
        mainLayout=findViewById(R.id.main);
        reviewPopUp=findViewById(R.id.review_popup_layout);
        loc_name=findViewById(R.id.popup_location_name);
        loc_address=findViewById(R.id.popup_location_address);
        loc_pic=findViewById(R.id.popup_location_image);
        phone=findViewById(R.id.popup_location_phone);
        reviewsR=findViewById(R.id.reviews);
        rating=findViewById(R.id.popup_location_rating);
        price=findViewById(R.id.popup_location_price);
        reviewBtn=findViewById(R.id.write_review);
        addToEvent=findViewById(R.id.add_to_ev);
        review_date=findViewById(R.id.review_popup_date);
        review_review=findViewById(R.id.review_popup_review);
        review_reviewer=findViewById(R.id.review_popup_reviewer);
        review_rating=findViewById(R.id.review_popup_rating);
        closeRev=findViewById(R.id.close_review);

        //setting the nested layout as invisible
        reviewPopUp.setVisibility(View.INVISIBLE);

        //setting properties of the recycler
        reviewsR.setHasFixedSize(true);
        reviewsR.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //creating adapter with our reviews
        locationReviewAdapter=new LocationReviewAdapter(reviews);

        //using the adapter for the recycler
        reviewsR.setAdapter(locationReviewAdapter);

        //attaches listener for each item of the recyclerview
        locationReviewAdapter.setOnItemClickListener(new LocationReviewAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                LocationReview review=reviews.get(position);
                review_date.setText("Review date: "+review.getDate());
                review_review.setText(review.getReview());
                review_rating.setRating(review.getRating());
                lookUpReviewer(review.getReviewerUserID());
                reviewPopUp.setVisibility(View.VISIBLE);
                reviewsR.setVisibility(View.INVISIBLE);
                addToEvent.setEnabled(false);
                reviewBtn.setEnabled(false);
            }
        });

        //initializes the Location object with the Location object from the previous activity
        final Location location = (Location) getIntent().getExtras().getSerializable("location");

        //location.setReviews(reviews);
        //FirebaseDbSingleton.getInstance().dbRef.child("Location").child(location.getLocationID()).setValue(location);
        //FirebaseDbSingleton.getInstance().dbRef.child("Location").child(location.getLocationID()).child("reviews").child("MGbBq2IUhyqbFSnFkpk").setValue(reviews.get(2));

        //sets the xml elements with the attributes of the Location object received from the previous activity
        loc_id=location.getLocationID();
        loc_name.setText("Name: "+ location.getLocationName());
        loc_name.setTypeface(loc_name.getTypeface(), Typeface.BOLD_ITALIC);
        loc_address.setText("Address: "+ location.getLocationInfo());
        loc_address.setTypeface(loc_name.getTypeface(), Typeface.BOLD_ITALIC);
        phone.setText("Phone: "+ location.getPhone());
        phone.setTypeface(loc_name.getTypeface(), Typeface.BOLD_ITALIC);
        rating.setRating(location.getRating());
        price.setText("Price: "+ location.getPrice());
        price.setTypeface(loc_name.getTypeface(), Typeface.BOLD_ITALIC);
        new DownloadImage(loc_pic).execute(location.getImageUrl());

        //function that begins listener for location reviews
        listenForReviews();

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locReviewIntent = new Intent(getApplicationContext(), ReviewPopUpActivity.class);
                locReviewIntent.putExtra("locationID", loc_id);
                startActivity(locReviewIntent);
            }
        });

        addToEvent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent addEventIntent = new Intent(getApplicationContext(), AddLocationActivity.class);
                addEventIntent.putExtra("locationID", loc_id);
                addEventIntent.putExtra("locationName", location.getLocationName());
                FirebaseDbSingleton.getInstance().dbRef.child("Location").child(loc_id).child("locationID").setValue(loc_id);

                startActivity(addEventIntent);

            }
        });

        closeRev.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                reviewPopUp.setVisibility(View.INVISIBLE);
                reviewsR.setVisibility(View.VISIBLE);
                addToEvent.setEnabled(true);
                reviewBtn.setEnabled(true);
            }
        });
    }
}


