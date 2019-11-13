package com.example.study_session;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroupViewAdapter extends RecyclerView.Adapter<GroupViewAdapter.GroupViewViewHolder> {
    //subclass for adapting RecyclerView.ViewHolder to new layout
    public static class GroupViewViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout groupViewHolder;

        public GroupViewViewHolder(LinearLayout v){
            super(v);
            groupViewHolder = v;
        }
    }

    private Group groupModel;

    //constructor
    public GroupViewAdapter(Group g){
        super();
        this.groupModel = g;
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

        //TODO: Fill text views with data from the database

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
