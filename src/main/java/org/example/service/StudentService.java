package org.example.service;

import org.example.model.Student;

import java.util.List;

public interface StudentService {

    void createStudent(Student student);

    Student getStudentById(int id);

    List<Student> getAllStudent();

    void updateStudent(Student student);

    void deleteStudent(int id);
}

