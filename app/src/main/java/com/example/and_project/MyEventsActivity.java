package com.example.and_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.and_project.data.Event;

import java.util.ArrayList;

public class MyEventsActivity extends AppCompatActivity implements EventListAdapter.OnListItemClickListener {
    private RecyclerView rvEventsList;
    private EventListAdapter adapter;
    private ArrayList<Event> events;
    private EventsViewModel eventsViewModel;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        uid = getIntent().getStringExtra("uid");
        initToolbar();
        initRecycleView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        int eventIndex = clickedItemIndex;
        Intent singleEventIntent = new Intent(this, EventInfoActivity.class);
        singleEventIntent.putExtra("event", events.get(eventIndex));
        startActivity(singleEventIntent);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.eventInfo_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initRecycleView() {
        events = new ArrayList<>();
        rvEventsList = findViewById(R.id.rvMyEvents);
        rvEventsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventListAdapter(events, this);
        rvEventsList.setAdapter(adapter);
        eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);
        eventsViewModel.getEventsForUser(uid).observe(this, values -> {
            events.clear();
            events.addAll(values);
            adapter.notifyDataSetChanged();
        });
    }
}