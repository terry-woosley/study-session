package com.example.study_session;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupSearchViewAdapter extends RecyclerView.Adapter<GroupSearchViewAdapter.GroupViewHolder> {
    public static class GroupViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout groupViewHolder;

        public GroupViewHolder(LinearLayout v){
            super(v);
            groupViewHolder = v;
        }
    }

    private ArrayList<Group> groupList;

    public GroupSearchViewAdapter(ArrayList<Group> groupList){
        super();
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout groupLayout =(LinearLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.group_search_layout, parent, false);
        return new GroupViewHolder(groupLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupSearchViewAdapter.GroupViewHolder holder, int position) {
        TextView groupNameTV = holder.groupViewHolder.findViewById(R.id.groupNameTV);
        TextView subjectTV = holder.groupViewHolder.findViewById(R.id.subjectTV);

        groupNameTV.setText(groupList.get(position).groupName);
        subjectTV.setText(groupList.get(position).groupSubject);
    }

    @Override
    public int getItemCount() {

        return groupList.size();
    }

}
