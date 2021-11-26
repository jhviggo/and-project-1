package com.example.and_project.data;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseRepository {
    private static String COLLECTION_EVENTS = "events";
    private static String COLLECTION_USERS = "users";
    private static String ATTENDEES = "attendees";
    public static String TITLE = "title";
    private static String ISO_DATE = "isoDate";
    private static String ROOM = "room";
    private static String DESCRIPTION = "description";
    private static String ORGANIZER = "organizer";
    private static String IMAGE = "image";
    private static String UID = "uid";

    private static FirebaseRepository instance;
    private FirebaseAuth mAuth;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final EventListLiveData eventListLiveData;
    private UserLiveData userLiveData;
    private CollectionReference eventCollectionReference;
    private CollectionReference userCollectionReference;
    private Query eventsQuery;

    private FirebaseRepository() {
        mAuth = FirebaseAuth.getInstance();
        eventCollectionReference = database.collection(COLLECTION_EVENTS);
        userCollectionReference = database.collection(COLLECTION_USERS);

        /** Event list: Query sorting by date after today */
        eventsQuery = eventCollectionReference
                .orderBy(ISO_DATE, Query.Direction.DESCENDING)
                .whereGreaterThan(ISO_DATE, LocalDateTime.now().toString());
        eventListLiveData = new EventListLiveData(eventsQuery);
    }

    public static synchronized FirebaseRepository getInstance() {
        if (instance == null) {
            instance = new FirebaseRepository();
        }
        return instance;
    }

    /* User auth */
    public Task<AuthResult> signIn(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signUp(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public void signOut() {
        mAuth.signOut();
    }

    public UserLiveData getUserLiveData() {
        if (userLiveData == null) {
            userLiveData = new UserLiveData(userCollectionReference.document(mAuth.getCurrentUser().getUid()));
        }
        return userLiveData;
    }

    /* Events */
    public EventListLiveData getEventListLiveData() {
        return eventListLiveData;
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

        return eventCollectionReference.add(docData);
    }

    public EventLiveData createEventLiveData(String eventId) {
        DocumentReference doc = eventCollectionReference.document(eventId);
        return new EventLiveData(doc);
    }
    public Query getAttendeeUsers(List<String> userUids) {
        if (userUids.isEmpty()) {
            return null;
        }
        return userCollectionReference.whereIn(UID, userUids);
    }

    public void attendEvent(String eventId) {
        DocumentReference doc = eventCollectionReference.document(eventId);
        // based on https://firebase.google.com/docs/firestore/manage-data/transactions
        // Uses transaction to ensure that the attendees list isn't overridden when multiple people try to attend at once
        database.runTransaction(transaction -> {
            DocumentSnapshot snapshop = transaction.get(doc);
            String userUID = mAuth.getCurrentUser().getUid();
            List<String> attendees = (List<String>) snapshop.get(ATTENDEES);
            // Makes sure the user isn't already an attendee
            if (attendees != null && !attendees.contains(userUID)) {
                attendees.add(userUID);
                transaction.update(doc, ATTENDEES, attendees);
            }
            return snapshop;
        });
    }
}
