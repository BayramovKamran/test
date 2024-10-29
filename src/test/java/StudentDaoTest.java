import org.example.dao.StudentDao;
import org.example.dao.StudentDaoImpl;
import org.example.model.Student;
import org.example.util.Database;
import org.h2.tools.Server;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

class StudentDaoTest {

    private Connection connection;
    private StudentDao studentDao;

    @BeforeEach
    void setUp() throws SQLException {
        Server.createTcpServer().start();
        connection = Database.getH2Connection(); // Пример метода подключения к H2
        studentDao = new StudentDaoImpl(connection);
        initializeDatabase();
    }

    private void initializeDatabase() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE students (id INT AUTO_INCREMENT PRIMARY KEY, studentName VARCHAR(100) NOT NULL);");
        }
    }

    @Test
    void testCreateStudent() {
        Student student = new Student();
        student.setStudentName("Test Student");
        studentDao.createStudent(student);
        Student fetchedStudent = studentDao.getStudentById(student.getId());
        Assertions.assertNotNull(fetchedStudent);
        Assertions.assertEquals("Test Student", fetchedStudent.getStudentName());
    }

    @Test
    void testGetStudentById() {
        Student student = new Student();
        student.setStudentName("John Doe");
        studentDao.createStudent(student);
        Student fetchedStudent = studentDao.getStudentById(student.getId());
        Assertions.assertNotNull(fetchedStudent);
        Assertions.assertEquals("John Doe", fetchedStudent.getStudentName());
    }


    @Test
    void testGetAllStudents() {
        Student student1 = new Student();
        student1.setStudentName("Student One");
        studentDao.createStudent(student1);
        Student student2 = new Student();
        student2.setStudentName("Student Two");
        studentDao.createStudent(student2);
        List<Student> students = studentDao.getAllStudent();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student();
        student.setStudentName("Old Name");
        studentDao.createStudent(student);
        student.setStudentName("New Name");
        studentDao.updateStudent(student);
        Student updatedStudent = studentDao.getStudentById(student.getId());
        Assertions.assertEquals("New Name", updatedStudent.getStudentName());
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student();
        student.setStudentName("Test Student");
        studentDao.createStudent(student);
        studentDao.deleteStudent(student.getId());
        Assertions.assertNull(studentDao.getStudentById(student.getId()));
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }
}
