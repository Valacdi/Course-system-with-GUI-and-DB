package utils;

import fxControl.Login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DbOperations {

    private static Connection connection;
    private static PreparedStatement statement;

    public static Connection connectToDb() {

        String DB_URL = "jdbc:mysql://localhost/lab2";
        String USER = "root";
        String PASS = "";
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void disconnectFromDb(Connection connection, Statement statement) {
        try {
            if (connection != null && statement != null) {
                connection.close();
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Course> getAllCoursesFromDb(int courseIsId) throws SQLException {
        ArrayList<Course> allCourses = new ArrayList<>();
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM `course` AS c WHERE c.course_is = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, courseIsId);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            allCourses.add(new Course(rs.getInt(1), rs.getString(2), LocalDate.parse(rs.getString(3)), LocalDate.parse(rs.getString(4)), rs.getDouble(6)));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return allCourses;
    }

    public static void insertCourse(String name, LocalDate startDate, LocalDate endDate, int adminId, double price, int courseIs) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "INSERT INTO `course`(`course_name`, `start_date`, `end_date`, `admin_id`, `course_price`, `course_is`) VALUES (?, ?, ?, ?, ?, ?)";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setDate(2, Date.valueOf(startDate));
        statement.setDate(3, Date.valueOf(endDate));
        statement.setInt(4, adminId);
        statement.setDouble(5, price);
        statement.setInt(6, courseIs);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static Course getCourseByName(String name) throws SQLException {
        Course course = null;
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM course AS c WHERE c.name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            course = new Course(rs.getInt(1), rs.getString(2), LocalDate.parse(rs.getString(3)), LocalDate.parse(rs.getString(4)), rs.getDouble(6));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return course;
    }

    public static void updateCourse(int id, String colName, Double newValue) throws SQLException {
        if (newValue != 0) {
            connection = DbOperations.connectToDb();
            String sql = "UPDATE course SET `" + colName + "`  = ? WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setDouble(1, newValue);
            statement.setInt(2, id);
            statement.executeUpdate();
            DbOperations.disconnectFromDb(connection, statement);
        }
    }

    public static void updateCourse(int id, String colName, String newValue) throws SQLException {
        if (!newValue.equals("")) {
            connection = DbOperations.connectToDb();
            String sql = "UPDATE `course` SET `" + colName + "`  = ? WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, newValue);
            statement.setInt(2, id);
            statement.executeUpdate();
            DbOperations.disconnectFromDb(connection, statement);
        }
    }

    public static void updateCourse(int id, String colName, LocalDate newValue) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "UPDATE `course` SET `" + colName + "`  = ? WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setDate(1, Date.valueOf(newValue));
        statement.setInt(2, id);
        statement.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteCourse(int id) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM `course` WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteDbRecord(String name) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM `course` WHERE name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static ArrayList<Administrator> getAllAdmins() throws SQLException {
        connection = DbOperations.connectToDb();
        ArrayList<Administrator> loadAdmins = new ArrayList<>();
        statement = connection.prepareStatement("SELECT * FROM admins");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            loadAdmins.add(new Administrator(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return loadAdmins;
    }

    public static void insertAdmin(String login, String password, String email, int phoneNumber, int courseIs) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "INSERT INTO `admins`(`login`, `password`, `email`, `phone_number`, `course_is`) VALUES (?, ?, ?, ?, ?)";
        statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        statement.setString(2, password);
        statement.setString(3, email);
        statement.setInt(4, phoneNumber);
        statement.setInt(5, courseIs);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static Administrator getAdminByLogin(String login) throws SQLException {
        Administrator administrator = null;
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM admins AS a WHERE a.login = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            administrator = new Administrator(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return administrator;
    }

    public static void updateAdmin(int adminId, String login, String password, String email, int phoneNumber, int courseIs) throws SQLException {
        connection = DbOperations.connectToDb();
        statement = connection.prepareStatement("UPDATE admins SET login = ?, password = ?, email = ?, phone_number = ?, course_is = ? WHERE id = ?");
        statement.setInt(6, adminId);
        statement.setString(1, login);
        statement.setString(2, password);
        statement.setString(3, email);
        statement.setInt(4, phoneNumber);
        statement.setInt(5, courseIs);
        statement.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteAdmin(int id) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM `course` WHERE name = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static ArrayList<CourseIS> getAllCoursesByIS() throws SQLException {
        ArrayList<CourseIS> allCoursesByIS = new ArrayList<>();
        connection = DbOperations.connectToDb();
        statement = connection.prepareStatement("SELECT * FROM course_is");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            allCoursesByIS.add(new CourseIS(rs.getString(2), LocalDate.parse(rs.getString(3)), rs.getString(4)));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return allCoursesByIS;
    }

    public static void insertCourseIS(String name, LocalDate dateCreated, String version) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "INSERT INTO `course_is`(`name`, `date_created`, `version`) VALUES (?, ?, ?)";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setDate(2, Date.valueOf(dateCreated));
        statement.setString(3, version);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static CourseIS getCourseISByName(String name) throws SQLException {
        CourseIS courseIS = null;
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM course_is AS c WHERE c.name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            courseIS = new CourseIS(rs.getString(2), LocalDate.parse(rs.getString(3)), rs.getString(4));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return courseIS;
    }

    public static void updateCourseIS(int courseISId, String name, LocalDate dateCreated, String version) throws SQLException {
        connection = DbOperations.connectToDb();
        statement = connection.prepareStatement("UPDATE course_is SET name = ?, date_created = ?, email = ? WHERE id = ?");
        statement.setInt(4, courseISId);
        statement.setString(1, name);
        statement.setDate(2, Date.valueOf(dateCreated));
        statement.setString(3, version);
        statement.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteCourseIS(int id) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM `course_is` WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static ArrayList<Student> getAllStudents() throws SQLException {
        ArrayList<Student> allStudents = new ArrayList<>();
        connection = DbOperations.connectToDb();
        statement = connection.prepareStatement("SELECT * FROM students");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            allStudents.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8)));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return allStudents;
    }

    public static void insertStudent(String login, String password, String email, String studentName, String surname, String accNumber, int courseIs) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "INSERT INTO `students`(`login`, `password`, `email`, `student_name`, `surname`, `acc_number`, `course_is`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        statement.setString(2, password);
        statement.setString(3, email);
        statement.setString(4, studentName);
        statement.setString(5, surname);
        statement.setString(6, accNumber);
        statement.setInt(7, courseIs);
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static Student getStudentByName(String login) throws SQLException {
        Student student = null;
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM students AS s WHERE s.login = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            student = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getInt(8));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return student;
    }

    public static void updateStudent(int studentId, String login, String password, String email, String studentName, String surname, String accNumber, int courseIs) throws SQLException {
        connection = DbOperations.connectToDb();
        statement = connection.prepareStatement("UPDATE students SET login = ?, password = ?, email = ?, student_name = ?, surname = ?, acc_number = ?, course_is = ? WHERE id = ?");
        statement.setInt(8, studentId);
        statement.setString(1, login);
        statement.setString(2, password);
        statement.setString(3, email);
        statement.setString(4, studentName);
        statement.setString(5, surname);
        statement.setString(6, accNumber);
        statement.setInt(7, courseIs);
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteStudent(int id) throws SQLException {
        connection = DbOperations.connectToDb();
        ObservableList<String> elements;
        String sql = "DELETE FROM students WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static ArrayList<Folder> getAllFolders() throws SQLException {
        ArrayList<Folder> allFolders = new ArrayList<>();
        connection = DbOperations.connectToDb();
        statement = connection.prepareStatement("SELECT * FROM folder");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            allFolders.add(new Folder(rs.getString(2), rs.getDate(3)));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return allFolders;
    }

    public static Folder getFolderByName(String folderName) throws SQLException {
        Folder folder = null;
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM folder AS f WHERE f.folder_name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, folderName);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            folder = new Folder(rs.getString(2), rs.getDate(3));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return folder;
    }

    public static void insertFolder(String folderName, LocalDate dateModified, int courseId) throws SQLException {
        connection = DbOperations.connectToDb();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO folder(folder_name, date_modified, course_id) VALUES (?, ?, ?)");
        stmt.setString(1, folderName);
        stmt.setDate(2, Date.valueOf(dateModified));
        stmt.setInt(3, courseId);
        stmt.executeUpdate();
        statement.execute();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void updateFolder(int folderId, String folderName, LocalDate dateModified, int courseId) throws SQLException {
        connection = DbOperations.connectToDb();
        statement = connection.prepareStatement("UPDATE folder SET folder_name = ?, date_modified = ?, course_id = ? WHERE id = ?");
        statement.setInt(4, folderId);
        statement.setString(1, folderName);
        statement.setDate(2, Date.valueOf(dateModified));
        statement.setInt(3, courseId);
        statement.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteFolder(int id) throws SQLException {
        connection = DbOperations.connectToDb();
        ObservableList<String> elements;
        String sql = "DELETE FROM folder WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static ArrayList<File> getAllFiles() throws SQLException {
        connection = DbOperations.connectToDb();
        ArrayList<File> allFiles= new ArrayList<>();
        connection = DbOperations.connectToDb();
        statement = connection.prepareStatement("SELECT * FROM folder_files");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            allFiles.add(new File(rs.getString(2), rs.getDate(3), rs.getString(4)));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return allFiles;
    }

    public static void insertFile(String fileName, LocalDate dateModified, String filePath, int folderId) throws SQLException {
        connection = DbOperations.connectToDb();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO folder_files(file_name, date_added, file_path, folder_id) VALUES (?, ?, ?, ?)");
        stmt.setString(1, fileName);
        stmt.setDate(2, Date.valueOf(dateModified));
        stmt.setString(3, filePath);
        stmt.setInt(4, folderId);
        stmt.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static File getFileByName(String fileName) throws SQLException {
        File file = null;
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM folder_files AS f WHERE f.file_name = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, fileName);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            file = new File(rs.getString(2), rs.getDate(3), rs.getString(4));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return file;
    }

    public static void updateFile(int fileId, String fileName, LocalDate dateModified, String filePath, int folderId) throws SQLException {
        connection = DbOperations.connectToDb();
        statement = connection.prepareStatement("UPDATE folder_files SET file_name = ?, date_added = ?, file_path = ?, folder_id = ? WHERE id = ?");
        statement.setInt(4, fileId);
        statement.setString(1, fileName);
        statement.setDate(2, Date.valueOf(dateModified));
        statement.setString(3, filePath);
        statement.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteFile(int id) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM folder_files WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static Student getStudent(String loginName, String password, int courseIs) throws SQLException {
        Student student = null;
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM admins AS a WHERE a.login = ? AND a.psw = ? AND a.course_is = ? AND a.phone_number is not NULL";
        statement = connection.prepareStatement(sql);
        statement.setString(1, loginName);
        statement.setString(2, password);
        statement.setInt(3, courseIs);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            student = new Student(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return student;
    }

    public static Administrator getAdmin(String loginName, String password, int courseIs) throws SQLException {
        Administrator administrator = null;
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM admins AS a WHERE a.login = ? AND a.password = ? AND a.course_is = ? AND a.phone_number is not NULL";
        statement = connection.prepareStatement(sql);
        statement.setString(1, loginName);
        statement.setString(2, password);
        statement.setInt(3, courseIs);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            administrator = new Administrator(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return administrator;
    }

    public static ArrayList<String> getAllEnrolled() throws SQLException {
        connection = DbOperations.connectToDb();
        ArrayList<String> allFiles= new ArrayList<>();
        connection = DbOperations.connectToDb();
        statement = connection.prepareStatement("SELECT * FROM folder_files");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            allFiles.add(rs.getString(1) + ". STUDENT ID: " + rs.getString(2) + ", COURSE ID: " + rs.getString(3));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return allFiles;
    }

    public static void insertStudentToEnroll(int studentId, int courseIs) throws SQLException {
        connection = DbOperations.connectToDb();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO student_enroll_course(student_id, course_id) VALUES (?, ?)");
        stmt.setInt(1, studentId);
        stmt.setInt(2, courseIs);
        stmt.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static ArrayList<String> getStudentById(int studentId) throws SQLException {
        connection = DbOperations.connectToDb();
        ArrayList<String> allEnrolled= new ArrayList<>();
        connection = DbOperations.connectToDb();
        statement = connection.prepareStatement("SELECT * FROM student_enroll_course WHERE id = ?");
        statement.setInt(1, studentId);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            allEnrolled.add(rs.getString(1) + ". STUDENT ID: " + rs.getString(2) + ", COURSE ID: " + rs.getString(3));
        }
        DbOperations.disconnectFromDb(connection, statement);
        return allEnrolled;
    }

    public static void updateEnrollment(int id, int studentId, int courseIs) throws SQLException {
        connection = DbOperations.connectToDb();
        statement = connection.prepareStatement("UPDATE student_enroll_course SET student_id = ?, course_id = ? WHERE id = ?");
        statement.setInt(3, id);
        statement.setInt(1, studentId);
        statement.setInt(2, courseIs);
        statement.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public static void deleteEnrollment(int id) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM student_enroll_course WHERE course_id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
    }
}
