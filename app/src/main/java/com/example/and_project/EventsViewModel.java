package com.example.and_project;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.and_project.data.Event;
import com.example.and_project.data.FirebaseRepository;

import java.util.ArrayList;

public class EventsViewModel extends AndroidViewModel {
    private final FirebaseRepository firebaseRepository;

    public EventsViewModel(@NonNull Application application) {
        super(application);
        firebaseRepository = FirebaseRepository.getInstance();
    }

    public LiveData<ArrayList<Event>> getEvents() {
        return firebaseRepository.getEventLiveData();
    }
}
