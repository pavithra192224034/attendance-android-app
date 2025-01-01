package com.example.attendance;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance.Service.Response.CreateCourseResponse;
import com.example.attendance.Service.Response.ErrorResponse;
import com.example.attendance.Service.RestClient;
import com.example.attendance.Service.SF;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCourse extends AppCompatActivity {

    private ImageView imageView;
    private EditText editCourseName, editCourseCode;
    private Button pickImageButton, buttonLaunchCourse;
    private Uri imageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    if (imageUri != null) {
                        imageView.setImageURI(imageUri);
                    } else {
                        Toast.makeText(CreateCourse.this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        // Initialize views
        imageView = findViewById(R.id.pickimage);
        editCourseName = findViewById(R.id.etCourseName);
        editCourseCode = findViewById(R.id.etcoursecode);
        pickImageButton = findViewById(R.id.pickimagebutton);
        buttonLaunchCourse = findViewById(R.id.btnSave);

        // Set listeners
        pickImageButton.setOnClickListener(v -> pickImageFromGallery());
        buttonLaunchCourse.setOnClickListener(v -> launchCourse());
    }

    // Function to start the image picker
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    // Function to handle course creation
    private void launchCourse() {
        String courseName = editCourseName.getText().toString().trim();
        String courseCode = editCourseCode.getText().toString().trim();

        // Validate inputs
        if (courseName.isEmpty() || courseCode.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Fill all the fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call the API with validated inputs
        File imageFile = new File(getPathFromUri(imageUri));
        apicall(imageFile, courseCode, courseName);
    }

    @Nullable
    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }

    private void apicall(File imageFile, String courseCode, String courseName) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("course_image", imageFile.getName(), requestBody);

        RequestBody courseNamePart = RequestBody.create(MediaType.parse("text/plain"), courseName);
        RequestBody courseCodePart = RequestBody.create(MediaType.parse("text/plain"), courseCode);
        RequestBody facultyId = RequestBody.create(MediaType.parse("text/plain"), "" + SF.getLoginSF(this).getInt(SF.LOGIN_USER_ID, 0));

        Call<CreateCourseResponse> call = RestClient.makeAPI().uploadCourse(courseNamePart, courseCodePart, facultyId, filePart);
        call.enqueue(new Callback<CreateCourseResponse>() {
            @Override
            public void onResponse(Call<CreateCourseResponse> call, Response<CreateCourseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreateCourse.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(), ErrorResponse.class);
                        Toast.makeText(CreateCourse.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(CreateCourse.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("CreateCourse", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateCourseResponse> call, Throwable t) {
                Toast.makeText(CreateCourse.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
