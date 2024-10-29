package org.example.service;

import org.example.dao.CourseDao;
import org.example.model.Course;

import java.util.List;

public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;

    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public void createCourse(Course course) {
        courseDao.createCourse(course);
    }

    public Course getCourse(int id) {
        return courseDao.getCourse(id);
    }

    public List<Course> getAllCourse() {
        return courseDao.getAllCourse();
    }

    public void updateCourse(Course course) {
        courseDao.updateCourse(course);
    }

    public void deleteCourse(int id) {
        courseDao.deleteCourse(id);
    }
}

