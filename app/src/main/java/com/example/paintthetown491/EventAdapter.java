package com.example.paintthetown491;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//the class is needed to feed all our data to the recyclerview
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
{
    private ArrayList<EventItem> mEventList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener=listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_event,parent,false);
        EventViewHolder vh=new EventViewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position)
    {
        EventItem curr=mEventList.get(position);

        holder.mImageView.setImageResource(curr.getImage());
        holder.eName.setText(curr.getEventName().toString());
        holder.eDate.setText(curr.getEventDate().toString());
        holder.eCreator.setText(curr.getEventCreator().toString());
    }

    //returns the size of the list
    @Override
    public int getItemCount()
    {
        return mEventList.size();
    }

    //constructor
    public EventAdapter(ArrayList<EventItem> eventList)
    {
        mEventList=eventList;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView eName,eDate,eCreator;

        public EventViewHolder(@NonNull View itemView, final OnItemClickListener listener)
        {
            super(itemView);
            mImageView= itemView.findViewById(R.id.eventView);
            eName=itemView.findViewById(R.id.eventName);
            eDate=itemView.findViewById(R.id.eventDate);
            eCreator=itemView.findViewById(R.id.eventCreator);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(listener !=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
