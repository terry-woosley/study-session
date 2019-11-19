package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GroupSearchActivity extends AppCompatActivity {

    private GestureDetectorCompat detector = null;

    RecyclerView groupsViewRV;

    GroupSearchViewAdapter groupSearchViewAdapter;

    String uid;
    String school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search);

        Intent intent = getIntent();
        school = intent.getStringExtra("school");
        uid = intent.getStringExtra("uid");

        this.getGroups();

        groupsViewRV = findViewById(R.id.groupsRV);
        groupSearchViewAdapter = new GroupSearchViewAdapter();
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

        TextView searchBar = findViewById(R.id.searchET);
        searchBar.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (groupSearchViewAdapter != null){
                    groupSearchViewAdapter.getFilter().filter(s);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getGroups();
    }

    public void showGroup(View view) {
        Intent showGroup = new Intent(this, GroupActivity.class);
        startActivity(showGroup);
    }

    public void getGroups(){
        GroupSearchViewAdapter.filteredGroupList = new ArrayList<Group>();
        Group.getGroupsFromUniversity(school, GroupSearchViewAdapter.filteredGroupList, new Group.CallBackFunction() {
            @Override
            public void done() {
                groupSearchViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void error(Exception e) {
                Toast.makeText(getApplicationContext(),"Error occurred! Please try again!",Toast.LENGTH_SHORT).show();
            }
        });
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
                    Intent intent = new Intent(getBaseContext(),GroupActivity.class);
                    intent.putExtra("group", GroupSearchViewAdapter.filteredGroupList.get(position));
                    intent.putExtra("join",true);
                    intent.putExtra("uid",uid);
                    startActivity(intent);
                    return true;
                }
            }
            return false;
        }
    }

}
