package com.example.attendance.Service.Response;

import java.util.List;

public class AbsentListResponse {
    private boolean success;
    private String message;

    private List<Student> data;

    public List<Student> getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public class Stdent {
        private String register_number;
        private String student_name;
        private String Mobile_contact;
        private String Email;
        private String Gender;
        private int user_id;

        public String getRegister_number() {
            return register_number;
        }

        public String getStudent_name() {
            return student_name;
        }

        public String getMobile_contact() {
            return Mobile_contact;
        }

        public String getEmail() {
            return Email;
        }

        public String getGender() {
            return Gender;
        }

        public int getUser_id() {
            return user_id;
        }
    }
}
