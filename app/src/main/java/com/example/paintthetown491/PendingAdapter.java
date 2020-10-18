package com.example.paintthetown491;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.PendingRequestViewHolder>
{
    private ArrayList<User> pendingList;

    //variable that will make each item in the recyclerview clickable
    private PendingAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
        void deleteOnClick(int position);
        void acceptOnClick(int position);
    }

    public void setOnItemClickListener(PendingAdapter.OnItemClickListener listener)
    {
        mListener=listener;
    }

    @NonNull
    @Override
    public PendingAdapter.PendingRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_friend_item, parent, false);
        PendingAdapter.PendingRequestViewHolder ps_vh = new PendingAdapter.PendingRequestViewHolder(view, mListener);
        return ps_vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PendingAdapter.PendingRequestViewHolder holder, int position)
    {
        User curr_prof = pendingList.get(position);

        holder.profile_fn.setText(curr_prof.getFirstName());
        holder.profile_ln.setText(curr_prof.getLastName());
    }

    @Override
    public int getItemCount()
    {
        return pendingList.size();
    }

    public PendingAdapter(ArrayList<User> profiles) { pendingList = profiles; }

    public static class PendingRequestViewHolder extends RecyclerView.ViewHolder
    {
        public TextView profile_fn;
        public TextView profile_ln;
        public ImageView decline;
        public ImageView accept;

        public PendingRequestViewHolder(@NonNull View itemView, final PendingAdapter.OnItemClickListener listener)
        {
            super(itemView);
            //binding xml elements to members of the class
            profile_fn = itemView.findViewById(R.id.pending_friend_firstName);
            profile_ln = itemView.findViewById(R.id.pending_friend_lastName);
            accept=itemView.findViewById(R.id.accept_request);
            decline=itemView.findViewById(R.id.delete_request);

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

            //handles accepting friend requests in the pending requests activity
            accept.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(listener !=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.acceptOnClick(position);
                        }
                    }
                }
            });

            //handles deleting friend requests in the pending requests activity
            decline.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(listener !=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.deleteOnClick(position);
                        }
                    }
                }
            });
        }
    }
}

