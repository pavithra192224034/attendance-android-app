package com.example.attendance.Service.Response;


public class MyRequest {

    public static class SignupRequest {
        private String name;
        private String email;
        private String password;

        public SignupRequest(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }
    }
}