package org.example.model;

public class Student {

    private int id;
    private String studentName;

    public Student() {
    }
    public Student(int id, String name) {
        this.id = id;
        this.studentName = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String name) {
        this.studentName = name;
    }
}
