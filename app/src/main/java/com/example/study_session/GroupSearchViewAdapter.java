package com.example.study_session;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Vector;

public class GroupSearchViewAdapter extends RecyclerView.Adapter<GroupSearchViewAdapter.GroupViewHolder> implements Filterable {

    public static class GroupViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout groupViewHolder;

        public GroupViewHolder(LinearLayout v){
            super(v);
            groupViewHolder = v;
        }
    }

    private Vector<Group> groupVector;

    public static Vector<Group> filteredGroupVector;

    public GroupSearchViewAdapter(){
        super();
        this.groupVector = new Vector<Group>();
        filteredGroupVector = new Vector<Group>();
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout groupLayout =(LinearLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.group_search_layout, parent, false);
        return new GroupViewHolder(groupLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupSearchViewAdapter.GroupViewHolder holder, int position) {
        TextView groupNameTV = holder.groupViewHolder.findViewById(R.id.memberNameTV);
        TextView subjectTV = holder.groupViewHolder.findViewById(R.id.subjectTV);

        groupNameTV.setText(filteredGroupVector.get(position).groupName);
        subjectTV.setText(filteredGroupVector.get(position).groupSubject);
    }

    @Override
    public int getItemCount() {

        return filteredGroupVector.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String charString = constraint.toString();

                if (charString.isEmpty()){
                    filteredGroupVector = groupVector;
                }else{

                    Vector<Group> filterVector = new Vector<>();

                    for (Group data : groupVector){

                        if (data.groupName.toLowerCase().contains(charString)){
                            filterVector.add(data);
                        }
                    }

                    filteredGroupVector = filterVector;
                }

                return new FilterResults();
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

}
