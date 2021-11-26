package com.example.and_project.data;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;

public class EventLiveData extends LiveData<Event> {
    private final EventListener<DocumentSnapshot> listener = (value, error) -> {
        setValue(new Event(value.getId(), value.getData()));
    };
    DocumentReference documentReference;
    ListenerRegistration listenerRegistration;

    public EventLiveData(DocumentReference ref) {
        documentReference = ref;
    }

    @Override
    protected void onActive() {
        super.onActive();
        listenerRegistration = documentReference.addSnapshotListener(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (listenerRegistration != null)
            listenerRegistration.remove();
    }
}
