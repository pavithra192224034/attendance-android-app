package com.example.attendance.Service.Response;

import com.google.gson.annotations.SerializedName;

public class ScannerResponse {


    @SerializedName("message")
    private String message;

    @SerializedName("studentId")
    private int studentId;

    // Constructor
    public void APIResponse(String message, int studentId) {
        this.message = message;
        this.studentId = studentId;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}

