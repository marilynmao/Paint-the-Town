package com.example.paintthetown491;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

//class that holds elements needed for our events fragment
public class EventsActivity extends Fragment
{
    private RecyclerView eventsRecycler;
    private EventAdapter eAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //the inflate() method takes the layout you wanna show as the first parameter
        final View view=inflater.inflate(R.layout.frag_events, container, false);
        //dummy data to test the recyclerview
        ArrayList<EventItem> sampleData=new ArrayList<>();
        sampleData.add(new EventItem(R.drawable.ic_baseline_event_24,"Night out with the BOYZZZ","starting on: "+"12/12/2020", "created by: "+"Julian Campos"));
        sampleData.add(new EventItem(R.drawable.ic_baseline_event_24,"Night out with the GIRLZZZ","starting on: "+"12/22/2020","created by:"+"Julian Campos"));
        
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
                view.getContext().startActivity(new Intent(view.getContext(),EventPopUpActivity.class));
                System.out.println("CLICKED after!");
            }
        });

        return view;
    }

}
