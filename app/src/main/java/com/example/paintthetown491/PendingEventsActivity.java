package com.example.paintthetown491;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.HashMap;


public class PendingEventsActivity extends Fragment {
    private RecyclerView pendingEventsRecycler;
    private TextView noPendingEvents;
    private PendingEventsAdapter pendingEventsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference eventRef;
    private HashMap<String, String> eventIds;
    private ArrayList<Event> events;

    // deletes event ID from pendingEInvites
    public void deleteEventID(String eventID, Integer position)
    {
        //logged-in user ID
        String mainID=FirebaseDbSingleton.getInstance().user.getUid();
        //pending event ID to delete
        String key=eventIds.get(eventID);
        //deletes the pending event ID from the DB
        FirebaseDbSingleton.getInstance().dbRef.child("User").child(mainID).child("pendingEInvites").child(key).removeValue();
        //deletes user id from pendingInvites in events table
        FirebaseDbSingleton.getInstance().dbRef.child("Event").child("-"+ eventID).child("pendingInvites").child(mainID).removeValue();
        events.remove(position);
        pendingEventsAdapter.notifyItemRemoved(position);
        pendingEventsAdapter.notifyItemRangeChanged(position,eventIds.size());
    }

    //confirmation to delete pending event invite
    public void confirmDeleteInvite(final int position) {
        //confirmation dialog before deleting a request
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm")
                .setMessage("Are you sure?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //remove it from the pending list in firebase
                        deleteEventID(events.get(position).getEventId(),position);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    //confirmation to accept a pending event invite
    public void confirmAcceptInvite(final int position) {
        //confirmation dialog before deleting a request
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm")
                .setMessage("Are you sure?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //add it from the pending list in firebase
                        addEventID(events.get(position).getEventId(),position);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    // deletes event ID from pendingEInvites
    public void addEventID(String eventID, Integer position)
    {
        //logged-in user ID
        String mainID=FirebaseDbSingleton.getInstance().user.getUid();
        //pending event ID to delete
        String key=eventIds.get(eventID);
        //appends "-" at the beginning of the event ID (in order to match event ID in FB)
        String eventkey="-"+key;
        //deletes the pending event ID from the DB
        FirebaseDbSingleton.getInstance().dbRef.child("User").child(mainID).child("pendingEInvites").child(key).removeValue();
        //adds the event ID to the user's list of events in FB
        FirebaseDbSingleton.getInstance().dbRef.child("User").child(mainID).child("events").child(eventkey).setValue(key);
        //removes the user ID from the pending invite list in the event schema
        FirebaseDbSingleton.getInstance().dbRef.child("Event").child(eventkey).child("pendingInvites").child(mainID).removeValue();
        //adds the user ID to the participant list in the event schema
        FirebaseDbSingleton.getInstance().dbRef.child("Event").child(eventkey).child("participantList").child(mainID).setValue(mainID);
        events.remove(position);
        pendingEventsAdapter.notifyItemRemoved(position);
        pendingEventsAdapter.notifyItemRangeChanged(position,eventIds.size());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.frag_pending_events, container, false);

        //holds the event IDs for each user
        //eventIds = new ArrayList<String>();
        eventIds = new HashMap<>();
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

                        //sees if the hashmap contains the event ID
                        if (eventIds.containsValue(s))
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
                        // place key (eventID)  & value into hashmap
                        eventIds.put(e.getValue(String.class), e.getKey());
                    }

                    //notifies the adapter of any changes
                    pendingEventsAdapter.notifyDataSetChanged();

                    for (String id : eventIds.keySet())
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
            public void deleteInviteOnClick(int position) { confirmDeleteInvite(position);}

            @Override
            public void acceptInviteOnClick(int position) {confirmAcceptInvite(position);}
        });

        return view;
    }
}