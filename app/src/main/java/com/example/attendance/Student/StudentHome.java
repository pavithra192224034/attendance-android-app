package com.example.attendance.Student;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.attendance.CourseDetails;
import com.example.attendance.Courses;
import com.example.attendance.CreateCourse;
import com.example.attendance.R;
import com.example.attendance.Service.Response.FetchCourseResponse;
import com.example.attendance.Service.RestClient;
import com.example.attendance.Service.SF;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentHome extends AppCompatActivity {



        private RecyclerView courseListRV;
        private CourseAdapter adapter;
        private ImageView addButton;

        @SuppressLint("WrongViewCast")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_student_home);

            // Initialize RecyclerView
            courseListRV = findViewById(R.id.courseListRV);

            courseListRV.setLayoutManager(new LinearLayoutManager(this));

            // Fetch and display courses
            fetchCourses();

        }

        private void fetchCourses() {
            int userId = SF.getLoginSF(this).getInt(SF.LOGIN_USER_ID, 0);

            RestClient.makeAPI().studentfetchCourses().enqueue(new Callback<FetchCourseResponse>() {
                @Override
                public void onResponse(Call<FetchCourseResponse> call, Response<FetchCourseResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<FetchCourseResponse.CourseDetails> courses = response.body().getData();
                        adapter = new CourseAdapter(courses);
                        courseListRV.setAdapter(adapter);
                    } else {
                        Toast.makeText(com.example.attendance.Student.StudentHome.this, "Failed to fetch courses", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FetchCourseResponse> call, Throwable t) {
                    Toast.makeText(com.example.attendance.Student.StudentHome.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private static class CourseAdapter extends RecyclerView.Adapter<com.example.attendance.Courses.CourseAdapter.MyViewHolder> {
            private final List<FetchCourseResponse.CourseDetails> courseList;

            public CourseAdapter(List<FetchCourseResponse.CourseDetails> courseList) {
                this.courseList = courseList;
            }

            @NonNull
            @Override
            public com.example.attendance.Courses.CourseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
                return new com.example.attendance.Courses.CourseAdapter.MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(@NonNull com.example.attendance.Courses.CourseAdapter.MyViewHolder holder, int position) {
                FetchCourseResponse.CourseDetails data = courseList.get(position);

                holder.courseName.setText(data.getCourse_name());
                holder.courseCode.setText(data.getCourse_code());
                Glide.with(holder.itemView.getContext()).load(data.getImage_url()).into(holder.courseImage);

                // Set OnClickListener for each item
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(holder.itemView.getContext(), StudentCourseDetails.class);
                    intent.putExtra("courseId", data.getCourse_id());
                    holder.itemView.getContext().startActivity(intent);
                });

            }

            @Override
            public int getItemCount() {
                return courseList.size();
            }

            static class MyViewHolder extends RecyclerView.ViewHolder {
                TextView courseName, courseCode;
                ImageView courseImage;

                public MyViewHolder(@NonNull View itemView) {
                    super(itemView);
                    courseName = itemView.findViewById(R.id.course);
                    courseCode = itemView.findViewById(R.id.code);
                    courseImage = itemView.findViewById(R.id.image);
                }
            }
        }
    }
