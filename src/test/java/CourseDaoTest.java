import org.example.dao.CourseDao;
import org.example.dao.CourseDaoImpl;
import org.example.model.Course;
import org.example.util.Database;
import org.h2.tools.Server;
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
        Server.createTcpServer().start();
        connection = Database.getH2Connection(); // Подключение к H2
        courseDao = new CourseDaoImpl(connection);
        initializeDatabase();
    }

    private void initializeDatabase() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE courses (id INT AUTO_INCREMENT PRIMARY KEY, course_name VARCHAR(100) NOT NULL);");
        }
    }

    @Test
    void testCreateCourse() {
        Course course = new Course();
        course.setCourseName("Test Course");
        courseDao.createCourse(course);
        Course fetchedCourse = courseDao.getCourseById(course.getId());
        Assertions.assertNotNull(fetchedCourse);
        Assertions.assertEquals("Test Course", fetchedCourse.getCourseName());
    }

    @Test
    void testGetCourseById() {
        Course course = new Course();
        course.setCourseName("Java Programming");
        courseDao.createCourse(course);
        Course fetchedCourse = courseDao.getCourseById(course.getId());
        Assertions.assertNotNull(fetchedCourse);
        Assertions.assertEquals("Java Programming", fetchedCourse.getCourseName());
    }

    @Test
    void testGetAllCourses() {
        Course course1 = new Course();
        course1.setCourseName("Course One");
        courseDao.createCourse(course1);
        Course course2 = new Course();
        course2.setCourseName("Course Two");
        courseDao.createCourse(course2);
        List<Course> courses = courseDao.getAllCourses();
        Assertions.assertEquals(2, courses.size());
    }

    @Test
    void testUpdateCourse() {
        Course course = new Course();
        course.setCourseName("Old Course Name");
        courseDao.createCourse(course);
        course.setCourseName("New Course Name");
        courseDao.updateCourse(course);
        Course updatedCourse = courseDao.getCourseById(course.getId());
        Assertions.assertEquals("New Course Name", updatedCourse.getCourseName());
    }

    @Test
    void testDeleteCourse() {
        Course course = new Course();
        course.setCourseName("Test Course");
        courseDao.createCourse(course);
        courseDao.deleteCourse(course.getId());
        Assertions.assertNull(courseDao.getCourseById(course.getId()));
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }
}
