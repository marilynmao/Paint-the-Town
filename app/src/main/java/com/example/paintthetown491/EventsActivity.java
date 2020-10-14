package com.example.paintthetown491;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//class that holds elements needed for our events fragment
public class EventsActivity extends Fragment
{
    private RecyclerView eventsRecycler;
    private EventAdapter eAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference eventRef;
    private ArrayList<String> eventIds;
    private ArrayList<EventItem> events;
    private Button createEventButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //the inflate() method takes the layout you wanna show as the first parameter
        final View view = inflater.inflate(R.layout.frag_events, container, false);

        //holds the event IDs for each user
        eventIds = new ArrayList<String>();
        //holds the events loaded from firebase
        events = new ArrayList<EventItem>();

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
                    eAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        };

        //querying the specific user's list of events
        Query query = FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid().toString()).child("events");
        //attaching the value listener
        query.addValueEventListener(eventIdValListener);

        //now that we have the event IDs saved, we need to look at the Event table for each of them
        eventRef = FirebaseDbSingleton.getInstance().dbRef.child("Event");
        //listener for the values we want
        eventRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
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
                            EventItem e = new EventItem(R.drawable.ic_baseline_event_24, ds.child("eventName").getValue().toString(), ds.child("eventDate").getValue().toString(), ds.child("eventCreator").getValue().toString(), getCollectionFromIterable(ds.child("participantList").getChildren()), "12:23", "ff", "testing");
                            //add it to the arraylist that goes into the adapter
                            events.add(e);
                        }
                    }
                }
                //notifies the adapter of any changes
                eAdapter.notifyDataSetChanged();
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
        });

        // add create event button at bottom of events recyclerview page
        createEventButton = view.findViewById(R.id.createNewEventBtn);

        //////////////////////////////////////inserting event data to DB for locations
        /*//dummy data to post to the DB (for posting to DB)
        ArrayList<String>participantIds=new ArrayList<>();
        participantIds.add("7iPPl1ZXgaTnyAtqWNfKgtUgBcb2");
        participantIds.add("KX1UfoLwTQOGTFHxyguqcl7i5YQ2");
        participantIds.add("7iPPl1ZXgaTnyAtqWNfKgtUgBcb2");*/

        //dummy data to test the recyclerview (for retrieving from DB)
        ArrayList<Location> sampleData=new ArrayList<>();
        ArrayList<String>reviews=new ArrayList<>();
        reviews.add("This place is the best. I love it!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        reviews.add("This place sucks. I hate it!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        reviews.add("This place is okay. I think we might be back soon to give it another shot!");


        sampleData.add(new Location("YqvoyaNvtoC8N5dA8pD2JA",5,reviews));
        sampleData.add(new Location("bai6umLcCNy9cXql0Js2RQ",4,reviews));

        //event to be posted to DB
        //final EventItem event=new EventItem(R.drawable.ic_baseline_event_24,"Night out with the BOYZZZZZZZZZZ","12/12/2020","Julian Campos",participantIds,"10:30PM","Bar", "Gettin' Rowdy");

        //reference to db entry where this will be saved
        //dbRef=FirebaseDbSingleton.getInstance().dbRef.child("Location");
        //save event
        FirebaseDbSingleton.getInstance().dbRef.child("Location").push().setValue(sampleData.get(0));
        FirebaseDbSingleton.getInstance().dbRef.child("Location").push().setValue(sampleData.get(1));
        //////////////////////////////////////inserting event data to DB for locations

        eventsRecycler = view.findViewById(R.id.events);
        eventsRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        eAdapter = new EventAdapter(events);
        eventsRecycler.setLayoutManager(layoutManager);
        eventsRecycler.setAdapter(eAdapter);
        eAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener()
        {
            //handles what happens when an item from the recyclerview is clicked
            @Override
            public void onItemClick(int position)
            {
                startActivity(new Intent(getContext(), EventPopUpActivity.class));
            }
        });

        // set on click listener for create event button to up create event page
        createEventButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CreateEventActivity createEvent = new CreateEventActivity();
                getParentFragmentManager().beginTransaction().replace(R.id.container_frag, createEvent).commit();
            }
        });

        return view;
    }
}
