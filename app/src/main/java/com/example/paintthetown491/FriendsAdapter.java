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
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>
{
    private ArrayList<FriendItem> mFriendList;
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
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_event,parent,false);
        FriendViewHolder vh=new FriendViewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position)
    {
        FriendItem curr= mFriendList.get(position);

        holder.mImageView.setImageResource(curr.getImage());
        holder.eName.setText(curr.getFriendName());
        holder.eDate.setText(curr.getUserID());
    }

    //returns the size of the list
    @Override
    public int getItemCount()
    {
        return mFriendList.size();
    }

    //constructor
    public FriendsAdapter(ArrayList<FriendItem> friendList)
    {
        mFriendList =friendList;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView eName,eDate,eCreator;

        public FriendViewHolder(@NonNull View itemView, final OnItemClickListener listener)
        {
            super(itemView);
            mImageView= itemView.findViewById(R.id.eventView);

            //TODO commented code is causing errors
            //eName=itemView.findViewById(R.id.Name);
            //eDate=itemView.findViewById(R.id.user);

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