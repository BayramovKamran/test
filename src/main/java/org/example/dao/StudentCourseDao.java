package org.example.dao;

import java.util.List;

public interface StudentCourseDao {

    void enrollStudentInCourse(int studentId, int courseId);

    void removeStudentFromCourse(int studentId, int courseId);

    List<Integer> getCoursesForStudent(int studentId);
}
