package com.example.and_project;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.and_project.data.Event;
import com.example.and_project.data.FirebaseRepository;

public class EventViewModel extends AndroidViewModel {
    private final FirebaseRepository firebaseRepository;

    public EventViewModel(@NonNull Application application) {
        super(application);
        firebaseRepository = FirebaseRepository.getInstance();
    }

    public LiveData<Event> getEvents(String event) {
        return firebaseRepository.createEventLiveData(event);
    }
}
