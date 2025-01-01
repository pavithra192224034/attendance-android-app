package com.example.attendance.Service.Response;

import com.google.gson.annotations.SerializedName;

public class CreateStudentResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("studentId")
    private int studentId;

    public String getMessage() {

        return message;
    }

    public int getStudentId() {

        return studentId;
    }
}



