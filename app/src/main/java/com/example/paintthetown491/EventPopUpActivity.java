package com.example.paintthetown491;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

//this is the class that will include all the fields that are going to be displayed in the popup
public class EventPopUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //binds the layout to this activity. You can find the xml in res.layout
        setContentView(R.layout.activity_event_pop_up);

        //will allow us to set the size of the popup relative to the screen size
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int w=metrics.widthPixels;
        int h=metrics.heightPixels;

        //0.6 indicates to make the popup 60% the size of the screen
        getWindow().setLayout((int)(w*0.6),(int)(h*0.6));

    }
}