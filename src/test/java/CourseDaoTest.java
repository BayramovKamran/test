import org.example.dao.CourseDao;
import org.example.dao.CourseDaoImpl;
import org.example.model.Course;
import org.example.util.Database;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

class CourseDaoTest {

    private Connection connection;
    private CourseDao courseDao;

    @BeforeEach
    void setUp() throws SQLException {
        connection = Database.getH2Connection(); // Получаем соединение с H2
        courseDao = new CourseDaoImpl(connection);
        clearDatabase();
    }

    @Test
    void testCreateCourse() {
        Course course = new Course();
        course.setCourseName("Test Course");
        courseDao.createCourse(course);

        Course fetchedCourse = courseDao.getCourse(course.getId());
        Assertions.assertNotNull(fetchedCourse);
        Assertions.assertEquals("Test Course", fetchedCourse.getCourseName());
    }

    @Test
    void testReadAllCourses() {
        Course course1 = new Course();
        course1.setCourseName("Course One");
        courseDao.createCourse(course1);

        Course course2 = new Course();
        course2.setCourseName("Course Two");
        courseDao.createCourse(course2);

        List<Course> courses = courseDao.getAllCourse();
        Assertions.assertEquals(2, courses.size());
    }

    @Test
    void testUpdateCourse() {
        Course course = new Course();
        course.setCourseName("Old Course Name");
        courseDao.createCourse(course);

        course.setCourseName("New Course Name");
        courseDao.updateCourse(course);

        Course updatedCourse = courseDao.getCourse(course.getId());
        Assertions.assertEquals("New Course Name", updatedCourse.getCourseName());
    }

    @Test
    void testDeleteCourse() {
        Course course = new Course();
        course.setCourseName("Test Course");
        courseDao.createCourse(course);

        courseDao.deleteCourse(course.getId());
        Assertions.assertNull(courseDao.getCourse(course.getId()));
    }

    // Метод для очистки базы данных
    private void clearDatabase() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM courses");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }
}
