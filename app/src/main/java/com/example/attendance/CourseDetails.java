package com.example.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CourseDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);


        // Initialize Buttons
        Button btnAttendance = findViewById(R.id.btn_attendance);
        Button btnStudentDetails = findViewById(R.id.btn_student_details);

        // Set click listener for btnAttendan
        // Set click listener for btnStudentDetails
        btnStudentDetails.setOnClickListener(v -> {
            Intent intent = new Intent(CourseDetails.this, StudentTotal.class);
            startActivity(intent);
        });

        }
    public void yes(View v) {
        Intent i = new Intent(this, QrScanner.class);
        startActivity(i);
    }

}

