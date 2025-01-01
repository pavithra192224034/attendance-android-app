package com.example.attendance.Service.Response;

import java.util.List;


public class FetchCourseResponse {

    private String message;
    private List<CourseDetails> data;

    public String getMessage() {
        return message;
    }

    public List<CourseDetails> getData() {
        return data;
    }

    public static class CourseDetails {
        private String course_name;
        private int course_id;

        public int getCourse_id() {
            return course_id;
        }

        private String course_code;
        private String image_url;
        private String Faculty_id;

        public String getCourse_name() {
            return course_name;
        }

        public String getCourse_code() {
            return course_code;
        }

        public String getImage_url() {
            return image_url;
        }

        public String getFaculty_id() {
            return Faculty_id;
        }
    }
}



