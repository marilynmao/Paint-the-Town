package com.example.paintthetown491;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsListViewHolder> {
    private ArrayList <FriendsListItem> FriendsList;

    //variable that will make each item in the recyclerview clickable
    private EventAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(EventAdapter.OnItemClickListener listener)
    {
        mListener=listener;
    }



    @NonNull
    @Override
    public FriendsListAdapter.FriendsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_search_results, parent, false);
        FriendsListViewHolder fl_vh = new FriendsListViewHolder(view, mListener);
        return fl_vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsListAdapter.FriendsListViewHolder holder, int position) {
        FriendsListItem curr_prof = FriendsList.get(position);

        holder.profile_fn.setText(curr_prof.getUsr_fn());
        holder.profile_ln.setText(curr_prof.getUsr_ln());
        holder.profile_ph.setText(curr_prof.getUsr_ph());

    }

    @Override
    public int getItemCount() {
        return FriendsList.size();
    }

    public FriendsListAdapter(ArrayList<FriendsListItem> profiles) { FriendsList = profiles; }

    public static class FriendsListViewHolder extends RecyclerView.ViewHolder {
        public TextView profile_fn;
        public TextView profile_ln;
        public TextView profile_ph;

        public FriendsListViewHolder(@NonNull View itemView, final EventAdapter.OnItemClickListener listener) {
            super(itemView);
            profile_fn = itemView.findViewById(R.id.u_firstName);
            profile_ln = itemView.findViewById(R.id.u_lastName);
            profile_ph = itemView.findViewById(R.id.u_phone);
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
