package com.example.paintthetown491;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

//class that holds elements needed for our events fragment
public class EventsActivity extends Fragment
{
    private RecyclerView eventsRecycler;
    private EventAdapter eAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //the inflate() method takes the layout you wanna show as the first parameter
        final View view=inflater.inflate(R.layout.frag_events, container, false);

        //////////////////////////////////////inserting event data to DB for events
        
        //dummy data to post to the DB (for posting to DB)
        ArrayList<String>participantIds=new ArrayList<>();
        participantIds.add("7iPPl1ZXgaTnyAtqWNfKgtUgBcb2");
        participantIds.add("KX1UfoLwTQOGTFHxyguqcl7i5YQ2");
        participantIds.add("7iPPl1ZXgaTnyAtqWNfKgtUgBcb2");

        //dummy data to test the recyclerview (for retrieving from DB)
        ArrayList<EventItem> sampleData=new ArrayList<>();
        sampleData.add(new EventItem(R.drawable.ic_baseline_event_24,"Night out with the BOYZZZ","12/12/2020", "Julian Campos",participantIds));
        sampleData.add(new EventItem(R.drawable.ic_baseline_event_24,"Night out with the GIRLZZZ","12/22/2020","Julian Campos",participantIds));

        //event to be posted to DB
        EventItem event=new EventItem(R.drawable.ic_baseline_event_24,"Night out with the BOYZZZZZZZZZZ","12/12/2020","Julian Campos",participantIds);

        //reference to db entry where this will be saved
        dbRef=FirebaseDbSingleton.getInstance().dbRef.child("Event");
        //save event
        dbRef.push().setValue(event);

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

        return view;
    }

}
