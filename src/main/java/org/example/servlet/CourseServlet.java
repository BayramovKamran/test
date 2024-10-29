package org.example.servlet;

import org.example.dao.CourseDao;
import org.example.dao.CourseDaoImpl;
import org.example.model.Course;
import org.example.util.Database;

import javax.servlet.ServletException;
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

    @Override
    public void init() {
        try {
            Connection connection = Database.getConnection(); // Проверьте, что этот метод подключается к вашей БД
            courseDao = new CourseDaoImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String courseName = request.getParameter("courseName");
        if (courseName == null || courseName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course name is required");
            return;
        }

        Course course = new Course();
        course.setCourseName(courseName);
        courseDao.createCourse(course);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write("Course created with ID: " + course.getId());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Course> courses = courseDao.getAllCourse();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.write("[");
        for (int i = 0; i < courses.size(); i++) {
            out.write("{\"id\":" + courses.get(i).getId() + ",\"courseName\":\"" + courses.get(i).getCourseName() + "\"}");
            if (i < courses.size() - 1) {
                out.write(",");
            }
        }
        out.write("]");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int courseId;
        try {
            courseId = Integer.parseInt(request.getParameter("id")); // Получаем ID курса
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid course ID");
            return;
        }

        String courseName = request.getParameter("courseName");
        if (courseName == null || courseName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course name is required");
            return;
        }

        Course course = new Course(); // Создаём объект Course
        course.setId(courseId);
        course.setCourseName(courseName);
        courseDao.updateCourse(course); // Обновляем данные в базе
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Course updated with ID: " + course.getId());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int courseId;
        try {
            courseId = Integer.parseInt(request.getParameter("id")); // Получаем ID курса
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid course ID");
            return;
        }

        courseDao.deleteCourse(courseId); // Удаляем курс из базы
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Course deleted with ID: " + courseId);
    }
}
