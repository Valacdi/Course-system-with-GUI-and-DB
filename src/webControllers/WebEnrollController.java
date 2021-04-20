package webControllers;

import com.google.gson.Gson;
import model.Administrator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DbOperations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

@Controller
@RequestMapping(value = "/enroll")
public class WebEnrollController {

    //READ
    @RequestMapping(value = "/getAllEnrolled", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllEnrolled() {
        ArrayList<String> allEnrolled = new ArrayList<>();
        Gson parser = new Gson();
        try {
            allEnrolled = DbOperations.getAllEnrolled();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(allEnrolled);
    }

    //INSERT
    @RequestMapping(value = "/insertStudentToEnroll", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertAdmin(@RequestBody String request) throws SQLException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int studentId = Integer.parseInt(data.getProperty("studentId"));
        int courseIs = Integer.parseInt(data.getProperty("courseIs"));
        try {
            DbOperations.insertStudentToEnroll(studentId, courseIs);
            return parser.toJson(DbOperations.getStudentById(studentId));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    //UPDATE
    @RequestMapping(value = "/updEnrollment", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateAdmin(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int id = Integer.parseInt(data.getProperty("id"));
        int studentId = Integer.parseInt(data.getProperty("studentId"));
        int courseIs = Integer.parseInt(data.getProperty("courseIs"));
        try {
            DbOperations.updateEnrollment(id, studentId, courseIs);
            return parser.toJson(DbOperations.getStudentById(studentId));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }

    //DELETE
    @RequestMapping(value = "/delEnrollmentById", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCourseId(@RequestParam("id") int id) {
        try {
            DbOperations.deleteEnrollment(id);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }
}
