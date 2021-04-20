package webControllers;

import com.google.gson.Gson;
import model.Course;
import model.CourseIS;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DbOperations;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

@Controller
@RequestMapping(value = "/courseIS")
public class WebCourseISController {

    //READ
    @RequestMapping(value = "/getAllCourseIS", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllCourses() {
        ArrayList<CourseIS> allCoursesIS = new ArrayList<>();
        Gson parser = new Gson();
        try {
            allCoursesIS = DbOperations.getAllCoursesByIS();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(allCoursesIS);
    }

    //INSERT
    @RequestMapping(value = "/insertCourseIS", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertCourseIS(@RequestBody String request) throws SQLException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        LocalDate dateCreated = LocalDate.parse(data.getProperty("dateCreated"));
        String version = data.getProperty("version");
        try {
            DbOperations.insertCourseIS(name, dateCreated, version);
            return parser.toJson(DbOperations.getCourseISByName(name));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    //UPDATE
    @RequestMapping(value = "/updCourseIS", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCourseIS(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int courseISId = Integer.parseInt(data.getProperty("id"));
        String name = data.getProperty("name");
        LocalDate dateCreated = LocalDate.parse(data.getProperty("dateCreated"));
        String version = data.getProperty("version");
        try {
            DbOperations.updateCourseIS(courseISId, name, dateCreated, version);
            return parser.toJson(DbOperations.getCourseISByName(name));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }

    //DELETE
    @RequestMapping(value = "/delCourseISByID", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCourseId(@RequestParam("id") int id) {
        try {
            DbOperations.deleteCourseIS(id);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }
}
