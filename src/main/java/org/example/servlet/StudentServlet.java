package org.example.servlet;

import com.google.gson.Gson;
import org.example.dao.StudentDao;
import org.example.dao.StudentDaoImpl;
import org.example.model.Student;
import org.example.util.Database;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/students")
public class StudentServlet extends HttpServlet {

    private StudentDao studentDao;
    Gson gson = new Gson();

    @Override
    public void init() {
        try {
            Connection connection = Database.getConnection();
            studentDao = new StudentDaoImpl(connection);
            gson = new Gson();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to initialize CourseDao due to a database error: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        if (name == null || name.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Student name cannot be null or empty\"}");
            return;
        }
        Student student = new Student();
        student.setName(name);
        studentDao.createStudent(student);
        response.setStatus(HttpServletResponse.SC_CREATED);
        out.write("{\"message\":\"Student created with ID: " + student.getId() + "\"}");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        response.setContentType("application/json");
        if (idParam != null) {
            int studentId = Integer.parseInt(idParam);
            Student student = studentDao.getStudentById(studentId);
            PrintWriter out = response.getWriter();
            if (student != null) {
                out.write("{\"id\":" + student.getId() + ",\"name\":\"" + student.getName() + "\"}");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"error\":\"Student not found\"}");
            }
        } else {
            List<Student> students = studentDao.getAllStudents();
            PrintWriter out = response.getWriter();
            out.write("[");
            for (int i = 0; i < students.size(); i++) {
                out.write("{\"id\":" + students.get(i).getId() + ",\"name\":\"" + students.get(i).getName() + "\"}");
                if (i < students.size() - 1) {
                    out.write(",");
                }
            }
            out.write("]");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String idParam = request.getParameter("id");
        String name = request.getParameter("name");
        if (idParam == null || name == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Student ID and name cannot be null\"}");
            return;
        }
        try {
            int studentId = Integer.parseInt(idParam);
            Student student = new Student();
            student.setId(studentId);
            student.setName(name);
            studentDao.updateStudent(student);
            response.setStatus(HttpServletResponse.SC_OK);
            out.write("{\"message\":\"Student updated with ID: " + student.getId() + "\"}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Invalid student ID\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Student ID cannot be null\"}");
            return;
        }
        try {
            int studentId = Integer.parseInt(idParam);
            studentDao.deleteStudent(studentId);
            response.setStatus(HttpServletResponse.SC_OK);
            out.write("{\"message\":\"Student deleted with ID: " + studentId + "\"}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Invalid student ID\"}");
        }
    }
}
