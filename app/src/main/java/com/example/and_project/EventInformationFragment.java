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

import java.util.stream.Collectors;

public class EventInformationFragment extends Fragment {
    Event event;
    public EventInformationFragment(Event event) {
        this.event = event;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_information, container, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.eventInfoTitle);
        TextView tvDate = (TextView) view.findViewById(R.id.eventInfoDate);
        TextView tvTime = (TextView) view.findViewById(R.id.eventInfoTime);
        TextView tvRoom = (TextView) view.findViewById(R.id.eventInfoRoom);
        TextView tvTags = (TextView) view.findViewById(R.id.eventInfoTags);
        TextView tvDescription = (TextView) view.findViewById(R.id.eventInfoDescription);

        tvTitle.setText(event.getTitle());
        tvDate.setText("Date: " + event.getPrettyDate());
        tvTime.setText("Time: " + event.getPrettyTime());
        if (event.getRoom() != null && !event.getRoom().isEmpty()) {
            tvRoom.setText("Room: " + event.getRoom());
        }
        String tagsString = "";
        if (event.getTags().size() > 0)
            tagsString = "Tags: " + event.getTags().stream().collect(Collectors.joining(", "));
        tvTags.setText(tagsString);
        tvDescription.setText(event.getDescription());
        return view;
    }
}