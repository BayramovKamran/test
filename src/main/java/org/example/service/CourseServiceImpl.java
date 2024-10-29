
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
        if (course.getCourseName() == null || course.getCourseName().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty");
        }
        courseDao.createCourse(course);
    }

    @Override
    public Course getCourse(int id) {
        Course course = courseDao.getCourse(id);
        if (course == null) {
            throw new NullPointerException("Course with id " + id + " not found");
        }
        return course;
    }

    @Override
    public List<Course> getAllCourse() {
        return courseDao.getAllCourse();
    }

    @Override
    public void updateCourse(Course course) {
        courseDao.updateCourse(course);

    }

    @Override
    public void deleteCourse(int id) {
        courseDao.deleteCourse(id);

    }
}
