package com.example.paintthetown491;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddLocationActivity extends AppCompatActivity {
    private RecyclerView addLocationRecycler;
    private AddLocationAdapter addLocationAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference eventRef;
    private ArrayList<String> eventIds;
    private ArrayList<Event> events;
    private Button addToEventBtn, cancelBtn;
    private String locationID, locationName, selectedEventId, selectedEventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        // bind elements
        addToEventBtn = findViewById(R.id.add_to_event_btn);
        cancelBtn = findViewById(R.id.cancel_add);

        // retrieve location ID & name from location Activity
        locationID = getIntent().getStringExtra("locationID");
        locationName = getIntent().getStringExtra("locationName");

        //holds the event IDs for each user
        eventIds = new ArrayList<String>();
        //holds the events loaded from firebase
        events = new ArrayList<Event>();


        final Query queryVal = FirebaseDbSingleton.getInstance().dbRef.child("Event");

        //listener for the values we want
        final ValueEventListener eventValListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                // clear events list...this prevents each event card from duplicating after updating
                events.clear();
                // clears the radio button selection
                addLocationAdapter.clearSelection();
                //check that firebase returned something
                if (snapshot.exists())
                {
                    //iterate through each child returned
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        String s=ds.getKey();

                        if(ds.getKey().charAt(0)=='-')
                        {
                            //removes a "-" character appended to the beginning of each key
                            s = ds.getKey().substring(1);
                        }

                        //sees if the arraylist contains this child
                        if (eventIds.contains(s))
                        {
                            //create the event object with the properties returned from firebase
                            Event e = new Event(s, ds.child("eventName").getValue().toString(), ds.child("eventDate").getValue().toString(), ds.child("eventCreator").getValue().toString(), getCollectionFromIterable(ds.child("participantList").getChildren()), ds.child("eventTime").getValue().toString(), getLocationCollectionFromIterable(ds.child("eventLocation").getChildren()), ds.child("eventInfo").getValue().toString());
                            //add it to the arraylist that goes into the adapter
                            events.add(e);
                        }
                        else {
                            System.out.println("here");
                        }
                    }
                }
                //notifies the adapter of any changes
                addLocationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }

            //used to convert a Iterable (type returned from firebase) to an arraylist
            public ArrayList<String> getCollectionFromIterable(Iterable<DataSnapshot> itr)
            {
                ArrayList<String> participants = new ArrayList<String>();
                for (DataSnapshot id : itr)
                {
                    participants.add(id.toString());
                }
                return participants;
            }

            //used to convert a Iterable (type returned from firebase) to an arraylist
            public ArrayList<String> getLocationCollectionFromIterable(Iterable<DataSnapshot> itr)
            {
                ArrayList<String> locations = new ArrayList<String>();
                for (DataSnapshot id : itr)
                {
                    locations.add(id.toString());
                }
                return locations;
            }
        };


        //listener for the event IDs in firebase
        ValueEventListener eventIdValListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                eventIds.clear();
                //check to see if firebase returned anything
                if (snapshot.exists())
                {
                    //iterate through each child returned
                    for (DataSnapshot e : snapshot.getChildren())
                    {
                        //holds the event ID as a string
                        String event = e.getValue(String.class);
                        //adds it to the list
                        eventIds.add(event);
                    }
                    //notifies the adapter of any changes
                    addLocationAdapter.notifyDataSetChanged();

                    for (String id : eventIds)
                    {
                        queryVal.equalTo(id);
                        queryVal.addValueEventListener(eventValListener);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        };

        //querying the specific user's list of events
        Query queryID = FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("events");
        //attaching the value listener
        queryID.addValueEventListener(eventIdValListener);

        eventRef = FirebaseDbSingleton.getInstance().dbRef.child("Event");

        addLocationRecycler = findViewById(R.id.events);
        addLocationRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(AddLocationActivity.this);
        addLocationAdapter = new AddLocationAdapter(events);
        addLocationRecycler.setLayoutManager(layoutManager);
        addLocationRecycler.setAdapter(addLocationAdapter);

        // listener for event selected
        addLocationAdapter.setOnItemClickListener(new AddLocationAdapter.OnItemClickListener() {
            @Override
            public void getEventOnclick(int position) {
                // get selected event id & name
                selectedEventId = "-" + events.get(position).getEventId();
                selectedEventName = events.get(position).getEventName();
            }
        });

        // add to event
        addToEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add location to selected event
                eventRef.child(selectedEventId).child("eventLocation").child(locationID).setValue(locationName);

                // TODO: save locationID to firebase

                Toast.makeText(AddLocationActivity.this, "The location has been added to " + selectedEventName + "!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        // cancel & go back to location popup
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}