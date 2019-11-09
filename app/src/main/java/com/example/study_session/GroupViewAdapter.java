package com.example.study_session;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroupViewAdapter extends RecyclerView.Adapter<GroupViewAdapter.GroupViewViewHolder> {
    //subclass for adapting RecyclerView.ViewHolder to new layout
    public static class GroupViewViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout theView;

        public GroupViewViewHolder(LinearLayout v){
            super(v);
            theView = v;
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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewAdapter.GroupViewViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
