package com.example.study_session.ProfileAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.study_session.R;
import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeAdapterViewHolder> {

    public static class TimeAdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TimeAdapterViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textView);
        }
    }

    private ArrayList<String> model;
    public TimeAdapter(ArrayList<String> model) {
        super();
        this.model = model;
    }

    @NonNull
    @Override
    public TimeAdapter.TimeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grouplist, parent, false);
        TimeAdapterViewHolder vh = new TimeAdapterViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(@NonNull TimeAdapterViewHolder holder, int position) {
        holder.textView.setText(model.get(position));
    }

    @Override
    public int getItemCount() {
        return model.size();
    }
}
