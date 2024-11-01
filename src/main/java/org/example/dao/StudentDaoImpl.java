package org.example.dao;

import org.example.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDaoImpl implements StudentDao {

    private static final Logger logger = Logger.getLogger(StudentDaoImpl.class.getName());
    private final Connection connection;

    public StudentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createStudent(Student student) {
        String sql = "INSERT INTO students (name) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, student.getName());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                student.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating student", e);
        }
    }

    @Override
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        Student student = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                student = new Student(rs.getInt("id"), rs.getString("name"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching student by ID: " + id, e);
        }
        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching all students", e);
        }
        return students;
    }

    @Override
    public void updateStudent(Student student) {
        String sql = "UPDATE students SET name = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating student with ID: " + student.getId(), e);
        }
    }

    @Override
    public void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting student with ID: " + id, e);
        }
    }
}
