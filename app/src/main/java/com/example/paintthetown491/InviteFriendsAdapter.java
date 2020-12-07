package com.example.paintthetown491;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class InviteFriendsAdapter extends RecyclerView.Adapter<InviteFriendsAdapter.InviteFriendsViewHolder>
{
    private ArrayList<User> profileSearchList;

    //variable that will make each item in the recyclerview clickable
    private InviteFriendsAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
        void onCheck(int position);
        void onUnCheck(int position);
    }

    public void setOnItemClickListener(InviteFriendsAdapter.OnItemClickListener listener)
    {
        mListener=listener;
    }

    @NonNull
    @Override
    public InviteFriendsAdapter.InviteFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_search_results, parent, false);
        InviteFriendsAdapter.InviteFriendsViewHolder ps_vh = new InviteFriendsAdapter.InviteFriendsViewHolder(view, mListener);
        return ps_vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final InviteFriendsAdapter.InviteFriendsViewHolder holder, int position) {
        final User curr_prof = profileSearchList.get(position);

        final ArrayList<String> friends = new ArrayList<>();
        ValueEventListener friendChecker = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friends.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot e : snapshot.getChildren()) {
                        String fID = e.getValue(String.class);
                        friends.add(fID);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        Query query = FirebaseDbSingleton.getInstance().dbRef.child("User").child(FirebaseDbSingleton.getInstance().user.getUid()).child("friends");

        query.addValueEventListener(friendChecker);

        holder.profile_fn.setText(curr_prof.getFirstName());
        holder.profile_ln.setText(curr_prof.getLastName());
        holder.profile_ph.setText(curr_prof.getPhoneNumber());

        String icon = curr_prof.getIcon();
        if (!icon.equals("none")) {
            StorageReference path = FirebaseStorage.getInstance().getReference().child("Icons").child(curr_prof.getIcon());
            path.getBytes(5 * 1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.profile_image.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Failed to set profile pic");
                }
            });
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                //check if the user is friends with the card that was clicked on.
                boolean isFriend = false;
                for (String s : friends) {
                    if (s.equals(curr_prof.getId())) {
                        isFriend = true;
                    }
                }

                if (isFriend){
                    intent = new Intent(v.getContext(), FriendPopUpActivity.class);
                }
                else{
                    intent = new Intent(v.getContext(), ProfileViewActivity.class);
                }
                intent.putExtra("firstname", curr_prof.getFirstName());
                intent.putExtra("lastname", curr_prof.getLastName());
                intent.putExtra("id", curr_prof.getId());
                intent.putExtra("image", curr_prof.getIcon());
                intent.putExtra("username", curr_prof.getUsername());
                intent.putExtra("friends", friends);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return profileSearchList.size();
    }

    public InviteFriendsAdapter(ArrayList<User> profiles) { profileSearchList = profiles; }

    public static class InviteFriendsViewHolder extends RecyclerView.ViewHolder
    {
        public TextView profile_fn;
        public TextView profile_ln;
        public TextView profile_ph;
        public ImageView profile_image;
        public TextView profile_username;
        public View relativeLayout;
        public CheckBox check_Box;

        public InviteFriendsViewHolder(@NonNull View itemView, final InviteFriendsAdapter.OnItemClickListener listener)
        {
            super(itemView);
            profile_fn = itemView.findViewById(R.id.u_firstName);
            profile_ln = itemView.findViewById(R.id.u_lastName);
            profile_ph = itemView.findViewById(R.id.u_phone);
            profile_image = itemView.findViewById(R.id.avatarView);
            relativeLayout = itemView.findViewById(R.id.profile_search_view);
            check_Box = itemView.findViewById(R.id.checkBox);

            //checks which function to when checking/unchecking friends to be invited to events (either a function to add to the list of invited people or to remove them from the list)
            check_Box.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    CheckBox checkBox = (CheckBox)view;
                    if(listener !=null)
                    {
                        int position=getAdapterPosition();

                        if(position!=RecyclerView.NO_POSITION)
                        {
                            if(checkBox.isChecked())
                            {
                                listener.onCheck(position);
                            }
                            if(!checkBox.isChecked())
                            {
                                listener.onUnCheck(position);
                            }
                        }
                    }
                }
            });

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
