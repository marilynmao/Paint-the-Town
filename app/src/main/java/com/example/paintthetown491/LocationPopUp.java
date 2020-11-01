package com.example.paintthetown491;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class LocationPopUp extends Activity
{
    private Location location;
    private TextView loc_name;
    private TextView loc_address;
    private TextView phone;
    private RecyclerView reviews;
    private ImageView loc_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //binds the layout to this activity. You can find the xml in res.layout
        setContentView(R.layout.location_pop_up);

        //binds the xml
        loc_name=findViewById(R.id.popup_location_name);
        loc_address=findViewById(R.id.popup_location_address);
        loc_pic=findViewById(R.id.popup_location_image);
        phone=findViewById(R.id.popup_location_phone);
        reviews=findViewById(R.id.reviews);

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
        getWindow().setLayout((int) (w * 0.85), (int) (h * 0.85));


    }
}


