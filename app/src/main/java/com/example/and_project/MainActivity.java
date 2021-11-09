package com.example.and_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.and_project.data.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EventAdapter.OnListItemClickListener {
    private RecyclerView rvEventsList;
    private EventAdapter adapter;
    private ArrayList<Event> events;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        rvEventsList = findViewById(R.id.rvEvents);
        rvEventsList.setLayoutManager(new LinearLayoutManager(this));
        // For hard-coded data only
        this.SetStaticEvents();
        adapter = new EventAdapter(events, this);
        rvEventsList.setAdapter(adapter);
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

    private void SetStaticEvents() {
        events = new ArrayList<>();
        events.add(new Event("Git Workshop", R.drawable.missing));
        events.add(new Event("Pizza Party", R.drawable.missing));
        events.add(new Event("Homework Help", R.drawable.missing));
        events.add(new Event("Something cool", R.drawable.missing));
        events.add(new Event("Designer class1", R.drawable.missing));
        events.add(new Event("Designer class2", R.drawable.missing));
        events.add(new Event("Designer class3", R.drawable.missing));
        events.add(new Event("Designer class4", R.drawable.missing));
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