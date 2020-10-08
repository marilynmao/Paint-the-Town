package com.example.paintthetown491;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class MainEventActivity extends Fragment {

    private TextView mainevent_name, mainevent_info, mainevent_date, mainevent_time, mainevent_location;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.activity_main_event, container, false);


        mainevent_name = view.findViewById(R.id.mainEventName);
        mainevent_info = view.findViewById(R.id.mainEventInfo);
        mainevent_date = view.findViewById(R.id.mainEventDate);
        mainevent_time = view.findViewById(R.id.mainEventTime);
        mainevent_location = view.findViewById(R.id.mainEventLocation);

        // get event data and set textview for each field
        mainevent_name.setText(getArguments().getString("event name"));
        mainevent_info.setText(getArguments().getString("event info"));
        mainevent_date.setText(getArguments().getString("event date"));
        mainevent_time.setText(getArguments().getString("event time"));
        mainevent_location.setText(getArguments().getString("event location"));

        return view;
    }
}