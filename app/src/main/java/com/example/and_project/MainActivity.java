package com.example.and_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.and_project.data.Event;
import com.example.and_project.data.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EventListAdapter.OnListItemClickListener {
    private RecyclerView rvEventsList;
    private EventListAdapter adapter;
    private ArrayList<Event> events;
    private FirebaseAuth mAuth;
    private EventsViewModel eventsViewModel;
    private UserViewModel userViewModel;
    private LiveData<User> userLiveData;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userLiveData = userViewModel.getUserLiveData();
        initNavigationDrawer();
        initRecycleView();
        eventsViewModel.getEvents();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void initNavigationDrawer() {
        // Navigation drawer / bar is based on https://proandroiddev.com/easy-approach-to-navigation-drawer-7fe87d8fd7e7
        setSupportActionBar(findViewById(R.id.toolbar));
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_logout:
                    userViewModel.signOut();
                    userViewModel.updateUserLiveData();
                    goToLogin();
                    break;
                case R.id.nav_myEvents:
                    Intent myEventsIntent = new Intent(this, MyEventsActivity.class);
                    myEventsIntent.putExtra("uid", userLiveData.getValue().getUid());
                    startActivity(myEventsIntent);
                    break;
                case R.id.nav_about:
                    startActivity(new Intent(this, AboutActivity.class));
                    break;
            }
            return true;
        });

        View headerView = navigationView.getHeaderView(0);
        userLiveData.observe(this, value -> {
            ((TextView)headerView.findViewById(R.id.nav_username)).setText(value.getName());
            ((TextView)headerView.findViewById(R.id.nav_email)).setText(value.getEmail());
        });
    }

    private void initRecycleView() {
        events = new ArrayList<>();
        rvEventsList = findViewById(R.id.rvEvents);
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
}