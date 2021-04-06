package fxControl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.DbOperations;
import utils.utilOperations;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FileManagement implements Initializable {

    @FXML
    public ListView listViewFolders = new ListView();
    public ListView listViewFiles = new ListView();
    public TextField folderName;
    public TextField courseID;
    public DatePicker dateModified;
    public TextField fileName;
    public DatePicker dateAdded;
    public TextField filePath;
    public TextField folderID;
    @FXML

    private Connection connection;
    private PreparedStatement statement;
    private String folder_id = null;
    private String file_id = null;
    private List<String> folders = new ArrayList<>();
    private List<String> files = new ArrayList<>();
    private ObservableList<String> elements;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = DbOperations.connectToDb();
        List<String> loadFolders = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT * FROM folder");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                loadFolders.add(rs.getString(1) + ". FOLDER NAME: " + rs.getString(2) + ", DATE MODIFIED: " + rs.getString(3) + ", COURSE ID: " + rs.getString(4));
                ObservableList loadElements = FXCollections.observableArrayList();
                loadElements.addAll(loadFolders);
                listViewFolders.setItems(loadElements);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<String> loadFiles = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT * FROM folder_files");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                loadFiles.add(rs.getInt(1) + ". FILE NAME: " + rs.getString(2) + ", DATE ADDED: " + rs.getDate(3) + ", FILE PATH: " + rs.getString(4) + ", FOLDER ID: " + rs.getInt(5));
                ObservableList loadElements = FXCollections.observableArrayList();
                loadElements.addAll(loadFiles);
                listViewFiles.setItems(loadElements);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void returnToLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) fileName.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Login.currentAdmin = null;
        Login.currentStudent = null;
    }

    public void clearSelection(ActionEvent actionEvent) {
        connection = DbOperations.connectToDb();
        fileName.setText("");
        dateAdded.setValue(null);
        filePath.setText("");
        folderID.setText("");
        folderName.setText("");
        dateModified.setValue(null);
        courseID.setText("");
        folder_id = "";
        file_id = "";
        files.clear();
        folders.clear();
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void selectFolder(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        elements = listViewFolders.getSelectionModel().getSelectedItems();
        folder_id = elements.get(0).substring(0, 2).replace(".", "");
        statement = connection.prepareStatement("SELECT * FROM folder WHERE id ='" + folder_id + "'");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            folders.add(rs.getString(1));
            folders.add(rs.getString(2));
            folders.add(rs.getString(3));
            folders.add(rs.getString(4));
        }
        folderName.setText(folders.get(1));
        dateModified.setValue(LocalDate.parse(folders.get(2)));
        courseID.setText(folders.get(3));
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void selectFile(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        elements = listViewFiles.getSelectionModel().getSelectedItems();
        file_id = elements.get(0).substring(0, 2).replace(".", "");
        statement = connection.prepareStatement("SELECT * FROM folder_files WHERE id ='" + file_id + "'");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            files.add(rs.getString(1));
            files.add(rs.getString(2));
            files.add(rs.getString(3));
            files.add(rs.getString(4));
            files.add(rs.getString(5));
        }
        fileName.setText(files.get(1));
        dateAdded.setValue(LocalDate.parse(files.get(2)));
        filePath.setText(files.get(3));
        folderID.setText(files.get(4));
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void updateFolder(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        List<String> saveChanges = new ArrayList<>();

        saveChanges.add(folderName.getText());
        saveChanges.add(String.valueOf(dateModified.getValue()));
        saveChanges.add(courseID.getText());

        statement = connection.prepareStatement("UPDATE folder SET folder_name = ?, date_modified = ?, course_id = ? WHERE id = ?");
        statement.setString(4, folders.get(0));
        statement.setString(1, saveChanges.get(0));
        statement.setString(2, saveChanges.get(1));
        statement.setString(3, saveChanges.get(2));
        statement.executeUpdate();
        utilOperations.alertMessage("You have successfully updated the folder!");
        DbOperations.disconnectFromDb(connection, statement);
        refresh();

        folderName.setText("");
        dateModified.setValue(null);
        courseID.setText("");
        folder_id = "";
        folders.clear();
    }

    public void updateFile(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        List<String> saveChanges = new ArrayList<>();

        saveChanges.add(fileName.getText());
        saveChanges.add(String.valueOf(dateAdded.getValue()));
        saveChanges.add(filePath.getText());
        saveChanges.add(folderID.getText());

        statement = connection.prepareStatement("UPDATE folder_files SET file_name = ?, date_added = ?, file_path = ?, folder_id = ? WHERE id = ?");
        statement.setString(5, files.get(0));
        statement.setString(1, saveChanges.get(0));
        statement.setString(2, saveChanges.get(1));
        statement.setString(3, saveChanges.get(2));
        statement.setString(4, saveChanges.get(3));
        statement.executeUpdate();
        utilOperations.alertMessage("You have successfully updated the file!");
        DbOperations.disconnectFromDb(connection, statement);
        refresh();

        fileName.setText("");
        dateAdded.setValue(null);
        filePath.setText("");
        folderID.setText("");
        file_id = "";
        files.clear();
    }

    public void addFolder(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM folder WHERE folder_name = ? AND date_modified = ? AND course_id = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, folderName.getText());
        statement.setString(2, String.valueOf(dateModified.getValue()));
        statement.setString(3, courseID.getText());
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            utilOperations.alertMessage("Folder already exists!");
        } else {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO folder(folder_name, date_modified, course_id) VALUES (?, ?, ?)");
            stmt.setString(1, folderName.getText());
            stmt.setString(2, String.valueOf(dateModified.getValue()));
            stmt.setString(3, courseID.getText());
            stmt.executeUpdate();
            utilOperations.alertMessage("You have successfully added a new folder to the DB!");
        }
        DbOperations.disconnectFromDb(connection, statement);
        refresh();

        folderName.setText("");
        dateModified.setValue(null);
        courseID.setText("");
        folder_id = "";
        folders.clear();
    }

    public void deleteFolder(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        ObservableList<String> elements;
        elements = listViewFolders.getSelectionModel().getSelectedItems();
        String sql = "DELETE FROM folder WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, Integer.parseInt(elements.get(0).substring(0, 2).replace(".", "")));
        statement.executeUpdate();
        utilOperations.alertMessage("You have successfully removed the student!");
        DbOperations.disconnectFromDb(connection, statement);
        refresh();

        folderName.setText("");
        dateModified.setValue(null);
        courseID.setText("");
        folder_id = "";
        folders.clear();
    }

    public void deleteFile(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        ObservableList<String> elements;
        elements = listViewFiles.getSelectionModel().getSelectedItems();
        String sql = "DELETE FROM folder_files WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, Integer.parseInt(elements.get(0).substring(0, 2).replace(".", "")));
        statement.executeUpdate();
        utilOperations.alertMessage("You have successfully removed a file!");
        DbOperations.disconnectFromDb(connection, statement);
        refresh();

        fileName.setText("");
        dateAdded.setValue(null);
        filePath.setText("");
        folderID.setText("");
        file_id = "";
        files.clear();
    }

    public void addFile(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM folder_files WHERE file_name = ? AND date_added = ? AND file_path = ? AND folder_id = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, fileName.getText());
        statement.setString(2, String.valueOf(dateAdded.getValue()));
        statement.setString(3, filePath.getText());
        statement.setString(4, folderID.getText());
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            utilOperations.alertMessage("File already exists!");
        } else {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO folder_files(file_name, date_added, file_path, folder_id) VALUES (?, ?, ?, ?)");
            stmt.setString(1, fileName.getText());
            stmt.setString(2, String.valueOf(dateAdded.getValue()));
            stmt.setString(3, filePath.getText());
            stmt.setString(4, folderID.getText());
            stmt.executeUpdate();
            utilOperations.alertMessage("You have successfully added a new file to the DB!");
        }
        DbOperations.disconnectFromDb(connection, statement);
        refresh();

        fileName.setText("");
        dateAdded.setValue(null);
        filePath.setText("");
        folderID.setText("");
        file_id = "";
        files.clear();
    }

    private void refresh() throws SQLException {
        connection = DbOperations.connectToDb();
        List<String> loadFolders = new ArrayList<>();
        statement = connection.prepareStatement("SELECT * FROM folder");
        ResultSet rs = statement.executeQuery();
        ObservableList loadElements = FXCollections.observableArrayList();
        while (rs.next()) {
            loadFolders.add(rs.getString(1) + ". FOLDER NAME: " + rs.getString(2) + ", DATE MODIFIED: " + rs.getString(3) + ", COURSE ID: " + rs.getString(4));
            loadElements.addAll(loadFolders);
        }
        listViewFolders.setItems(loadElements);

        List<String> loadFiles = new ArrayList<>();
        statement = connection.prepareStatement("SELECT * FROM folder_files");
        ResultSet rs2 = statement.executeQuery();
        ObservableList loadElements2 = FXCollections.observableArrayList();
        while (rs2.next()) {
            loadFiles.add(rs2.getInt(1) + ". FILE NAME: " + rs2.getString(2) + ", DATE ADDED: " + rs2.getDate(3) + ", FILE PATH: " + rs2.getString(4) + ", FOLDER ID: " + rs2.getInt(5));
            loadElements2.addAll(loadFiles);
        }
        listViewFiles.setItems(loadElements2);
        DbOperations.disconnectFromDb(connection, statement);
    }
}
