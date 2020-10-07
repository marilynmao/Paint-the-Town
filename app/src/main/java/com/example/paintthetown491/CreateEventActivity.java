package com.example.paintthetown491;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import java.util.Calendar;

public class CreateEventActivity extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private EditText eName, eInfo,eLocation, eDate, eTime;

    final Calendar c = Calendar.getInstance();
    private int dMonth, dDay, dYear, dHour, dMinute;
    private String event_name, event_info, event_location, event_date, event_time, period;
    private Button createEventButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.frag_create_event, container, false);
        super.onCreate(savedInstanceState);

        eName = view.findViewById(R.id.newEventName);
        eInfo = view.findViewById(R.id.newEventInfo);
        eDate = view.findViewById(R.id.selectDate);
        eTime = view.findViewById(R.id.selectTime);
        eLocation = view.findViewById(R.id.newEventLocation);
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
                // =======================================
                //  post event data to database here???
                // =======================================

                event_name = eName.getText().toString();
                event_info = eInfo.getText().toString();
                event_location = eLocation.getText().toString();
                //open main event activity (shows created event) when create event is clicked
                MainEventActivity mainEvent = new MainEventActivity();
                Bundle b = new Bundle();
                b.putString("event name", event_name);
                b.putString("event info", event_info);
                b.putString("event date", event_date);
                b.putString("event time", event_time);
                b.putString("event location", event_location);
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
        event_date = (month+1) + " / " + dayOfMonth + " / " + year;
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
        if(hourOfDay >= 12) {
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