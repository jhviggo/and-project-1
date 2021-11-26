package com.example.and_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.and_project.data.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EventListAdapter.OnListItemClickListener {
    private RecyclerView rvEventsList;
    private EventListAdapter adapter;
    private ArrayList<Event> events;
    private FirebaseAuth mAuth;
    private EventsViewModel eventsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        mAuth = FirebaseAuth.getInstance();
        rvEventsList = findViewById(R.id.rvEvents);
        events = new ArrayList<>();
        rvEventsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventListAdapter(events, this);
        rvEventsList.setAdapter(adapter);
        eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);

        eventsViewModel.getEvents().observe(this, values -> {
            events.clear();
            events.addAll(values);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            goToLogin();
        }
    }

    public void addNewEvent(View view) {
        startActivity(new Intent(this, CreateEventActivity.class));
    }

    private void goToLogin() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        int eventIndex = clickedItemIndex;
        Intent singleEventIntent = new Intent(this, EventInfoActivity.class);
        singleEventIntent.putExtra("event", events.get(eventIndex));
        startActivity(singleEventIntent);
    }
}