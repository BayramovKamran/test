package org.example.service;

import java.util.List;

public interface StudentCourseService {

    void enrollStudentInCourse(int studentId, int courseId);

    void removeStudentFromCourse(int studentId, int courseId);

    List<Integer> getCoursesForStudent(int studentId);
}

