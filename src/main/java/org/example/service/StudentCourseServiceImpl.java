package org.example.service;

import org.example.dao.CourseDao;
import org.example.dao.StudentCourseDao;
import org.example.dao.StudentDao;

import java.util.List;

public class StudentCourseServiceImpl implements StudentCourseService {

    private final StudentCourseDao studentCourseDao;
    private final StudentDao studentDao;
    private final CourseDao courseDao;

    public StudentCourseServiceImpl(StudentCourseDao studentCourseDao, StudentDao studentDao, CourseDao courseDao) {
        this.studentCourseDao = studentCourseDao;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    public void enrollStudentInCourse(int studentId, int courseId) {
        if (studentDao.getStudentById(studentId) != null && courseDao.getCourseById(courseId) != null) {
            studentCourseDao.enrollStudentInCourse(studentId, courseId);
        } else {
            throw new IllegalArgumentException("Student or Course not found.");
        }
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        studentCourseDao.removeStudentFromCourse(studentId, courseId);
    }

    public List<Integer> getCoursesForStudent(int studentId) {
        return studentCourseDao.getCoursesForStudent(studentId);
    }
}