package com.example.attendance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.attendance.Service.Response.FetchCourseResponse;
import com.example.attendance.Service.RestClient;
import com.example.attendance.Service.SF;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Courses extends AppCompatActivity {

    private RecyclerView courseListRV;
    private CourseAdapter adapter;
    private ImageView addButton;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        // Initialize RecyclerView
        courseListRV = findViewById(R.id.courseListRV);
        addButton = findViewById(R.id.add_button);
        courseListRV.setLayoutManager(new LinearLayoutManager(this));

        // Fetch and display courses
        fetchCourses();

        addButton.setOnClickListener(v -> {
            Intent i = new Intent(this, CreateCourse.class);
            startActivity(i);

        });
    }

    private void fetchCourses() {
        int userId = SF.getLoginSF(this).getInt(SF.LOGIN_USER_ID, 0);

        RestClient.makeAPI().fetchCourses(userId).enqueue(new Callback<FetchCourseResponse>() {
            @Override
            public void onResponse(Call<FetchCourseResponse> call, Response<FetchCourseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FetchCourseResponse.CourseDetails> courses = response.body().getData();
                    adapter = new CourseAdapter(courses, Courses.this);
                    courseListRV.setAdapter(adapter);
                } else {
                    Toast.makeText(Courses.this, "Failed to fetch courses", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchCourseResponse> call, Throwable t) {
                Toast.makeText(Courses.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {
        private final List<FetchCourseResponse.CourseDetails> courseList;

        FragmentActivity activity;
        public CourseAdapter(List<FetchCourseResponse.CourseDetails> courseList, FragmentActivity activity) {
            this.courseList = courseList;
            this.activity = activity;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            FetchCourseResponse.CourseDetails data = courseList.get(position);

            holder.courseName.setText(data.getCourse_name());
            holder.courseCode.setText(data.getCourse_code());
            Glide.with(holder.itemView.getContext()).load(data.getImage_url()).into(holder.courseImage);

            // Set OnClickListener for each item
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), CourseDetails.class);
                intent.putExtra("courseId", data.getCourse_id());
                SF.setLoginSFValue(activity, SF.COURSE_CODE_FOR_QR_SCANNER, data.getCourse_id());
                holder.itemView.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return courseList.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView courseName;
            public TextView courseCode;
            public ImageView courseImage;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                courseName = itemView.findViewById(R.id.course);
                courseCode = itemView.findViewById(R.id.code);
                courseImage = itemView.findViewById(R.id.image);
            }
        }
    }
}
