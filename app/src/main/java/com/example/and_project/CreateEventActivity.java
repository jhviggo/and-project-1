package com.example.and_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.and_project.data.FirebaseRepository;

import java.time.LocalDateTime;

public class CreateEventActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;

    FirebaseRepository repository;
    ImageButton ibImage;
    TextView tvTitleError;
    TextView tvRoom;
    DatePicker tvDate;
    TimePicker tvTime;
    TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        repository = FirebaseRepository.getInstance();
        TimePicker picker=(TimePicker)findViewById(R.id.eventTime);
        picker.setIs24HourView(true);
        tvTitleError = findViewById(R.id.eventTitleError);
        tvTitleError.setTextColor(Color.RED);
        ibImage = findViewById(R.id.eventImage);
        tvRoom = findViewById(R.id.eventRoom);
        tvDate = (DatePicker) findViewById(R.id.eventDate);
        tvTime = (TimePicker) findViewById(R.id.eventTime);
        tvDescription = findViewById(R.id.eventDescription);
        initToolbar();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void addNewEvent(View view) {
        tvTitleError.setText("");
        TextView tvTitle = findViewById(R.id.eventTitle);
        if (tvTitle.getText().toString().equals("")) {
            tvTitleError.setText("Title is required");
            return;
        }
        String ISODate = LocalDateTime.of(tvDate.getYear(), tvDate.getMonth(), tvDate.getDayOfMonth(), tvTime.getHour(), tvTime.getMinute()).toString();

        repository.addEvent(
                tvTitle.getText().toString(),
                ISODate,
                tvRoom.getText().toString(),
                tvDescription.getText().toString()
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                finish();
            } else {
                Toast.makeText(this, "Unable to create event at this time", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void dispatchTakePictureIntent(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, PICK_IMAGE);
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.eventInfo_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.createEvent_title);
    }

    /** based on https://medium.com/@hasangi/capture-image-or-choose-from-gallery-photos-implementation-for-android-a5ca59bc6883 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null) {
            Uri uri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            ibImage.setImageBitmap(bitmap);
            if (cursor != null)
                cursor.close();
        }
    }
}