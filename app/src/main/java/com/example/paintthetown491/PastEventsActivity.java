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

public class PastEventsActivity extends Fragment {
    private RecyclerView eventsRecycler;
    private EventAdapter eAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference userEventRef, eventRef;
    private ArrayList<String> eventIds;
    private ArrayList<Event> pastEvents;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_past_events, container, false);



        //holds the event IDs for each user
        eventIds = new ArrayList<>();
        //holds the events loaded from firebase
        pastEvents = new ArrayList<>();

        //Dummy data
        //ArrayList<String> test = new ArrayList<String>();
        //test.add("MJZjsenCnooeKkunNja");

       // FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("pastEvents").setValue(test);

        loadPastEvents();



        eventsRecycler=view.findViewById(R.id.events);
        eventsRecycler.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        eAdapter=new EventAdapter(pastEvents);
        eventsRecycler.setLayoutManager(layoutManager);
        eventsRecycler.setAdapter(eAdapter);


        return view;
    }

    private void loadPastEvents(){
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
        Query query = FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("pastEvents");
        //attaching the value listener
        query.addValueEventListener(eventIdValListener);

        eventRef = FirebaseDbSingleton.getInstance().dbRef.child("Event");

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //iterate through each child returned
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        String s=ds.getKey();

                        if(s.charAt(0)=='-')
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
                            pastEvents.add(e);
                        }
                    }

                }
                //notifies the adapter of any changes
                eAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            //used to convert a Iterable (type returned from firebase) to an arraylist
            public ArrayList<String> getCollectionFromIterable(Iterable<DataSnapshot> itr)
            {
                ArrayList<String> participants = new ArrayList<>();
                for (DataSnapshot id : itr)
                {
                    participants.add(id.toString());
                }
                return participants;
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
        });

    }
}
