package com.example.paintthetown491;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LocationActivity extends Activity
{
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
    private Button reviewBtn, addToEvent;
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
        loc_name=findViewById(R.id.popup_location_name);
        loc_address=findViewById(R.id.popup_location_address);
        loc_pic=findViewById(R.id.popup_location_image);
        phone=findViewById(R.id.popup_location_phone);
        reviewsR=findViewById(R.id.reviews);
        rating=findViewById(R.id.popup_location_rating);
        price=findViewById(R.id.popup_location_price);
        reviewBtn=findViewById(R.id.write_review);
        addToEvent=findViewById(R.id.add_to_ev);

        //setting the message to invisible
        //noReviews.setVisibility(View.INVISIBLE);

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
                Intent popup=new Intent(getApplicationContext(), ReviewPopUp.class);
                popup.putExtra("review",review);
                startActivity(popup);
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

        //closes the activity when you click outside
        this.setFinishOnTouchOutside(true);

        //will allow us to set the size of the popup relative to the screen size
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int w = metrics.widthPixels;
        int h = metrics.heightPixels;

        //1 indicates to make the popup 100% the size of the screen
        getWindow().setLayout((int) (w * 1), (int) (h * 1));

        //function that begins listener for location reviews
        listenForReviews();

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent locReviewIntent = new Intent(getBaseContext(), ReviewPopUpActivity.class);
                //locReviewIntent.putExtra("locationID", location.getLocationID());
                //startActivity(locReviewIntent);
            }
        });

        addToEvent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });
    }
}


