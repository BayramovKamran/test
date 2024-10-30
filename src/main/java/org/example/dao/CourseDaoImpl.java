package org.example.dao;

import org.example.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl implements CourseDao {

    private final Connection connection;

    public CourseDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createCourse(Course course) {
        String sql = "INSERT INTO courses (course_name) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, course.getCourseName());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                course.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Course getCourseById(int id) {
        String sql = "SELECT * FROM courses WHERE id = ?";
        Course course = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                course = new Course(rs.getInt("id"), rs.getString("course_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                courses.add(new Course(rs.getInt("id"), rs.getString("course_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public void updateCourse(Course course) {
        String sql = "UPDATE courses SET course_name = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseName());
            pstmt.setInt(2, course.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCourse(int id) {
        String sql = "DELETE FROM courses WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
