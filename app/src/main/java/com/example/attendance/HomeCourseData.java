package com.example.attendance;


public class HomeCourseData {
    public static class Course {
        private String courseName;
        private String instructorName;
        private int imageResId;

        public Course(String courseName, String instructorName, int imageResId) {
            this.courseName = courseName;
            this.instructorName = instructorName;
            this.imageResId = imageResId;
        }

        public String getCourseName() {
            return courseName;
        }

        public String getInstructorName() {
            return instructorName;
        }

        public int getImageResId() {
            return imageResId;
        }
    }

}

