package com.example.and_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.and_project.data.Event;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EventAttendeesFragment extends Fragment {
    ArrayAdapter<String> adapter;
    ListView lvAttendeeList;

    public EventAttendeesFragment(ArrayAdapter<String> adapter) {
        this.adapter = adapter;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_attendees, container, false);
        lvAttendeeList = (ListView)view.findViewById(R.id.eventAttendeeList);
        lvAttendeeList.setAdapter(adapter);
        return view;
    }
}