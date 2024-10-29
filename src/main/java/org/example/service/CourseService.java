package org.example.service;

import org.example.model.Course;

import java.util.List;

public interface CourseService {

    void createCourse(Course course);

    Course getCourseById(int id);

    List<Course> getAllCourse();

    void updateCourse(Course course);

    void deleteCourse(int id);
}
