package com.example.and_project.data;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EventLiveData extends LiveData<ArrayList<Event>> {
    private final EventListener<QuerySnapshot> listener = (value, error) -> {
        ArrayList<Event> newData = new ArrayList<>();
        for (DocumentSnapshot document : value.getDocuments()) {
            newData.add(new Event(document.getId(), document.getData()));
        }
        setValue(newData);
    };
    CollectionReference databaseReference;
    ListenerRegistration listenerRegistration;

    public EventLiveData(CollectionReference ref) {
        databaseReference = ref;
    }

    @Override
    protected void onActive() {
        super.onActive();
        listenerRegistration = databaseReference.addSnapshotListener(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (listenerRegistration != null)
            listenerRegistration.remove();
    }
}
