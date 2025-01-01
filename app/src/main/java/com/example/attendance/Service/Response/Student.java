package com.example.attendance.Service.Response;


import com.google.gson.annotations.SerializedName;

public class Student {
    private String register_number;
    private String student_name;
    private int faculty_id;
    private int course_id;
    private int student_id;

    // Constructor
    public Student(String register_number, String student_name, int faculty_id, int course_id, int student_id) {
        this.register_number = register_number;
        this.student_name = student_name;
        this.faculty_id = faculty_id;
        this.course_id = course_id;
        this.student_id = student_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public String getRegister_number() {
        return register_number;
    }

    public int getCourse_id() {
        return course_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public int getFaculty_id() {
        return faculty_id;
    }


    public class Faculty {
        private int faculty_id;

        // Constructor
        public Faculty(int faculty_id) {
            this.faculty_id = faculty_id;
        }

        public int getFaculty_id() {
            return faculty_id;
        }

    }

    public class Students {

        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("rollNumber")
        private String rollNumber;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRollNumber() {
            return rollNumber;
        }

        public void setRollNumber(String rollNumber) {
            this.rollNumber = rollNumber;
        }


    }


}

