package com.example.and_project.data;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FirebaseRepository {
    private static FirebaseRepository instance;
    private FirebaseAuth mAuth;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final UserLiveData currentUser;
    private final EventLiveData eventLiveData;
    private CollectionReference collectionReference;

    private FirebaseRepository() {
        mAuth = FirebaseAuth.getInstance();
        collectionReference = database.collection("events");
        currentUser = new UserLiveData();
        eventLiveData = new EventLiveData(collectionReference);
    }

    public static synchronized FirebaseRepository getInstance() {
        if (instance == null) {
            instance = new FirebaseRepository();
        }
        return instance;
    }

    /* User auth */
    public LiveData<FirebaseUser> getCurrentUser() {
        return currentUser;
    }

    public Task<AuthResult> signIn(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signUp(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public void signOut() {
        mAuth.signOut();
    }

    /* Events */
    public void fetchEvents() {
        collectionReference.get();
    }

    public EventLiveData getEventLiveData() {
        return eventLiveData;
    }
}
