package org.example.servlet;

import org.example.dao.StudentDao;
import org.example.dao.StudentDaoImpl;
import org.example.model.Student;
import org.example.util.Database;

import javax.servlet.ServletException;
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

    public StudentServlet(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public void init() throws ServletException {
        Connection connection; // Получите соединение
        try {
            connection = Database.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        studentDao = new StudentDaoImpl(connection);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        Student student = new Student();
        student.setName(name);
        studentDao.createStudent(student);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write("Student created with ID: " + student.getId());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Student> students = studentDao.getAllStudent();
        response.setContentType("application/json");
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

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("id")); // Получаем ID студента
        String name = request.getParameter("name"); // Получаем новое имя студента
        Student student = new Student(); // Создаём объект Student
        student.setId(studentId);
        student.setName(name);
        studentDao.updateStudent(student); // Обновляем данные в базе
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Student updated with ID: " + student.getId());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentId = Integer.parseInt(request.getParameter("id")); // Получаем ID студента
        studentDao.deleteStudent(studentId); // Удаляем студента из базы
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Student deleted with ID: " + studentId);
    }
}
