package webControllers;

import com.google.gson.Gson;
import model.Administrator;
import model.Folder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DbOperations;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

@Controller
@RequestMapping(value = "/folders")
public class WebFolderController {

    //READ
    @RequestMapping(value = "/getAllFolders", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllFolders() {
        ArrayList<Folder> allFolders = new ArrayList<>();
        Gson parser = new Gson();
        try {
            allFolders = DbOperations.getAllFolders();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(allFolders);
    }

    //INSERT
    @RequestMapping(value = "/insertFolder", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertFolder(@RequestBody String request) throws SQLException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String folderName = data.getProperty("folder_name");
        LocalDate dateModified = LocalDate.parse(data.getProperty("date_modified"));
        int courseId = Integer.parseInt(data.getProperty("course_id"));
        try {
            DbOperations.insertFolder(folderName, dateModified, courseId);
            return parser.toJson(DbOperations.getFolderByName(folderName));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    //UPDATE
    @RequestMapping(value = "/updFolder", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateFolder(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int folderId = Integer.parseInt(data.getProperty("id"));
        String folderName = data.getProperty("folder_name");
        LocalDate dateModified = LocalDate.parse(data.getProperty("date_modified"));
        int courseId = Integer.parseInt(data.getProperty("courseIs"));
        try {
            DbOperations.updateFolder(folderId, folderName, dateModified, courseId);
            return parser.toJson(DbOperations.getFolderByName(folderName));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }

    //DELETE
    @RequestMapping(value = "/delFolder", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteFolderId(@RequestParam("id") int id) {
        try {
            DbOperations.deleteFolder(id);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }
}
