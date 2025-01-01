package com.example.attendance.Student;

import java.util.List;

public class StudentDetailsResponse {

    private String message;

    private List<Users> users;

    public String getMessage() {
        return message;
    }

    public List<Users> getUsers() {
        return users;
    }

    public class Users {
        private int user_id;
        private String register_number;
        private String password;
        private String Student_name;
        private String Mobile_contact;
        private String Email;
        private String Address;
        private String pincode;
        private String Date_of_birth;
        private String Gender;
        private String User_type;
        private String username;

        // Getters
        public int getUser_id() {
            return user_id;
        }

        public String getRegister_number() {
            return register_number;
        }

        public String getPassword() {
            return password;
        }

        public String getStudent_name() {
            return Student_name;
        }

        public String getMobile_contact() {
            return Mobile_contact;
        }

        public String getEmail() {
            return Email;
        }

        public String getAddress() {
            return Address;
        }

        public String getPincode() {
            return pincode;
        }

        public String getDate_of_birth() {
            return Date_of_birth;
        }

        public String getGender() {
            return Gender;
        }

        public String getUser_type() {
            return User_type;
        }

        public String getUsername() {
            return username;
        }
    }
}
