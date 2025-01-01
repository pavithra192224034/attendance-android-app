package com.example.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.attendance.Service.Response.FetchCourseResponse;
import com.example.attendance.Service.Response.FetchStudentResponse;
import com.example.attendance.Service.RestClient;
import com.example.attendance.Student.StudentDetailsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTotal extends AppCompatActivity {
    private RecyclerView studentRecyclerView;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_total);
        studentRecyclerView = findViewById(R.id.courseListRV);
        studentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchStudents();
    }
        public void add_button(View v) {
            Intent i = new Intent(this, EnterStudDetails.class);
            startActivity(i);


            }


    private void fetchStudents() {
        RestClient.makeAPI().studentfetch().enqueue(new Callback<StudentDetailsResponse>() {
            @Override
            public void onResponse(Call<StudentDetailsResponse> call, Response<StudentDetailsResponse> response) {
                if(response.isSuccessful()) {
                    List<StudentDetailsResponse.Users> student = response.body().getUsers();
                    adapter = new StudentAdapter(student);
                    studentRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(com.example.attendance.StudentTotal.this, "Failed to fetch courses", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StudentDetailsResponse> call, Throwable t) {
                Toast.makeText(StudentTotal.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

        // List of students
        private final List<StudentDetailsResponse.Users> studentList;

        // Constructor to initialize the list
        public StudentAdapter(List<StudentDetailsResponse.Users> studentList) {
            this.studentList = studentList;
        }

        // Inflating the layout for each item in the RecyclerView
        @NonNull
        @Override
        public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
            return new StudentViewHolder(view);
        }

        // Binding the data to the ViewHolder
        @Override
        public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
            StudentDetailsResponse.Users student = studentList.get(position);
            holder.nameTextView.setText(student.getStudent_name());

        }

        // Returning the total count of items
        @Override
        public int getItemCount() {
            return studentList==null?0:studentList.size();
        }

        // ViewHolder class for managing item views
        public  class StudentViewHolder extends RecyclerView.ViewHolder {
            TextView nameTextView;

            public StudentViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.course);


            }
        }
    }


}