package com.example.attendance.Service.Response;


public class Response {

    public class LoginResponse {
        private String message;
        private Admin admin;

        public Admin getAdmin() {
            return admin;
        }

        public String getMessage() {
            return message;
        }

        public class Admin {
            private int user_id;
            private String register_number;
            private String Student_name;
            private String Mobile_contact;
            private String Email;
            private String Address;
            private int pincode;
            private String Date_of_birth;
            private String Gender;
            private int User_type;
            private String username;


            public int getUser_id() {
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

            public String getEmail() {
                return Email;
            }

            public String getAddress() {
                return Address;
            }

            public int getPincode() {
                return pincode;
            }

            public String getDate_of_birth() {
                return Date_of_birth;
            }

            public String getGender() {
                return Gender;
            }

            public int getUser_type() {
                return User_type;
            }

            public String getUsername() {
                return username;
            }
        }
    }
}