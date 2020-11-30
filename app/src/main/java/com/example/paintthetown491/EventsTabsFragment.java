package com.example.paintthetown491;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class EventsTabsFragment extends Fragment {
    private TabLayout events_tabs;
    private ViewPager events_pager;
    private static int int_items = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //the inflate() method takes the layout you wanna show as the first parameter
        View view = inflater.inflate(R.layout.event_tabs, container, false);

        //tabs for our fragment
        events_tabs = view.findViewById(R.id.events_tabs);
        events_pager = view.findViewById(R.id.events_pager);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(events_pager);
        events_tabs.setupWithViewPager(events_pager);
        events_tabs.getTabAt(0).select();

        // Open the correct tab based on which screen we came from
        int tab = getActivity().getIntent().getIntExtra("tab",8);
        // Users
        if(tab == 1) {
            events_tabs.getTabAt(1).select();
        }
        // Services
        else if(tab == 2){
            events_tabs.getTabAt(2).select();
        }

        events_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    //pager will call the hometab adapter in order to switch between tab based on which was selected
    private void setUpViewPager(ViewPager viewPager) {
        //creating a new tab adapter object with a child fragment manager for the 3 tabs that are fragment objects
        EventTabsAdapter adapter = new EventTabsAdapter(getChildFragmentManager());
        //fragments added to the adapter to swap between
        adapter.addFragment(new EventsActivity(), "Events");
        adapter.addFragment(new PendingEventsActivity(), "Pending Event Invites");
        //set the adapter to the view pager
        viewPager.setAdapter(adapter);
    }
}
