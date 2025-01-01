package com.example.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance.Service.Response.CreateStudentResponse;
import com.example.attendance.Service.Response.Service;
import com.example.attendance.Service.RestClient;
import com.example.attendance.Service.Static;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterStudDetails extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EnterStudDetails";
    private Context context;

    // Declare UI components
    private EditText etUsername, etPassword, etName, etMobile, etEmail, etDob, etGender;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_stud_details);

        // Initialize context
        context = this;

        // Initialize UI components
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        etDob = findViewById(R.id.etDob);
        etGender = findViewById(R.id.etGender);
        btnSubmit = findViewById(R.id.button_save);

        // Set OnClickListener
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_save) {
            createStudent();
        }
    }

    private void createStudent() {
        // Collect input values
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String gender = etGender.getText().toString().trim();



        // Validate required fields
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty()) {
            Toast.makeText(context, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Required fields are missing");
            return;
        }

        // Make API call to create a student
        RestClient.getInstance().create(Service.class).createStudent(
                username, password, name, mobile, email, dob, gender
        ).enqueue(new Callback<CreateStudentResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreateStudentResponse> call, @NonNull Response<CreateStudentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Student Created Successfully: " + response.body().getMessage());
                    finish();
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateStudentResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error: " + t.getMessage(), t);
            }
        });
    }

    private void handleErrorResponse(Response<CreateStudentResponse> response) {
        try {
            String errorMessage = new Gson().fromJson(response.errorBody().string(), CreateStudentResponse.class).getMessage();
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Error: " + errorMessage);
        } catch (Exception e) {
            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error parsing error response", e);
        }

    }
}
