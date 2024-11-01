package org.example.dao;

import org.example.model.Student;

import java.util.List;

public interface StudentDao {

    void createStudent(Student student);

    Student getStudentById(int id);

    List<Student> getAllStudents();

    void updateStudent(Student student);

    void deleteStudent(int id);
}
