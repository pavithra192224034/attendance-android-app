package com.example.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance.Service.Constants;
import com.example.attendance.Service.Response.AbsentListResponse;
import com.example.attendance.Service.Response.ErrorResponse;
import com.example.attendance.Service.Response.Service;
import com.example.attendance.Service.Response.Student;
import com.example.attendance.Service.Response.UnscannedAdapter;
import com.example.attendance.Service.SF;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AfterScanned extends AppCompatActivity {

    private static final String TAG = "AfterScanned";

    private RecyclerView recyclerView;
    private UnscannedAdapter unscannedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_scanned);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter
        unscannedAdapter = new UnscannedAdapter();
        recyclerView.setAdapter(unscannedAdapter);
        int facultyId;
        int courseId ;
        facultyId = SF.getLoginSF(AfterScanned.this).getInt(SF.LOGIN_USER_ID, 0);
        courseId = SF.getLoginSF(AfterScanned.this).getInt(SF.COURSE_CODE_FOR_QR_SCANNER, 0);

        // Fetch Unscanned Students
        fetchUnscannedStudents(facultyId, courseId);
    }

    private void fetchUnscannedStudents(int facultyId, int courseId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service service = retrofit.create(Service.class);

        service.getUnScannedStudents(facultyId, courseId).enqueue(new Callback<AbsentListResponse>() {
            @Override
            public void onResponse(Call<AbsentListResponse> call, Response<AbsentListResponse> response) {
                if(response.isSuccessful()) {

                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<AbsentListResponse> call, Throwable t) {
                Toast.makeText(AfterScanned.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleErrorResponse(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(), ErrorResponse.class);
                Toast.makeText(this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing error response", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
