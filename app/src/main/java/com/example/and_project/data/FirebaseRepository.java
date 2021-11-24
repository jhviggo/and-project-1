package com.example.and_project.data;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;

public class FirebaseRepository {
    private static String ATTENDEES = "attendees";
    public static String TITLE = "title";
    private static String ISO_DATE = "isoDate";
    private static String ROOM = "room";
    private static String DESCRIPTION = "description";
    private static String ORGANIZER = "organizer";
    private static String IMAGE = "image";

    private static FirebaseRepository instance;
    private FirebaseAuth mAuth;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final UserLiveData currentUser;
    private final EventLiveData eventLiveData;
    private CollectionReference collectionReference;
    private Query eventsQuery;

    private FirebaseRepository() {
        mAuth = FirebaseAuth.getInstance();
        collectionReference = database.collection("events");
        currentUser = new UserLiveData();
        /** Query sorting by date after today */
        eventsQuery = collectionReference
                .orderBy(ISO_DATE, Query.Direction.DESCENDING)
                .whereGreaterThan(ISO_DATE, LocalDateTime.now().toString());
        eventLiveData = new EventLiveData(eventsQuery);
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
    public EventLiveData getEventLiveData() {
        return eventLiveData;
    }

    public Task<DocumentReference> addEvent(String title, String ISODate, String room, String description) {
        Map<String, Object> docData = new HashMap<>();
        docData.put(ATTENDEES, new ArrayList<>());
        docData.put(TITLE, title);
        docData.put(ISO_DATE, ISODate);
        docData.put(ROOM, room);
        docData.put(DESCRIPTION, description);
        docData.put(ORGANIZER, mAuth.getCurrentUser().getUid());
        docData.put(IMAGE, null);

        return collectionReference.add(docData);
    }
}
