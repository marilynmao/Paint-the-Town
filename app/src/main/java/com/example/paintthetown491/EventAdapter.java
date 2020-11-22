package com.example.paintthetown491;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//the class is needed to feed all our data to the recyclerview
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
{
    //holds the events for the recyclerview
    private ArrayList<Event> mEventList;

    //variable that will make each item in the recyclerview clickable
    private OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    //will bind the layout for each event and attach a listener
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_event,parent,false);
        EventViewHolder vh=new EventViewHolder(v, mListener);
        return vh;
    }

    //Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position)
    {
        final Event curr = mEventList.get(position);

        holder.mImageView.setImageResource(curr.getImage());
        holder.eName.setText(curr.getEventName());
        holder.eDate.setText(curr.getEventDate());
        holder.eCreator.setText(curr.getEventCreator());
        // set the onclick listener for the current item here so that the data can easily be sent to the popup activity for display
        holder.relativeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), EventPopUpActivity.class);
                intent.putExtra("eid", curr.getEventId());
                intent.putExtra("ename", curr.getEventName());
                intent.putExtra("einfo", curr.getEventInfo());
                intent.putExtra("edate", curr.getEventDate());
                intent.putExtra("etime", curr.getEventTime());
                intent.putStringArrayListExtra("elocation", curr.getEventLocation());
                intent.putStringArrayListExtra("ePeople",curr.getParticipantList());
                v.getContext().startActivity(intent);
            }
        });
    }

    //returns the size of the list
    @Override
    public int getItemCount()
    {
        return mEventList.size();
    }

    //constructor
    public EventAdapter(ArrayList<Event> eventList)
    {
        mEventList=eventList;
    }

    //A ViewHolder describes an item view and metadata about its place within the RecyclerView
    public static class EventViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView eName,eDate,eCreator;
        public RelativeLayout relativeLayout;
        public RadioButton radioBtn;

        public EventViewHolder(@NonNull final View itemView, final OnItemClickListener listener)
        {
            super(itemView);
            mImageView= itemView.findViewById(R.id.eventView);
            eName=itemView.findViewById(R.id.eventName);
            eDate=itemView.findViewById(R.id.eventDate);
            eCreator=itemView.findViewById(R.id.eventCreator);
            relativeLayout=itemView.findViewById(R.id.relativelayout);
            radioBtn=itemView.findViewById(R.id.radioButton);
            radioBtn.setVisibility(View.INVISIBLE);
        }
    }
}
