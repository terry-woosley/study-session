package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class GroupSearchActivity extends AppCompatActivity {
    ArrayList<Group> groupList = new ArrayList<Group>();

    private GestureDetectorCompat detector = null;

    RecyclerView groupsViewRV;

    GroupSearchViewAdapter groupSearchViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search);

        Intent intent = getIntent();
        String school = intent.getStringExtra("school");

        Group.getGroupsFromUniversity(school, groupList, new Group.CallBackFunction() {
            @Override
            public void done() {
                groupSearchViewAdapter.notifyDataSetChanged();

            }
        });

        groupsViewRV = findViewById(R.id.groupsRV);
        groupSearchViewAdapter = new GroupSearchViewAdapter(groupList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        groupsViewRV.setAdapter(groupSearchViewAdapter);
        groupsViewRV.setLayoutManager(linearLayoutManager);

        detector = new GestureDetectorCompat(this,
                new RecyclerViewOnGestureListener());

        groupsViewRV.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return detector.onTouchEvent(e);
            }
        });

    }

    public void showGroup(View view) {
        Intent showGroup = new Intent(this, GroupActivity.class);
        startActivity(showGroup);
    }

    public void showMain(View view) {
        finish();
    }

    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = groupsViewRV.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                RecyclerView.ViewHolder holder = groupsViewRV.getChildViewHolder(view);
                if (holder instanceof GroupSearchViewAdapter.GroupViewHolder) {
                    int position = holder.getAdapterPosition();
                    //TODO when the user click on a group

                    return true;
                }
            }
            return false;
        }
    }
}
