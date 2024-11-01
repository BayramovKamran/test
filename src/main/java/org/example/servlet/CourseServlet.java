package org.example.servlet;

import com.google.gson.Gson;
import org.example.dao.CourseDao;
import org.example.dao.CourseDaoImpl;
import org.example.model.Course;
import org.example.util.Database;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/courses")
public class CourseServlet extends HttpServlet {

    private CourseDao courseDao;
    Gson gson = new Gson();

    @Override
    public void init() {
        try {
            Connection connection = Database.getConnection();
            courseDao = new CourseDaoImpl(connection);
            gson = new Gson();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to initialize CourseDao due to a database error: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String courseName = request.getParameter("course_name");
        if (courseName == null || courseName.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Course name cannot be null or empty\"}");
            return;
        }
        Course course = new Course();
        course.setCourseName(courseName);
        courseDao.createCourse(course);
        response.setStatus(HttpServletResponse.SC_CREATED);
        out.write("{\"message\":\"Course created with ID: " + course.getId() + "\"}");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        response.setContentType("application/json");
        if (idParam != null) {
            int courseId = Integer.parseInt(idParam);
            Course course = courseDao.getCourseById(courseId);
            PrintWriter out = response.getWriter();
            if (course != null) {
                out.write("{\"id\":" + course.getId() + ",\"course_name\":\"" + course.getCourseName() + "\"}");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"error\":\"Course not found\"}");
            }
        } else {
            List<Course> courses = courseDao.getAllCourses();
            PrintWriter out = response.getWriter();
            out.write("[");
            for (int i = 0; i < courses.size(); i++) {
                out.write("{\"id\":" + courses.get(i).getId() + ",\"course_name\":\"" + courses.get(i).getCourseName() + "\"}");
                if (i < courses.size() - 1) {
                    out.write(",");
                }
            }
            out.write("] ");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String idParam = request.getParameter("id");
        String courseName = request.getParameter("course_name");
        if (idParam == null || courseName == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Course ID and name cannot be null\"}");
            return;
        }
        try {
            int courseId = Integer.parseInt(idParam);
            Course course = new Course();
            course.setId(courseId);
            course.setCourseName(courseName);
            courseDao.updateCourse(course);
            response.setStatus(HttpServletResponse.SC_OK);
            out.write("{\"message\":\"Course updated with ID: " + course.getId() + "\"}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Invalid course ID\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Course ID cannot be null\"}");
            return;
        }
        try {
            int courseId = Integer.parseInt(idParam);
            courseDao.deleteCourse(courseId);
            response.setStatus(HttpServletResponse.SC_OK);
            out.write("{\"message\":\"Course deleted with ID: " + courseId + "\"}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Invalid course ID\"}");
        }
    }
}

