package com.example.study_session;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class GroupMemberViewAdapter extends RecyclerView.Adapter<GroupMemberViewAdapter.GroupMemberViewHolder> {
    //subclass for adapting RecyclerView.ViewHolder to new layout
    public static class GroupMemberViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout GroupMemberViewHolder;

        public GroupMemberViewHolder(LinearLayout v){
            super(v);
            GroupMemberViewHolder = v;
        }
    }

    private Vector<Profile> memberList;

    //constructor
    public GroupMemberViewAdapter(Vector<Profile> g){
        super();
        this.memberList = g;
    }

    @NonNull
    @Override
    public GroupMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout memberLayout =(LinearLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.member_layout, parent, false);
        return new GroupMemberViewHolder(memberLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupMemberViewAdapter.GroupMemberViewHolder holder, int position) {
        TextView memberNameTV = holder.GroupMemberViewHolder.findViewById(R.id.memberNameTV);

        memberNameTV.setText(memberList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }
}
