package webControllers;


import com.google.gson.Gson;
import model.Administrator;
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
@RequestMapping(value = "/admins")
public class WebAdminController {

    //READ
    @RequestMapping(value = "/getAllAdmins", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllAdmins() {
        ArrayList<Administrator> allAdmins= new ArrayList<>();
        Gson parser = new Gson();
        try {
            allAdmins = DbOperations.getAllAdmins();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(allAdmins);
    }

    //INSERT
    @RequestMapping(value = "/insertAdmin", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertAdmin(@RequestBody String request) throws SQLException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String login = data.getProperty("login");
        String password = data.getProperty("password");
        String email = data.getProperty("email");
        int phoneNumber = Integer.parseInt(data.getProperty("phoneNumber"));
        int courseIs = Integer.parseInt(data.getProperty("courseIs"));
        try {
            DbOperations.insertAdmin(login, password, email, phoneNumber, courseIs);
            return parser.toJson(DbOperations.getAdminByLogin(login));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    //UPDATE
    @RequestMapping(value = "/updAdmin", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateAdmin(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int adminId = Integer.parseInt(data.getProperty("id"));
        String login = data.getProperty("login");
        String password = data.getProperty("password");
        String email = data.getProperty("email");
        int phoneNumber = Integer.parseInt(data.getProperty("phoneNumber"));
        int courseIs = Integer.parseInt(data.getProperty("courseIs"));
        try {
            DbOperations.updateAdmin(adminId, login, password, email, phoneNumber, courseIs);
            return parser.toJson(DbOperations.getAdminByLogin(login));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }

    //DELETE
    @RequestMapping(value = "/delAdminId", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCourseId(@RequestParam("id") int id) {
        try {
            DbOperations.deleteAdmin(id);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }

    //Authorization
    @RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String loginEmployee(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String loginName = data.getProperty("login");
        String password = data.getProperty("psw");
        int courseIs = Integer.parseInt(data.getProperty("courseIs"));
        Administrator administrator = null;
        try {
            administrator = DbOperations.getAdmin(loginName, password, courseIs);
        } catch (Exception e) {
            return "Error";
        }
        if (administrator == null) {
            return "Wrong credentials";
        }
        return Integer.toString(administrator.getId());
    }
}
