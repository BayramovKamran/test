
package org.example.service;

import org.example.dao.StudentDao;
import org.example.model.Student;
import java.util.List;

public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public void createStudent(Student student) {
        if (student.getName() == null || student.getName().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }
        studentDao.createStudent(student);
    }

    public Student getStudent(int id) {
        Student student = studentDao.getStudent(id);
        if (student == null) {
            throw new NullPointerException("Student with ID " + id + " not found.");
        }
        return student;
    }

    public List<Student> getAllStudent() {
        return studentDao.getAllStudent();
    }

    public void updateStudent(Student student) {
        studentDao.updateStudent(student);
    }

    public void deleteStudent(int id) {
        if (studentDao.getStudent(id) == null) {
            throw new RuntimeException("Student with ID " + id + " not found.");
        }
        studentDao.deleteStudent(id);
    }
}
