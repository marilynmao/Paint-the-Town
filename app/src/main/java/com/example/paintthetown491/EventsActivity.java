package com.example.paintthetown491;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private ArrayList<Event> events;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //the inflate() method takes the layout you wanna show as the first parameter
        final View view = inflater.inflate(R.layout.frag_events, container, false);

        //holds the event IDs for each user
        eventIds = new ArrayList<>();
        //holds the events loaded from firebase
        events = new ArrayList<>();

        final Query queryVal = FirebaseDbSingleton.getInstance().dbRef.child("Event").orderByChild("eventDate");

        //listener for the values we want
        final ValueEventListener eventValListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                // clear events list...this prevents each event card from duplicating after updating
                events.clear();
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
                eAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }

            //used to convert a Iterable (type returned from firebase) to an arraylist
            public ArrayList<String> getCollectionFromIterable(Iterable<DataSnapshot> itr)
            {
                ArrayList<String> IterToArrayList = new ArrayList<>();
                for (DataSnapshot id : itr)
                {
                    IterToArrayList.add(id.getValue().toString());
                }
                return IterToArrayList;
            }

            //used to convert a Iterable (type returned from firebase) to an arraylist
            public ArrayList<String> getLocationCollectionFromIterable(Iterable<DataSnapshot> itr)
            {
                ArrayList<String> eLocations = new ArrayList<>();
                for (DataSnapshot id : itr)
                {
                    eLocations.add(id.getValue().toString());
                }
                return eLocations;
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
                    eAdapter.notifyDataSetChanged();

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

        //now that we have the event IDs saved, we need to look at the Event table for each of them
        eventRef = FirebaseDbSingleton.getInstance().dbRef.child("Event");

        //////////////////////////////////////inserting event data to DB for locations
        /*//dummy data to post to the DB (for posting to DB)
        ArrayList<String>participantIds=new ArrayList<>();
        participantIds.add("7iPPl1ZXgaTnyAtqWNfKgtUgBcb2");
        participantIds.add("KX1UfoLwTQOGTFHxyguqcl7i5YQ2");
        participantIds.add("7iPPl1ZXgaTnyAtqWNfKgtUgBcb2");*/

        //dummy data to test the recyclerview (for retrieving from DB)
        /*ArrayList<Location> sampleData=new ArrayList<>();
        ArrayList<String>reviews=new ArrayList<>();
        reviews.add("This place is the best. I love it!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        reviews.add("This place sucks. I hate it!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        reviews.add("This place is okay. I think we might be back soon to give it another shot!");


        sampleData.add(new Location("YqvoyaNvtoC8N5dA8pD2JA",5,reviews));
        sampleData.add(new Location("bai6umLcCNy9cXql0Js2RQ",4,reviews));*/

        //event to be posted to DB
        //final EventItem event=new EventItem(R.drawable.ic_baseline_event_24,"Night out with the BOYZZZZZZZZZZ","12/12/2020","Julian Campos",participantIds,"10:30PM","Bar", "Gettin' Rowdy");

        //reference to db entry where this will be saved
        //dbRef=FirebaseDbSingleton.getInstance().dbRef.child("Location");
        //save event
        /*FirebaseDbSingleton.getInstance().dbRef.child("Location").push().setValue(sampleData.get(0));
        FirebaseDbSingleton.getInstance().dbRef.child("Location").push().setValue(sampleData.get(1));*/
        //////////////////////////////////////inserting event data to DB for locations

        eventsRecycler = view.findViewById(R.id.events);
        eventsRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        eAdapter = new EventAdapter(events);
        eventsRecycler.setLayoutManager(layoutManager);
        eventsRecycler.setAdapter(eAdapter);

        return view;
    }
}
