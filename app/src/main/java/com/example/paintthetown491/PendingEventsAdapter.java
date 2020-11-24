package com.example.paintthetown491;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PendingEventsAdapter extends RecyclerView.Adapter<PendingEventsAdapter.PendingEventsViewHolder>
{
    //holds the events for the recyclerview
    private ArrayList<Event> mEventList;

    //variable that will make each item in the recyclerview clickable
    private PendingEventsAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
        //will remove pending invites
        void deleteInviteOnClick(int position);
        //will add users to events
        void acceptInviteOnClick(int position);
    }

    public void setOnItemClickListener(PendingEventsAdapter.OnItemClickListener listener)
    {
        mListener=listener;
    }

    //will bind the layout for each event and attach a listener
    @NonNull
    @Override
    public PendingEventsAdapter.PendingEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_invite_item,parent,false);
        PendingEventsAdapter.PendingEventsViewHolder vh=new PendingEventsAdapter.PendingEventsViewHolder(v, mListener);
        return vh;
    }

    //Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(@NonNull PendingEventsAdapter.PendingEventsViewHolder holder, int position)
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
    public PendingEventsAdapter(ArrayList<Event> eventList)
    {
        mEventList=eventList;
    }

    //A ViewHolder describes an item view and metadata about its place within the RecyclerView
    public static class PendingEventsViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView eName,eDate,eCreator;
        public RelativeLayout relativeLayout;
        public ImageView declineInvite;
        public ImageView acceptInvite;

        public PendingEventsViewHolder(@NonNull final View itemView, final PendingEventsAdapter.OnItemClickListener listener)
        {
            super(itemView);
            mImageView= itemView.findViewById(R.id.eventView);
            eName=itemView.findViewById(R.id.eventName);
            eDate=itemView.findViewById(R.id.eventDate);
            eCreator=itemView.findViewById(R.id.eventCreator);
            relativeLayout=itemView.findViewById(R.id.relativelayout);
            acceptInvite = itemView.findViewById(R.id.accept_invite);
            declineInvite = itemView.findViewById(R.id.delete_invite);

            acceptInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.acceptInviteOnClick(position);
                        }
                    }
                }
            });

            declineInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.deleteInviteOnClick(position);
                        }
                    }
                }
            });
        }
    }
}
