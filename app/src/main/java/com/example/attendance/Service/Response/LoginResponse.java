package com.example.attendance.Service.Response;


public class LoginResponse {
    private String status;
    private String message;
    private Admin admin; // Matches the "data" object in JSON

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Admin getAdmin() {
        return admin;
    }

    public static class Admin {
        private int user_id;
        private String username;
        private String email;
        private String register_number;
        private int User_type;

        public int getUser_id() {
            return user_id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getRegister_number() {
            return register_number;
        }

        public int getUser_type() {
            return User_type;
        }
    }
}

