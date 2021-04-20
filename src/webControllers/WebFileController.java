package webControllers;

import com.google.gson.Gson;
import model.File;
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
@RequestMapping(value = "/files")
public class WebFileController {

    //READ
    @RequestMapping(value = "/getAllFiles", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllFiles() {
        ArrayList<File> allFiles = new ArrayList<>();
        Gson parser = new Gson();
        try {
            allFiles = DbOperations.getAllFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(allFiles);
    }

    //INSERT
    @RequestMapping(value = "/insertFile", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertFile(@RequestBody String request) throws SQLException {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String fileName = data.getProperty("file_name");
        LocalDate dateModified = LocalDate.parse(data.getProperty("date_added"));
        String filePath = data.getProperty("file_path");
        int folderId = Integer.parseInt(data.getProperty("folder_id"));
        try {
            DbOperations.insertFile(fileName, dateModified, filePath, folderId);
            return parser.toJson(DbOperations.getFileByName(fileName));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    //UPDATE
    @RequestMapping(value = "/updFile", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateFile(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int fileId = Integer.parseInt(data.getProperty("id"));
        String fileName = data.getProperty("file_name");
        LocalDate dateModified = LocalDate.parse(data.getProperty("date_added"));
        String filePath = data.getProperty("file_path");
        int folderId = Integer.parseInt(data.getProperty("folder_id"));
        try {
            DbOperations.updateFile(fileId, fileName, dateModified, filePath, folderId);
            return parser.toJson(DbOperations.getFileByName(fileName));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }

    //DELETE
    @RequestMapping(value = "/delFile", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteFileId(@RequestParam("id") int id) {
        try {
            DbOperations.deleteFile(id);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }
}
