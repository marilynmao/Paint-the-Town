package com.example.paintthetown491;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CreateEventActivity extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private EditText eName, eInfo, eLocation, eDate, eTime;
    final Calendar c = Calendar.getInstance();
    private int dMonth, dDay, dYear, dHour, dMinute;
    private String newEventID, eventCreatorName, event_name, event_info, event_date, event_time, period;
    private Button createEventButton;
    private DatabaseReference eventsDbRef, userEventsDbRef;
    //private User usr;
    private Event newEvent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_create_event, container, false);
        super.onCreate(savedInstanceState);

        eName = view.findViewById(R.id.newEventName);
        eInfo = view.findViewById(R.id.newEventInfo);
        eDate = view.findViewById(R.id.selectDate);
        eTime = view.findViewById(R.id.selectTime);
        createEventButton = view.findViewById(R.id.createEventbtn);

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

        // onclick listener for create event button
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ===========================
                // get instance of user
                //usr = User.getInstance();
                // get event creator name
                //eventCreatorName = (usr.getFirstName() + " " + usr.getLastName());
                // ===========================

                // added user singleton after account was already registered with firebase, so event creator name is temporarily hardcoded
                eventCreatorName = "Marilyn Mao";
                // set variables
                event_name = eName.getText().toString();
                event_info = eInfo.getText().toString();
                ArrayList<String> event_location = new ArrayList<>();
                event_location.add("");

                // ===== DUMMY DATA FOR PARTICIPANTS =====
                ArrayList<String> participantIds=new ArrayList<>();
                participantIds.add("7iPPl1ZXgaTnyAtqWNfKgtUgBcb2");
                participantIds.add("KX1UfoLwTQOGTFHxyguqcl7i5YQ2");
                participantIds.add("7iPPl1ZXgaTnyAtqWNfKgtUgBcb2");

                // references event node in db
                eventsDbRef = FirebaseDbSingleton.getInstance().dbRef.child("Event");
                // references events list in user node -  User/{Uid}/events
                userEventsDbRef = FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("events");
                // get new push event id key
                newEventID = eventsDbRef.push().getKey();

                // create new event item
                newEvent = new Event(newEventID, event_name, event_date, eventCreatorName, participantIds, event_time, event_location, event_info);
                // add new event to Event entry using push key
                eventsDbRef.child(newEventID).setValue(newEvent);

                // remove "-" from newEventID
                newEventID = newEventID.substring(1);
                
                // get push key for User/{Uid}/events
                String userEventsKey = userEventsDbRef.push().getKey();
                // hashmap holds the new event to be added to User/{Uid}/events
                HashMap usrEvent = new HashMap();
                // put pushkey and new event id in hash map
                usrEvent.put(userEventsKey, newEventID);
                // append new event to User/{Uid}/events
                userEventsDbRef.updateChildren(usrEvent);


                Toast.makeText(getActivity(), "Success! New event created.", Toast.LENGTH_SHORT).show();
                //open main event activity (shows created event) when create event is clicked
                MainEventActivity mainEvent = new MainEventActivity();
                Bundle b = new Bundle();
                b.putString("event name", event_name);
                b.putString("event info", event_info);
                b.putString("event date", event_date);
                b.putString("event time", event_time);
                mainEvent.setArguments(b);
                getParentFragmentManager().beginTransaction().replace(R.id.container_frag, mainEvent).commit();
            }
        });

        return view;
    }

    // show date picker dialog
    public void showDatePicker() {
        dMonth = c.get(Calendar.MONTH);
        dDay = c.get(Calendar.DAY_OF_MONTH);
        dYear = c.get(Calendar.YEAR);

        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, this, dYear, dMonth, dDay);
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
        TimePickerDialog timePicker = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, this, 0,0, false);
        timePicker.show();
    }

    // set selected time
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // hourOfDay is in 24 hr format
        if(hourOfDay > 12) {
            // convert hourOfDAY to 12 hr
            hourOfDay -= 12;
            period = " PM";
        }
        else {
            period = " AM";
        }

        event_time = String.format("%02d:%02d", hourOfDay, minute) + period;
        eTime.setText(event_time);
    }
}