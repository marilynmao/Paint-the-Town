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

//class that holds elements needed for our events fragment
public class FriendsActivity extends Fragment
{
    private RecyclerView FriendsRecycler;
    private FriendsListAdapter FlAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //the inflate() method takes the layout you wanna show as the first parameter
        View view=inflater.inflate(R.layout.frag_events, container, false);

        //dummy data to test the recyclerview
        ArrayList<FriendsListItem> sampleData=new ArrayList<>();
        sampleData.add(new FriendsListItem(R.drawable.ic_baseline_event_24,"Night out with the BOYZZZ","starting on: "+"12/12/2020", "created by: "+"Julian Campos"));
        sampleData.add(new FriendsListItem(R.drawable.ic_baseline_event_24,"Night out with the GIRLZZZ","starting on: "+"12/22/2020","created by:"+"Julian Campos"));

        FriendsRecycler=view.findViewById(R.id.events);
        FriendsRecycler.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        FlAdapter =new FriendsListAdapter(sampleData);
        FriendsRecycler.setLayoutManager(layoutManager);
        FriendsRecycler.setAdapter(FlAdapter);
        FlAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener()
        {
            //handles what happens when an item from the recyclerview is clicked
            @Override
            public void onItemClick(int position)
            {
                //TODO:open up the event page
                System.out.println("CLICKED!");
            }
        });

        return view;
    }

}