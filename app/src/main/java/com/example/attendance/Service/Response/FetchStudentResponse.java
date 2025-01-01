package com.example.attendance.Service.Response;

import java.util.List;

public class FetchStudentResponse {
    private String message;
    private List<Student> users;

    public String getMessage() {
        return message;
    }

    public List<Student> getStudents() {
        return users;
    }


    public static class Student {
        private int user_id;
        private String register_number;
        private String Student_name;
        private String Mobile_contact;
        private String Email;
        private String Date_of_birth;
        private String Address;
        private String pincode;
        private String Gender;
        private String Username;
        private String Password;


        // Getters for each field
        public int getId() {
            return user_id;
        }
        public String getRegister_number() {
            return register_number;
        }
        public String getStudent_name() {
            return Student_name;
        }
        public String getMobile_contact() {
            return Mobile_contact;
        }
        public String getEmail() { return Email;
        }
        public String getDate_of_birth() {
            return Date_of_birth;
        }
        public String getGender() {
            return Gender;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String password) {
            Password = password;
        }

        public String getUsername() {
            return Username;
        }
    }
}

