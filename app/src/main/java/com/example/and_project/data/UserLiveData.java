package com.example.and_project.data;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;

public class UserLiveData extends LiveData<User> {
    private final EventListener<DocumentSnapshot> listener = (value, error) -> {
        if (value != null && value.getData() != null)
            setValue(new User(value.getData()));
    };
    DocumentReference documentReference;
    ListenerRegistration listenerRegistration;

    public UserLiveData(DocumentReference ref) {
        documentReference = ref;
    }

    public UserLiveData() {
        setValue(null);
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
