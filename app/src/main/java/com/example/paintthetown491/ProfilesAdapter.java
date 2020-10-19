package com.example.paintthetown491;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ProfileSearchViewHolder>
{
    private ArrayList <User> profileSearchList;

    //variable that will make each item in the recyclerview clickable
    private ProfilesAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ProfilesAdapter.OnItemClickListener listener)
    {
        mListener=listener;
    }

    @NonNull
    @Override
    public ProfilesAdapter.ProfileSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_search_results, parent, false);
        ProfileSearchViewHolder ps_vh = new ProfileSearchViewHolder(view, mListener);
        return ps_vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfilesAdapter.ProfileSearchViewHolder holder, int position)
    {
        User curr_prof = profileSearchList.get(position);

        holder.profile_fn.setText(curr_prof.getFirstName());
        holder.profile_ln.setText(curr_prof.getLastName());
        holder.profile_ph.setText(curr_prof.getPhoneNumber());
        String icon = curr_prof.getIcon();
        if (icon != "none") {
            StorageReference path = FirebaseStorage.getInstance().getReference().child("Icons").child(curr_prof.getIcon());
            path.getBytes(5 * 1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.profile_pp.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Failed to set profile pic");
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return profileSearchList.size();
    }

    public ProfilesAdapter(ArrayList<User> profiles) { profileSearchList = profiles; }

    public static class ProfileSearchViewHolder extends RecyclerView.ViewHolder
    {
        public TextView profile_fn;
        public TextView profile_ln;
        public TextView profile_ph;
        public ImageView profile_pp;

        public ProfileSearchViewHolder(@NonNull View itemView, final ProfilesAdapter.OnItemClickListener listener)
        {
            super(itemView);
            profile_fn = itemView.findViewById(R.id.u_firstName);
            profile_ln = itemView.findViewById(R.id.u_lastName);
            profile_ph = itemView.findViewById(R.id.u_phone);
            profile_pp = itemView.findViewById(R.id.avatarView);
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
