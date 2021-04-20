package webControllers;

import com.google.gson.Gson;
import model.Administrator;
import model.CourseIS;
import model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DbOperations;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

@Controller
@RequestMapping(value = "/students")
public class WebStudentController {

    //READ
    @RequestMapping(value = "/getAllStudents", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllStudents() {
        ArrayList<Student> allStudents = new ArrayList<>();
        Gson parser = new Gson();
        try {
            allStudents = DbOperations.getAllStudents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(allStudents);
    }

    //INSERT
    @RequestMapping(value = "/insertStudent", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertStudent(@RequestBody String request) throws SQLException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String login = data.getProperty("login");
        String password = data.getProperty("password");
        String email = data.getProperty("email");
        String studentName = data.getProperty("student_name");
        String surname = data.getProperty("surname");
        String accNumber = data.getProperty("acc_number");
        int courseIs = Integer.parseInt(data.getProperty("course_is"));
        try {
            DbOperations.insertStudent(login, password, email, studentName, surname, accNumber, courseIs);
            return parser.toJson(DbOperations.getStudentByName(login));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    //UPDATE
    @RequestMapping(value = "/updStudent", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateStudent(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int studentId = Integer.parseInt(data.getProperty("id"));
        String login = data.getProperty("login");
        String password = data.getProperty("password");
        String email = data.getProperty("email");
        String studentName = data.getProperty("student_name");
        String surname = data.getProperty("surname");
        String accNumber = data.getProperty("acc_number");
        int courseIs = Integer.parseInt(data.getProperty("course_is"));
        try {
            DbOperations.updateStudent(studentId, login, password, email, studentName, surname, accNumber, courseIs);
            return parser.toJson(DbOperations.getStudentByName(login));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }

    //DELETE
    @RequestMapping(value = "/delStudent", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteStudentId(@RequestParam("id") int id) {
        try {
            DbOperations.deleteStudent(id);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }

    //Authorization
    @RequestMapping(value = "/studentLogin", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String loginStudent(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String loginName = data.getProperty("login");
        String password = data.getProperty("psw");
        int courseIs = Integer.parseInt(data.getProperty("courseIs"));
        Student student = null;
        try {
            student = DbOperations.getStudent(loginName, password, courseIs);
        } catch (Exception e) {
            return "Error";
        }
        if (student == null) {
            return "Wrong credentials";
        }
        return Integer.toString(student.getId());
    }
}
