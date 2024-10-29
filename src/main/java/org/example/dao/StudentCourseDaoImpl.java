package org.example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentCourseDaoImpl implements StudentCourseDao {

    private final Connection connection;

    public StudentCourseDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {
        try {
            String sql = "INSERT INTO student_courses (student_id, course_id) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        try {
            String sql = "DELETE FROM student_courses WHERE student_id = ? AND course_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Integer> getCoursesForStudent(int studentId) {
        List<Integer> courses = new ArrayList<>();
        try {
            String sql = "SELECT course_id FROM student_courses WHERE student_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courses.add(resultSet.getInt("course_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
}
