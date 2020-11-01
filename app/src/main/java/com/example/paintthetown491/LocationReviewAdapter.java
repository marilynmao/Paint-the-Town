package com.example.paintthetown491;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationReviewAdapter extends RecyclerView.Adapter<LocationReviewAdapter.LocationReviewViewHolder>
{
    private ArrayList<Location> searchList;

    //variable that will make each item in the recyclerview clickable
    private LocationReviewAdapter.OnItemClickListener mListener;

    //if your recyclerview items have multiple functions, you add functionality for each in this interface
    public interface OnItemClickListener
    {
        //will open locations from the search results
        void onItemClick(int position);
    }

    public void setOnItemClickListener(LocationReviewAdapter.OnItemClickListener listener)
    {
        mListener=listener;
    }

    @NonNull
    @Override
    public LocationReviewAdapter.LocationReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        LocationReviewAdapter.LocationReviewViewHolder ps_vh = new LocationReviewAdapter.LocationReviewViewHolder(view, mListener);
        return ps_vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationReviewAdapter.LocationReviewViewHolder holder, int position)
    {
        Location curr_loc = searchList.get(position);

        //sets holder fields with location attributes
        holder.location_name.setText(curr_loc.getLocationName());
        holder.location_info.setText(curr_loc.getLocationInfo());
        //fills the imageview with the location picture using the image URl provided by yelp
        new DownloadImage(holder.location_pic).execute(curr_loc.getImageUrl());
    }

    @Override
    public int getItemCount()
    {
        return searchList.size();
    }

    public LocationReviewAdapter(ArrayList<Location> profiles) { searchList = profiles; }

    public static class LocationReviewViewHolder extends RecyclerView.ViewHolder
    {
        public TextView location_name;
        public TextView location_info;
        public ImageView location_pic;

        public LocationReviewViewHolder(@NonNull View itemView, final LocationReviewAdapter.OnItemClickListener listener)
        {
            super(itemView);
            //binding xml elements to members of the class
            location_name = itemView.findViewById(R.id.location_name);
            location_info = itemView.findViewById(R.id.location_location_info);
            location_pic=itemView.findViewById(R.id.locationView);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(listener !=null)
                    {
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}

