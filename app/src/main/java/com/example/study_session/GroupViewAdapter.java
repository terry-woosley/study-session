package com.example.study_session;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupViewAdapter extends RecyclerView.Adapter<GroupViewAdapter.GroupViewViewHolder> {
    //subclass for adapting RecyclerView.ViewHolder to new layout
    public static class GroupViewViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout groupViewHolder;

        public GroupViewViewHolder(LinearLayout v){
            super(v);
            groupViewHolder = v;
        }
    }

    private ArrayList<Group> groupList;

    //constructor
    public GroupViewAdapter(ArrayList<Group> g){
        super();
        this.groupList = g;
    }

    @NonNull
    @Override
    public GroupViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout groupLayout =(LinearLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.group_layout, parent, false);
        return new GroupViewViewHolder(groupLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewAdapter.GroupViewViewHolder holder, int position) {
        TextView groupNameTV = holder.groupViewHolder.findViewById(R.id.groupNameTV);
        TextView subjectTV = holder.groupViewHolder.findViewById(R.id.subjectTV);
        TextView schoolTV = holder.groupViewHolder.findViewById(R.id.schoolTV);

        groupNameTV.setText(groupList.get(position).groupName);
        subjectTV.setText(groupList.get(position).groupSubject);
        schoolTV.setText(groupList.get(position).groupSchool);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}
