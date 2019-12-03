package com.example.study_session.ProfileAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.study_session.R;

import java.util.ArrayList;
import java.util.Vector;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupListViewHolder> {

    public static class GroupListViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public GroupListViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textView);
        }
    }

    private ArrayList<String> model;
    public GroupListAdapter(ArrayList<String> model) {
        super();
        this.model = model;
    }

    @NonNull
    @Override
    public GroupListAdapter.GroupListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grouplist, parent, false);
        GroupListViewHolder vh = new GroupListViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(@NonNull GroupListViewHolder holder, int position) {
        holder.textView.setText(model.get(position));
    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
