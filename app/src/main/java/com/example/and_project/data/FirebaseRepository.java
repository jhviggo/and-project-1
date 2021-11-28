package com.example.and_project.data;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseRepository {
    private static String COLLECTION_EVENTS = "events";
    private static String COLLECTION_USERS = "users";
    private static String ATTENDEES = "attendees";
    private static String TITLE = "title";
    private static String ISO_DATE = "isoDate";
    private static String ROOM = "room";
    private static String DESCRIPTION = "description";
    private static String ORGANIZER = "organizer";
    private static String HAS_IMAGE = "hasImage";
    private static String UID = "uid";
    private static String EMAIL = "email";
    private static String NAME = "name";
    private static String IS_PUBLIC = "isPublic";

    private static FirebaseRepository instance;
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private FirebaseStorage storage;
    private final EventListLiveData eventListLiveData;
    private UserLiveData userLiveData;
    private CollectionReference eventCollectionReference;
    private CollectionReference userCollectionReference;
    private StorageReference storageRef;
    private Query eventsQuery;

    private FirebaseRepository() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        eventCollectionReference = database.collection(COLLECTION_EVENTS);
        userCollectionReference = database.collection(COLLECTION_USERS);

        /** Event list: Query sorting by date after today */
        eventsQuery = eventCollectionReference
                .orderBy(ISO_DATE, Query.Direction.DESCENDING)
                .whereGreaterThan(ISO_DATE, LocalDateTime.now().toString())
                .whereEqualTo(IS_PUBLIC, true);
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
        String uid = mAuth.getCurrentUser() != null
                ? mAuth.getCurrentUser().getUid()
                : "-1"; // invalid uid to return null to prevent firebase from throwing
        if (uid != null && !uid.isEmpty()) {
            userLiveData = new UserLiveData(userCollectionReference.document(uid));
        }
        return userLiveData;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void addUserDetails(String email, String name, String uid, String description) {
        Map<String, Object> docData = new HashMap<>();
        docData.put(EMAIL, email);
        docData.put(NAME, name);
        docData.put(UID, uid);
        docData.put(DESCRIPTION, description);
        userCollectionReference.document(uid).set(docData);
    }

    public DocumentReference getUserById(String uid) {
        return userCollectionReference.document(uid);
    }

    /* Events */
    public EventListLiveData getEventListLiveData() {
        return eventListLiveData;
    }

    public EventListLiveData createEventListLiveDataForUser(String uid) {
        Query myEventsQuery = eventCollectionReference
                .orderBy(ISO_DATE, Query.Direction.DESCENDING)
                .whereGreaterThan(ISO_DATE, LocalDateTime.now().toString())
                .whereEqualTo(ORGANIZER, uid);
        return new EventListLiveData(myEventsQuery);
    }

    public Task<DocumentReference> addEvent(String title, String ISODate, String room, String description, boolean hasImage, boolean isPublic) {
        Map<String, Object> docData = new HashMap<>();
        docData.put(ATTENDEES, new ArrayList<>());
        docData.put(TITLE, title);
        docData.put(ISO_DATE, ISODate);
        docData.put(ROOM, room);
        docData.put(DESCRIPTION, description);
        docData.put(ORGANIZER, mAuth.getCurrentUser().getUid());
        docData.put(HAS_IMAGE, hasImage);
        docData.put(IS_PUBLIC, isPublic);

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

    /* Storage */
    public void uploadImage(Bitmap bitmap, String fileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        storageRef.child(fileName).putBytes(data);
    }

    public Task<byte[]> getImage(String eventId) {
        final long BYTE_ARRAY_LENGTH = 512 * 1024; // 512KB
        return storageRef.child(eventId).getBytes(BYTE_ARRAY_LENGTH);
    }
}
