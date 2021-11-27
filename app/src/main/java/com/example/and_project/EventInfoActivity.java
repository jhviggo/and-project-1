package com.example.and_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.and_project.data.Event;
import com.example.and_project.data.FirebaseRepository;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.stream.Collectors;

public class EventInfoActivity extends AppCompatActivity {
    FirebaseRepository repository;
    Event event;
    Fragment informationFragment;
    Fragment attendeesFragment;
    Fragment organizerFragment;
    UserViewModel userViewModel;
    EventViewModel eventViewModel;
    ArrayAdapter<String> attendeesAdapter;
    ImageView eventImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        repository = FirebaseRepository.getInstance();
        Intent intent = getIntent();
        eventImage = (ImageView) findViewById(R.id.eventInfoImage);
        event = (Event)intent.getExtras().get("event");
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        attendeesAdapter = new ArrayAdapter<>(this, R.layout.attendee_list_item, R.id.lvAttendeeName, event.getAttendees());
        informationFragment = new EventInformationFragment(event);
        attendeesFragment = new EventAttendeesFragment(attendeesAdapter);
        organizerFragment = new EventOrganizerFragment(repository.getUserById(event.getOrganizer()));
        setupFrabmentTab();
        initToolbar();

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getEvents(event.getId()).observe(this, value -> {
            event = value;
            Query attendeeUsersQuery = repository.getAttendeeUsers(event.getAttendees());
            if (attendeeUsersQuery != null) {
                attendeeUsersQuery.addSnapshotListener((nameValues, error) -> {
                    // map the user uids over to names
                    List<String> names = nameValues.getDocuments()
                            .stream()
                            .map(i -> (String) i.get("name"))
                            .collect(Collectors.toList());
                    attendeesAdapter.clear();
                    attendeesAdapter.addAll(names);
                    attendeesAdapter.notifyDataSetChanged();
                });
            }
        });
        if (event.hasImage())
            updateEventImage();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void attendEvent(View view) {
        repository.attendEvent(event.getId());
    }

    private void updateEventImage() {
        // Images should probably be references in firestore on the events objects
        // That was I wouldn't have to keep downloading them like this
        eventViewModel.getImage(event.getId()).addOnCompleteListener(value -> {
            if (value.isSuccessful()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeByteArray(value.getResult(), 0, value.getResult().length, options);
                eventImage.setImageBitmap(bitmap);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.eventInfo_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(event.getTitle());
    }

    private void setupFrabmentTab() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, informationFragment).commit();

        ((TabLayout) findViewById(R.id.tabLayout)).addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment currentFragment;
                switch (tab.getPosition()) {
                    case 1:
                        currentFragment = attendeesFragment;
                        break;
                    case 2:
                        currentFragment = organizerFragment;
                        break;
                    default:
                        currentFragment = informationFragment;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, currentFragment).commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}