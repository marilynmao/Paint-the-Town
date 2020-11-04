package com.example.paintthetown491;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationPopUp extends Activity
{
    private Location location;
    private TextView loc_name;
    private TextView loc_address;
    private TextView phone;
    private RecyclerView reviewsR;
    private ArrayList<LocationReview>reviews;
    private ImageView loc_pic;
    private Button reviewBtn;
    LocationReviewAdapter locationReviewAdapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //binds the layout to this activity. You can find the xml in res.layout
        setContentView(R.layout.location_pop_up);

        //allocating memory for reviews
        reviews=new ArrayList<LocationReview>();
        //adding sample reviews
        reviews.add(new LocationReview("good place!","12/33/1222",4));
        reviews.add(new LocationReview("good place!","12/33/1222",4));
        reviews.add(new LocationReview("good place!","12/33/1222",4));
        reviews.add(new LocationReview("good place!","12/33/1222",4));

        //binds the xml
        loc_name=findViewById(R.id.popup_location_name);
        loc_address=findViewById(R.id.popup_location_address);
        loc_pic=findViewById(R.id.popup_location_image);
        phone=findViewById(R.id.popup_location_phone);
        reviewsR=findViewById(R.id.reviews);
        reviewBtn=findViewById(R.id.write_review);
        reviewsR.setHasFixedSize(true);
        reviewsR.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        locationReviewAdapter=new LocationReviewAdapter(reviews);
        reviewsR.setAdapter(locationReviewAdapter);

        //attaches listener for each item of the recyclerview
        locationReviewAdapter.setOnItemClickListener(new LocationReviewAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                System.out.println(position);
            }
        });

        //initializes the Location object with the Location object from the previous activity
        location=(Location) getIntent().getExtras().getSerializable("location");

        //sets the xml elements with the attributes of the Location object received from the previous activity
        loc_name.setText(location.getLocationName());
        loc_address.setText(location.getLocationInfo());
        phone.setText(location.getPhone());
        new DownloadImage(loc_pic).execute(location.getImageUrl());

        //closes the activity when you click outside
        this.setFinishOnTouchOutside(true);

        //will allow us to set the size of the popup relative to the screen size
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int w = metrics.widthPixels;
        int h = metrics.heightPixels;

        //0.6 indicates to make the popup 60% the size of the screen
        getWindow().setLayout((int) (w * 1), (int) (h * 1));



        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locReviewIntent = new Intent(getBaseContext(), ReviewPopUpActivity.class);
                locReviewIntent.putExtra("locationID", location.getLocationID());
                startActivity(locReviewIntent);
            }
        });


    }
}


