package com.example.paintthetown491;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationReviewAdapter extends RecyclerView.Adapter<LocationReviewAdapter.LocationReviewViewHolder>
{
    private ArrayList<LocationReview> reviews;

    //variable that will make each item in the recyclerview clickable
    private LocationReviewAdapter.OnItemClickListener mListener;

    //if your recyclerview items have multiple functions, you add functionality for each in this interface
    public interface OnItemClickListener
    {
        //will expand review info
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_review_item, parent, false);
        LocationReviewAdapter.LocationReviewViewHolder ps_vh = new LocationReviewAdapter.LocationReviewViewHolder(view, mListener);
        return ps_vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationReviewAdapter.LocationReviewViewHolder holder, int position)
    {
        LocationReview curr_rev = reviews.get(position);

        //sets holder fields with review attributes
        holder.review_date.setText(curr_rev.getDate());
        holder.review.setText(curr_rev.getReview());
        holder.rating.setRating(curr_rev.getRating());

    }

    @Override
    public int getItemCount()
    {
        return reviews.size();
    }

    public LocationReviewAdapter(ArrayList<LocationReview> profiles) { reviews = profiles; }

    public static class LocationReviewViewHolder extends RecyclerView.ViewHolder
    {
        public TextView review;
        public TextView review_date;
        private RatingBar rating;

        public LocationReviewViewHolder(@NonNull View itemView, final LocationReviewAdapter.OnItemClickListener listener)
        {
            super(itemView);
            //binding xml elements to members of the class
            review = itemView.findViewById(R.id.review);
            review_date = itemView.findViewById(R.id.review_date);
            rating=itemView.findViewById(R.id.rating);

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

