package com.example.paintthetown491;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainEventActivity extends AppCompatActivity {

    private ImageView event_image;
    private TextView event_name, event_info, event_date, event_time, event_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_event);

        event_image = findViewById(R.id.eventImage);
        event_name = findViewById(R.id.mainEventName);
        event_info = findViewById(R.id.mainEventInfo);
        event_date = findViewById(R.id.mainEventDate);
        event_time = findViewById(R.id.mainEventTime);
        event_location = findViewById(R.id.mainEventLocation);

        // set event data

    }
}