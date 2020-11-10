package com.example.paintthetown491;

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

public class AddLocationAdapter extends RecyclerView.Adapter<AddLocationAdapter.AddLocationViewHolder> {
    //holds the events for the recyclerview
    private ArrayList<Event> mEventList;

    //variable that will make each item in the recyclerview clickable
    private AddLocationAdapter.OnItemClickListener mListener;

    public static int selectedEvent;

    public interface OnItemClickListener
    {
        // get selected location
        void getEventOnclick(int position);
    }

    public void setOnItemClickListener(AddLocationAdapter.OnItemClickListener listener)
    {
        mListener=listener;
    }

    public void clearSelection() {
        selectedEvent = -1;
    }

    //will bind the layout for each event and attach a listener
    @NonNull
    @Override
    public AddLocationAdapter.AddLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_event,parent,false);
        AddLocationAdapter.AddLocationViewHolder vh=new AddLocationAdapter.AddLocationViewHolder(v, mListener);
        return vh;
    }

    //Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(@NonNull AddLocationAdapter.AddLocationViewHolder holder, int position)
    {
        final Event curr=mEventList.get(position);

        holder.mImageView.setImageResource(curr.getImage());
        holder.eName.setText(curr.getEventName());
        holder.eDate.setText(curr.getEventDate());
        holder.eCreator.setText(curr.getEventCreator());
        // un-checks previous selections so that only one radio button is allowed to be selected
        holder.radioBtn.setChecked(position == selectedEvent);
    }

    //returns the size of the list
    @Override
    public int getItemCount()
    {
        return mEventList.size();
    }

    //constructor
    public AddLocationAdapter(ArrayList<Event> eventList)
    {
        mEventList=eventList;
    }

    //A ViewHolder describes an item view and metadata about its place within the RecyclerView
    public class AddLocationViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView eName,eDate,eCreator;
        public RelativeLayout relativeLayout;
        public RadioButton radioBtn;

        public AddLocationViewHolder(@NonNull final View itemView, final AddLocationAdapter.OnItemClickListener listener)
        {
            super(itemView);
            mImageView= itemView.findViewById(R.id.eventView);
            eName=itemView.findViewById(R.id.eventName);
            eDate=itemView.findViewById(R.id.eventDate);
            eCreator=itemView.findViewById(R.id.eventCreator);
            relativeLayout=itemView.findViewById(R.id.relativelayout);
            radioBtn=itemView.findViewById(R.id.radioButton);
            radioBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedEvent = getAdapterPosition();
                    notifyDataSetChanged();
                    if(listener !=null)
                    {
                        if(selectedEvent!=RecyclerView.NO_POSITION)
                        {
                            listener.getEventOnclick(selectedEvent);
                        }
                    }
                }
            });
        }
    }
}
