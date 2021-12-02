package com.example.and_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.and_project.data.Event;
import com.example.and_project.data.FirebaseRepository;
import com.example.and_project.data.User;
import com.google.firebase.firestore.DocumentReference;

public class EventOrganizerFragment extends Fragment {
    DocumentReference userDoc;

    public EventOrganizerFragment(DocumentReference userDoc) {
        this.userDoc = userDoc;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_organizer, container, false);
        TextView tvOrganizerName = (TextView) view.findViewById(R.id.organizerName);
        TextView tvOrganizerEmail = (TextView) view.findViewById(R.id.organizerEmail);
        TextView tvOrganizerDescription = (TextView) view.findViewById(R.id.organizerDescription);
        userDoc.addSnapshotListener((value, error) -> {
            if (value != null && value.getData() != null) {
                tvOrganizerName.setText(value.getString("name"));
                tvOrganizerEmail.setText(value.getString("email"));
                tvOrganizerDescription.setText(value.getString("description").replace("\\n", "\n"));
            }
        });
        return view;
    }
}