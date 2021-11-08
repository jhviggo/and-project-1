package com.example.and_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class EventInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        Intent intent = getIntent();
        TextView tvTitle = findViewById(R.id.infoTitle);
        TextView tvDescription = findViewById(R.id.infoDescription);

        tvTitle.setText(intent.getStringExtra("title"));
        tvDescription.setText(intent.getStringExtra("description"));
    }
}