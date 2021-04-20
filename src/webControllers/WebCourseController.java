package webControllers;

import com.google.gson.Gson;
import model.Course;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DbOperations;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

@Controller
//.../courses
@RequestMapping(value = "/courses")
public class WebCourseController {

    //READ
    @RequestMapping(value = "/getAllCourses", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllCourses(@RequestParam("courseIs") int courseIs) {
        ArrayList<Course> allCourses = new ArrayList<>();
        Gson parser = new Gson();
        try {
            allCourses = DbOperations.getAllCoursesFromDb(courseIs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(allCourses);
    }

    //INSERT
    @RequestMapping(value = "/insertCourse", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertCourse(@RequestBody String request) throws SQLException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        LocalDate startDate = LocalDate.parse(data.getProperty("start"));
        LocalDate endDate = LocalDate.parse(data.getProperty("end"));
        int adminId = Integer.parseInt(data.getProperty("adminId"));
        int sysId = Integer.parseInt(data.getProperty("courseIs"));
        Double price = Double.parseDouble(data.getProperty("price"));
        try {
            DbOperations.insertCourse(name, startDate, endDate, adminId, price, sysId);
            return parser.toJson(DbOperations.getCourseByName(name));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    //UPDATE
    @RequestMapping(value = "/updCourse", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCourse(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int courseId = Integer.parseInt(data.getProperty("id"));
        String name = data.getProperty("name");
        Double price = Double.parseDouble(data.getProperty("price"));
        try {
            DbOperations.updateCourse(courseId, "name", name);
            if (!data.getProperty("start").equals(""))
                DbOperations.updateCourse(courseId, "start_date", LocalDate.parse(data.getProperty("start")));
            if (!data.getProperty("end").equals(""))
                DbOperations.updateCourse(courseId, "end_date", LocalDate.parse(data.getProperty("end")));
            DbOperations.updateCourse(courseId, "course_price", price);
            return parser.toJson(DbOperations.getCourseByName(name));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }

    //DELETE
    @RequestMapping(value = "/delCourseId", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCourseId(@RequestParam("id") int id) {
        try {
            DbOperations.deleteCourse(id);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }

    @RequestMapping(value = "/delCourseName", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCourseName(@RequestParam("name") String name) {
        try {
            DbOperations.deleteDbRecord(name);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }
}
