package com.example.paintthetown491;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
{
    private ArrayList<EventItem> mEventList;

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_event,parent,false);
        EventViewHolder vh=new EventViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position)
    {
        EventItem curr=mEventList.get(position);

        holder.mImageView.setImageResource(curr.getImage());
        holder.text1.setText(curr.getText1().toString());
        holder.text2.setText(curr.getText2().toString());
    }

    @Override
    public int getItemCount()
    {
        return mEventList.size();
    }

    public EventAdapter(ArrayList<EventItem> eventList)
    {
        mEventList=eventList;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView text1,text2;


        public EventViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mImageView= itemView.findViewById(R.id.eventView);
            text1=itemView.findViewById(R.id.text1);
            text2=itemView.findViewById(R.id.text2);
        }
    }
}
