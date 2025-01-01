package com.example.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.attendance.Service.Response.ErrorResponse;
import com.example.attendance.Service.Response.LoginResponse;
import com.example.attendance.Service.Response.Service;
import com.example.attendance.Service.RestClient;
import com.example.attendance.Service.SF;
import com.example.attendance.Student.StudentHome;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacultyLogin extends AppCompatActivity {

    private static final String TAG = "FacultyLogin";
    private Context context;
    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;

    FragmentActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_login);

        context = this;
        activity = this;

        // Initialize UI components
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);

        // Set up the login button
        setupLoginButton();
    }


    private void setupLoginButton() {
        loginButton.setOnClickListener(v -> {
            String regNo = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            // Check for empty fields
            if (regNo.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Make API call for login
            apiCall(regNo, password);
        });
    }

    private void apiCall(String username, String password) {
        Service apiService = RestClient.makeAPI();
        Call<LoginResponse> call = apiService.login(username, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    LoginResponse.Admin admin = loginResponse.getAdmin();

                    if (admin != null) {
                        saveUserSession(admin);
                    } else {
                        Toast.makeText(context, "Login failed: Admin data is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API call failed", t);
                runOnUiThread(() -> Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void saveUserSession(LoginResponse.Admin admin) {
        SF.setLoginSFValue(activity, SF.LOGIN_USER_TYPE, admin.getUser_type());
        SF.setLoginSFValue(activity, SF.LOGIN_USER_ID, admin.getUser_id());
        SF.setLoginSFValue(activity, SF.LOGIN_USER_NAME, admin.getUsername());
        SF.setLoginSFValue(activity, SF.LOGIN_USER_LOGGED_IN, true);
        SF.setLoginSFValue(activity, SF.LOGIN_USER_EMAIL, admin.getEmail());
        SF.setLoginSFValue(activity, SF.LOGIN_USER_REG_NO, admin.getRegister_number());


        if(SF.getLoginSF(activity).getBoolean(SF.LOGIN_USER_LOGGED_IN, false)) {
            int userType = SF.getLoginSF(activity).getInt(SF.LOGIN_USER_TYPE, 0);
            if (userType == 100) {
                startActivity(new Intent(context, StudentHome.class));
            }else if(userType == 110) {
                startActivity(new Intent(context, Courses.class));
            }
        }

        Log.i(TAG, "User session saved successfully");
    }


    private void handleErrorResponse(Response<LoginResponse> response) {
        try {
            if (response.errorBody() != null) {
                ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(), ErrorResponse.class);
                Toast.makeText(context, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Unknown error occurred", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing error response", e);
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
