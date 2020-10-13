package com.example.paintthetown491;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

//this is the class that will include all the fields that are going to be displayed in the popup
public class EventPopUpActivity extends Activity {
    private EditText eName, eInfo,eLocation, eDate, eTime;
    final java.util.Calendar c = java.util.Calendar.getInstance();
    private int dMonth, dDay, dYear, dHour, dMinute;
    private String event_name, event_info, event_location, event_date, event_time, period;
    private Button editEventButton, saveEventButton;


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
        getWindow().setLayout((int)(w*0.8),(int)(h*0.8));
        // bind UI elements and intially disable editText fields
        eName = findViewById(R.id.editEventName);
        eName.setEnabled(false);
        eInfo = findViewById(R.id.editEventInfo);
        eInfo.setEnabled(false);
        eDate = findViewById(R.id.editDate);
        eDate.setEnabled(false);
        eTime = findViewById(R.id.editTime);
        eTime.setEnabled(false);
        eLocation = findViewById(R.id.editEventLocation);
        eLocation.setEnabled(false);
        editEventButton = findViewById(R.id.editEventbtn);
        saveEventButton = findViewById(R.id.saveEventbtn);
        saveEventButton.setVisibility(View.INVISIBLE);

        // loads the event data from event user clicked on
        loadEventData();

        // listener for when edit event btn is clicked
        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide save event btn and enable edittext fields
                editEventButton.setVisibility(View.INVISIBLE);
                saveEventButton.setVisibility(View.VISIBLE);
                eName.setEnabled(true);
                eInfo.setEnabled(true);
                eDate.setEnabled(true);
                eTime.setEnabled(true);
                eLocation.setEnabled(true);
            }
        });

        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // need to update database with edited event
             editEventButton.setVisibility(View.VISIBLE);
             saveEventButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    // fills the fields in the event UI
    public void loadEventData()
    {
        // retrieve data from events recycler view for current event selected and set it in corresponding text field
        eName.setText(getIntent().getStringExtra("ename"));
        eInfo.setText(getIntent().getStringExtra("einfo"));
        eDate.setText(getIntent().getStringExtra("edate"));
        eTime.setText(getIntent().getStringExtra("etime"));
        eLocation.setText(getIntent().getStringExtra("elocation"));
    }
}