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
        connection = Database.getH2Connection();
        studentDao = new StudentDaoImpl(connection);
        initializeDatabase();
    }

    private void initializeDatabase() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE students (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL);");
        }
    }

    @Test
    void testCreateStudent() {
        Student student = new Student();
        student.setName("Test Student");
        studentDao.createStudent(student);
        Student fetchedStudent = studentDao.getStudentById(student.getId());
        Assertions.assertNotNull(fetchedStudent);
        Assertions.assertEquals("Test Student", fetchedStudent.getName());
    }

    @Test
    void testGetStudentById() {
        Student student = new Student();
        student.setName("John Doe");
        studentDao.createStudent(student);
        Student fetchedStudent = studentDao.getStudentById(student.getId());
        Assertions.assertNotNull(fetchedStudent);
        Assertions.assertEquals("John Doe", fetchedStudent.getName());
    }


    @Test
    void testGetAllStudents() {
        Student student1 = new Student();
        student1.setName("Student One");
        studentDao.createStudent(student1);
        Student student2 = new Student();
        student2.setName("Student Two");
        studentDao.createStudent(student2);
        List<Student> students = studentDao.getAllStudents();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student();
        student.setName("Old Name");
        studentDao.createStudent(student);
        student.setName("New Name");
        studentDao.updateStudent(student);
        Student updatedStudent = studentDao.getStudentById(student.getId());
        Assertions.assertEquals("New Name", updatedStudent.getName());
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student();
        student.setName("Test Student");
        studentDao.createStudent(student);
        studentDao.deleteStudent(student.getId());
        Assertions.assertNull(studentDao.getStudentById(student.getId()));
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }
}
