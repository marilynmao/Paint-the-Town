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

import java.util.ArrayList;

public class FriendsActivity extends Fragment
{
    private RecyclerView FriendsRecycler;
    private EventAdapter eAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //the inflate() method takes the layout you wanna show as the first parameter
        View view=inflater.inflate(R.layout.frag_events, container, false);

        //dummy data to test the recyclerview
        ArrayList<FriendItem> sampleData=new ArrayList<>();
        sampleData.add(new FriendItem(R.drawable.ic_baseline_event_24,"Yurtus McFerguson","1234123123423"));
        sampleData.add(new FriendItem(R.drawable.ic_baseline_event_24,"John Doe","3435435345"));

        FriendsRecycler=view.findViewById(R.id.events);
        FriendsRecycler.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        eAdapter=new FriendsAdapter(sampleData);
        eventsRecycler.setLayoutManager(layoutManager);
        eventsRecycler.setAdapter(eAdapter);
        eAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener()
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

