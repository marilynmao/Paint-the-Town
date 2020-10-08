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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//class that holds elements needed for our events fragment
public class EventsActivity extends Fragment
{
    private RecyclerView eventsRecycler;
    private EventAdapter eAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference dbRef;
    private ArrayList<String>events;
    private Button createEventButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //the inflate() method takes the layout you wanna show as the first parameter
        final View view=inflater.inflate(R.layout.frag_events, container, false);
        // add create event button at bottom of events recyclerview page
        createEventButton = view.findViewById(R.id.createNewEventBtn);

        //////////////////////////////////////inserting event data to DB for events
        
        //dummy data to post to the DB (for posting to DB)
        ArrayList<String>participantIds=new ArrayList<>();
        participantIds.add("7iPPl1ZXgaTnyAtqWNfKgtUgBcb2");
        participantIds.add("KX1UfoLwTQOGTFHxyguqcl7i5YQ2");
        participantIds.add("7iPPl1ZXgaTnyAtqWNfKgtUgBcb2");

        //dummy data to test the recyclerview (for retrieving from DB)
        ArrayList<EventItem> sampleData=new ArrayList<>();
        sampleData.add(new EventItem(R.drawable.ic_baseline_event_24,"Night out with the BOYZZZ","12/12/2020", "Julian Campos",participantIds, "10:30PM","Bar", "Gettin' Rowdy"));
        sampleData.add(new EventItem(R.drawable.ic_baseline_event_24,"Night out with the GIRLZZZ","12/22/2020","Julian Campos",participantIds, "10:30PM","Bar", "Gettin' Rowdy"));

        //event to be posted to DB
        final EventItem event=new EventItem(R.drawable.ic_baseline_event_24,"Night out with the BOYZZZZZZZZZZ","12/12/2020","Julian Campos",participantIds,"10:30PM","Bar", "Gettin' Rowdy");

        //reference to db entry where this will be saved
        dbRef=FirebaseDbSingleton.getInstance().dbRef.child("Event");
        //save event
        dbRef.push().setValue(event);

        //////////////////////////////////////

        //////////////////////////////////////retrieving event data to DB for events

        //dbRef=FirebaseDbSingleton.getInstance().dbRef

        //////////////////////////////////////

        eventsRecycler=view.findViewById(R.id.events);
        eventsRecycler.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        eAdapter=new EventAdapter(sampleData);
        eventsRecycler.setLayoutManager(layoutManager);
        eventsRecycler.setAdapter(eAdapter);
        eAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener()
        {
            //handles what happens when an item from the recyclerview is clicked
            @Override
            public void onItemClick(int position)
            {
                System.out.println("CLICKED before!");
                startActivity(new Intent(getContext(),EventPopUpActivity.class));
                System.out.println("CLICKED after!");
            }
        });

        // set on click listener for create event button to up create event page
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEventActivity createEvent = new CreateEventActivity();
                getParentFragmentManager().beginTransaction().replace(R.id.container_frag, createEvent).commit();
            }
        });
        return view;
    }

}
