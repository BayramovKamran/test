import org.example.dao.StudentDao;
import org.example.dao.StudentDaoImpl;
import org.example.model.Student;
import org.example.util.Database;
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
        connection = Database.getH2Connection(); // Получаем соединение с H2
        studentDao = new StudentDaoImpl(connection);
        clearDatabase();
    }

    @Test
    void testCreateStudent() {
        Student student = new Student();
        student.setName("Test Student");
        studentDao.createStudent(student);

        Student fetchedStudent = studentDao.getStudent(student.getId());
        Assertions.assertNotNull(fetchedStudent);
        Assertions.assertEquals("Test Student", fetchedStudent.getName());
    }

    @Test
    void testReadAllStudents() {
        Student student1 = new Student();
        student1.setName("Student One");
        studentDao.createStudent(student1);

        Student student2 = new Student();
        student2.setName("Student Two");
        studentDao.createStudent(student2);

        List<Student> students = studentDao.getAllStudent();
        Assertions.assertEquals(2, students.size());
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student();
        student.setName("Old Name");
        studentDao.createStudent(student);

        student.setName("New Name");
        studentDao.updateStudent(student);

        Student updatedStudent = studentDao.getStudent(student.getId());
        Assertions.assertEquals("New Name", updatedStudent.getName());
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student();
        student.setName("Test Student");
        studentDao.createStudent(student);

        studentDao.deleteStudent(student.getId());
        Assertions.assertNull(studentDao.getStudent(student.getId()));
    }

    // Метод для очистки базы данных
    private void clearDatabase() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM students");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }
}
