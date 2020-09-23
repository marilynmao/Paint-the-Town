package com.example.paintthetown491;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class EventsActivity extends Fragment
{
    private RecyclerView eventsRecycler;
    private RecyclerView.Adapter eAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.frag_events, container, false);
        ArrayList<EventItem> sampleData=new ArrayList<>();
        sampleData.add(new EventItem(R.drawable.backgroundimg,"text1","text3"));
        sampleData.add(new EventItem(R.drawable.backgroundimg,"text1","text2"));
        
        eventsRecycler=view.findViewById(R.id.events);
        eventsRecycler.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        eAdapter=new EventAdapter(sampleData);
        eventsRecycler.setLayoutManager(layoutManager);
        eventsRecycler.setAdapter(eAdapter);
        return view;
    }

}