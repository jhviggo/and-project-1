package com.example.and_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.and_project.data.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EventAdapter.OnListItemClickListener {
    private RecyclerView rvEventsList;
    private EventAdapter adapter;
    private ArrayList<Event> events;
    private FirebaseAuth mAuth;
    private EventsViewModel eventsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        rvEventsList = findViewById(R.id.rvEvents);
        events = new ArrayList<>();
        rvEventsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventAdapter(events, this);
        rvEventsList.setAdapter(adapter);
        eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);

        eventsViewModel.getEvents().observe(this, values -> {
            events.add(values.get(0));
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
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
        singleEventIntent.putExtra("title", events.get(eventIndex).getTitle());
        singleEventIntent.putExtra("description", events.get(eventIndex).getDescription());
        startActivity(singleEventIntent);
    }
}