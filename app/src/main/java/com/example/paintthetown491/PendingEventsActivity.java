package com.example.paintthetown491;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PendingEventsActivity extends Fragment {
    private RecyclerView pendingEventsRecycler;
    private TextView noPendingEvents;
    private PendingEventsAdapter pendingEventsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference eventRef;
    private ArrayList<String> eventIds;
    private ArrayList<Event> events;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.frag_pending_events, container, false);

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
                pendingEventsAdapter.notifyDataSetChanged();
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
        ValueEventListener pendingEventIdValListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                eventIds.clear();
                //check to see if firebase returned anything
                if (snapshot.exists())
                {
                    //hides the "no pending invites" message and shows the recycler
                    pendingEventsRecycler.setVisibility(View.VISIBLE);
                    noPendingEvents.setVisibility(View.INVISIBLE);
                    //iterate through each child returned
                    for (DataSnapshot e : snapshot.getChildren())
                    {
                        //holds the event ID as a string
                        String event = e.getValue(String.class);
                        //adds it to the list
                        eventIds.add(event);
                    }

                    //notifies the adapter of any changes
                    pendingEventsAdapter.notifyDataSetChanged();

                    for (String id : eventIds)
                    {
                        queryVal.equalTo(id);
                        queryVal.addValueEventListener(eventValListener);
                    }
                }

                //no pending requests, show message and hide recycler
                else
                {
                    pendingEventsRecycler.setVisibility(View.INVISIBLE);
                    noPendingEvents.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        };

        //querying the specific user's list of events
        Query queryID = FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("pendingEInvites");
        //attaching the value listener
        queryID.addValueEventListener(pendingEventIdValListener);

        eventRef = FirebaseDbSingleton.getInstance().dbRef.child("Event");

        //binding xml elements to class members
        pendingEventsRecycler=view.findViewById(R.id.pending_invites);
        pendingEventsRecycler.setHasFixedSize(true);
        pendingEventsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        noPendingEvents = view.findViewById(R.id.no_pending_invites);

        //using our adapter to display the necessary info
        pendingEventsAdapter= new PendingEventsAdapter(events);
        pendingEventsRecycler.setAdapter(pendingEventsAdapter);

        // listener for event selected
        pendingEventsAdapter.setOnItemClickListener(new PendingEventsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void deleteInviteOnClick(int position) {
                // TODO: delete pending event invite
                }

            @Override
            public void acceptInviteOnClick(int position) {
                // TODO: accept pending event invite
            }
        });

        return view;
    }
}