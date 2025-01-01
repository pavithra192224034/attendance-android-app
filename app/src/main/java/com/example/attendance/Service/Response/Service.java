package com.example.attendance.Service.Response;

import com.example.attendance.Student.StudentDetailsResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import java.util.List;


public interface Service {
        @POST("admin/login")
        Call<LoginResponse> login(@Query("username") String username, @Query("password") String password);


        @GET("admin/fetch_courses") // Replace with your endpoint path
        Call<FetchCourseResponse> fetchCourses(@Query("faculty_id")int faculty_id);

        @GET("admin/stud_fetch_courses")
        Call<FetchCourseResponse> studentfetchCourses();

        @GET("admin/fetch_students")
        Call<StudentDetailsResponse> studentfetch();
        @Multipart
        @POST("admin/create_course")
        Call<CreateCourseResponse> uploadCourse(
                @Part("course_name") RequestBody courseName,
                @Part("course_code") RequestBody courseCode,
                @Part("Faculty_id") RequestBody Faculty_id,
                @Part MultipartBody.Part courseImage
        );
        // Create faculty endpoint
        @POST("admin/create_student")
        Call<CreateStudentResponse> createStudent(
                @Query("username") String username,
                @Query("password") String password,
                @Query("name") String name,
                @Query("mobile") String mobile,
                @Query("email") String email,
                @Query("dob") String dob,
                @Query("gender") String gender
        );


                // Register student
                @POST("admin/scanner")
                Call<ScannerResponse> registerStudent(@Body Student student);

                // Get student details
                @GET("/get_student_details")
                Call<List<FetchStudentResponse.Student>> getStudentDetails(@Body Student faculty);

                @GET("admin/unscannedStudentsAPI")
                Call<AbsentListResponse> getUnScannedStudents(@Query("faculty_id") int facultyId,
                                                              @Query("course_id") int courseId);

        @GET("admin/present_studnets")
        Call<AbsentListResponse> getScannedStudents(@Query("faculty_id") int facultyId,
                                                      @Query("course_id") int courseId);




}








