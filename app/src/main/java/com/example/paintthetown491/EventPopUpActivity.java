package com.example.paintthetown491;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//this is the class that will include all the fields that are going to be displayed in the popup
public class EventPopUpActivity extends Activity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private EditText eName, eInfo, eDate, eTime;
    final java.util.Calendar c = java.util.Calendar.getInstance();
    private int dMonth, dDay, dYear, dHour, dMinute;
    private String event_id, event_date, event_time, period;
    private Button editEventButton, saveEventButton, people, places, invite_friends,favoriteEventButton, removeEventButton;
    DatabaseReference eRef;
    private ArrayList<String> locationsList, peopleList;
    ListView eLocation, ePeople;
    ArrayAdapter peopleArrayAdapter=null;

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
        // bind UI elements and initially disable editText fields
        eName = findViewById(R.id.editEventName);
        eName.setEnabled(false);
        eInfo = findViewById(R.id.editEventInfo);
        eInfo.setEnabled(false);
        eDate = findViewById(R.id.editDate);
        eDate.setEnabled(false);
        eTime = findViewById(R.id.editTime);
        eTime.setEnabled(false);
        eLocation = findViewById(R.id.locationListView);

        editEventButton = findViewById(R.id.editEventbtn);
        saveEventButton = findViewById(R.id.saveEventbtn);
        saveEventButton.setVisibility(View.INVISIBLE);

        favoriteEventButton = findViewById(R.id.favEventbtn);
        favoriteEventButton.setVisibility(View.INVISIBLE);

        removeEventButton = findViewById(R.id.rmvEventbtn);
        removeEventButton.setVisibility(View.INVISIBLE);


        invite_friends=findViewById(R.id.inviteFriendsButton);
        invite_friends.setVisibility(View.INVISIBLE);

        locationsList = new ArrayList<String>();

        //buttons to show people and events
        people=findViewById(R.id.people_events);
        places=findViewById(R.id.location_events);

        ePeople=findViewById(R.id.peopleListView);
        peopleList=new ArrayList<String>();

        //shows the people in the event
        people.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                eLocation.setVisibility(View.INVISIBLE);
                peopleArrayAdapter.notifyDataSetChanged();
                ePeople.setVisibility(View.VISIBLE);
            }
        });

        places.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                eLocation.setVisibility(View.VISIBLE);
                ePeople.setVisibility(View.INVISIBLE);
            }
        });

        // get eventID
        StringBuilder sb = new StringBuilder(getIntent().getStringExtra("eid"));
        sb.insert(0, "-"); // prepend - to event key
        event_id = sb.toString();

        // load the event data for the event the user clicked on
        try {
            loadEventData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // listener for when edit event btn is clicked
        editEventButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // hide edit event btn and enable edittext fields
                editEventButton.setVisibility(View.INVISIBLE);
                saveEventButton.setVisibility(View.VISIBLE);
                invite_friends.setVisibility(View.VISIBLE);
                eName.setEnabled(true);
                eInfo.setEnabled(true);
                eDate.setEnabled(true);
                eTime.setEnabled(true);

                // set onclick listeners for date & time
                eDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePicker();
                    }
                });

                eTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTimePicker();
                    }
                });
            }
        });

        saveEventButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // reference event to update
                eRef = FirebaseDbSingleton.getInstance().dbRef.child("Event").child(event_id);
                eRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.exists())
                        {
                            // updates hashmap holds all the new data to be updated
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("eventName", eName.getText().toString());
                            updates.put("eventInfo", eInfo.getText().toString());
                            updates.put("eventDate", eDate.getText().toString());
                            updates.put("eventTime", eTime.getText().toString());

                            // updates.put("eventLocation", eLocation.getText().toString());
                            // update the events child in the DB
                            eRef.updateChildren(updates);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });

                // disable edittext fields
                eName.setEnabled(false);
                eInfo.setEnabled(false);
                eDate.setEnabled(false);
                eTime.setEnabled(false);
                Toast.makeText(EventPopUpActivity.this, "Event has been updated!", Toast.LENGTH_SHORT).show();
                editEventButton.setVisibility(View.VISIBLE);
                invite_friends.setVisibility(View.INVISIBLE);
                saveEventButton.setVisibility(View.INVISIBLE);
            }
        });

        //handles adding friends to events
        invite_friends.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent inviteFriends=new Intent(getApplicationContext(),InviteFriendsActivity.class);
                inviteFriends.putExtra("eventID",event_id);
                startActivity(inviteFriends);
            }
        });
    }

    private void loadParticipantInfo()
    {
        for(int i=0;i<peopleList.size();i++)
        {
            final int index=i;
            //listener for specific user ID
            FirebaseDbSingleton.getInstance().dbRef.child("User").child(peopleList.get(i)).addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.exists())
                    {
                        User user=snapshot.getValue(User.class);
                        //replacing the user ID with the first and last name of participant
                        peopleList.set(index,user.getFirstName() + " " + user.getLastName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }
    }

    // fills the fields in the event UI
    public void loadEventData() throws ParseException {
        // retrieve data from events recycler view for current event selected and set it in corresponding text field
        eName.setText(getIntent().getStringExtra("ename"));
        eInfo.setText(getIntent().getStringExtra("einfo"));
        eDate.setText(getIntent().getStringExtra("edate"));
        String eDateStr = eDate.getText().toString();
        System.out.print(eDateStr);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date eDateObj = sdf.parse(eDateStr);
        System.out.print(eDateObj);
        Date now = new Date();

        if (now.after(eDateObj)) {
            editEventButton.setVisibility(View.INVISIBLE);
            favoriteEventButton.setVisibility(View.VISIBLE);
        }
        eTime.setText(getIntent().getStringExtra("etime"));
        locationsList = getIntent().getStringArrayListExtra("elocation");
        peopleList=getIntent().getStringArrayListExtra("ePeople");

        //get user info to display in events page (because value returned by FB is the user ID)
        loadParticipantInfo();

        //displays people in the events
        peopleArrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,peopleList);
        ePeople.setAdapter(peopleArrayAdapter);
        ePeople.setVisibility(View.INVISIBLE);

        // display locations in list view
        ArrayAdapter locationArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, locationsList);
        eLocation.setAdapter(locationArrayAdapter);
        eLocation.setVisibility(View.INVISIBLE);
    }

    // show date picker dialog
    public void showDatePicker()
    {
        dMonth = c.get(Calendar.MONTH);
        dDay = c.get(Calendar.DAY_OF_MONTH);
        dYear = c.get(Calendar.YEAR);

        DatePickerDialog datePicker = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, this, dYear, dMonth, dDay);
        datePicker.show();
    }

    // set selected date
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        event_date = (month+1) + "/" + dayOfMonth + "/" + year;
        eDate.setText(event_date);
    }

    // show time picker dialog
    public void showTimePicker() {
        dHour = c.get(Calendar.HOUR_OF_DAY);
        dMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePicker = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, this, 0,0, false);
        timePicker.show();
    }

    // set selected time
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        // hourOfDay is in 24 hr format
        if(hourOfDay > 12) {
            // convert hourOfDAY to 12 hr
            hourOfDay -= 12;
            period = " PM";
        }
        else if (hourOfDay == 0) {
            hourOfDay += 12;
            period = " AM";
        }
        else if(hourOfDay == 12) {
            period = " PM";
        }
        else {
            period = " AM";
        }

        event_time = String.format("%02d:%02d", hourOfDay, minute) + period;
        eTime.setText(event_time);
    }
}