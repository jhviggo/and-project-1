package com.example.and_project.data;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EventListLiveData extends LiveData<ArrayList<Event>> {
    private final EventListener<QuerySnapshot> listener = (value, error) -> {
        Log.d("YUSH", "UPDATE " + value);
        if (value != null) {
            ArrayList<Event> newData = new ArrayList<>();
            for (DocumentSnapshot document : value.getDocuments()) {
                newData.add(new Event(document.getId(), document.getData()));
            }
            setValue(newData);
        }
    };
    Query databaseQueryReference;
    ListenerRegistration listenerRegistration;

    public EventListLiveData(Query ref) {
        databaseQueryReference = ref;
    }

    @Override
    protected void onActive() {
        super.onActive();
        listenerRegistration = databaseQueryReference.addSnapshotListener(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (listenerRegistration != null)
            listenerRegistration.remove();
    }
}
