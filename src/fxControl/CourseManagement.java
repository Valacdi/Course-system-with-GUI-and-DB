package fxControl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DbOperations;
import utils.utilOperations;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CourseManagement implements Initializable {
    @FXML
    public TextField courseIsField;
    public TextField coursePriceISField;
    public TextField admidIdField;
    public DatePicker endField;
    public DatePicker startField;
    public TextField nameField;
    public ListView listView = new ListView();
    @FXML

    private Connection connection;
    private PreparedStatement statement;
    private List<String> courses = new ArrayList<>();
    private String course_id = null;
    private ObservableList<String> elements;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = DbOperations.connectToDb();
        List<String> courses = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT * FROM course");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                courses.add(rs.getString(1) + ". " + rs.getString(2) + ", PRADZIA: " + rs.getString(3) + ", PABAIGA: " + rs.getString(4) + ", ADMIN ID: " + rs.getString(5) + ", COURSE PRICE: " + rs.getString(6) + ", COURSE IS: " + rs.getString(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ObservableList<String> elements = FXCollections.observableArrayList();
        elements.addAll(courses);
        listView.setItems(elements);
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void returnToLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Login.currentAdmin = null;
        Login.currentStudent = null;
    }

    public void addCourse(ActionEvent actionEvent) throws SQLException {
        ObservableList<String> elements;
        elements = listView.getSelectionModel().getSelectedItems();
        DbOperations.insertCourse(nameField.getText(), startField.getValue(), endField.getValue(), Integer.parseInt(admidIdField.getText()), Double.parseDouble(courseIsField.getText()), Integer.parseInt(courseIsField.getText()));
        utilOperations.alertMessage("You have successfully added a new course to the DB!");
        refresh();
        nameField.setText("");
        startField.setValue(null);
        endField.setValue(null);
        admidIdField.setText("");
        coursePriceISField.setText("");
        courseIsField.setText("");
        course_id = "";
        courses.clear();
    }

    public void deleteCourse(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        ObservableList<String> elements;
        elements = listView.getSelectionModel().getSelectedItems();
        int id = Integer.parseInt(elements.get(0).substring(0, 1));
        DbOperations.deleteCourse(id);
        utilOperations.alertMessage("You have successfully removed a course!");
        refresh();
    }

    public void updateCourse(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        List<String> saveChanges = new ArrayList<>();

        saveChanges.add(nameField.getText());
        saveChanges.add(String.valueOf(startField.getValue()));
        saveChanges.add(String.valueOf(endField.getValue()));
        saveChanges.add(admidIdField.getText());
        saveChanges.add(coursePriceISField.getText());
        saveChanges.add(courseIsField.getText());

        statement = connection.prepareStatement("UPDATE course SET course_name = ?, start_date = ?, end_date = ?, admin_id = ?, course_price = ?, course_is = ? WHERE id = ?");
        statement.setString(7, courses.get(0));
        statement.setString(1, saveChanges.get(0));
        statement.setString(2, saveChanges.get(1));
        statement.setString(3, saveChanges.get(2));
        statement.setString(4, saveChanges.get(3));
        statement.setString(5, saveChanges.get(4));
        statement.setString(6, saveChanges.get(5));
        statement.executeUpdate();
        utilOperations.alertMessage("You have successfully updated the course!");
        DbOperations.disconnectFromDb(connection, statement);
        refresh();

        nameField.setText("");
        startField.setValue(null);
        endField.setValue(null);
        admidIdField.setText("");
        coursePriceISField.setText("");
        courseIsField.setText("");
        course_id = "";
        courses.clear();
    }

    public void selectCourse(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        elements = listView.getSelectionModel().getSelectedItems();
        course_id = elements.get(0).substring(0, 2).replace(".", "");
        statement = connection.prepareStatement("SELECT * FROM course WHERE id ='" + course_id + "'");
        //statement.setString(1, student_id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            courses.add(rs.getString(1));
            courses.add(rs.getString(2));
            courses.add(rs.getString(3));
            courses.add(rs.getString(4));
            courses.add(rs.getString(5));
            courses.add(rs.getString(6));
            courses.add(rs.getString(7));
        }
        nameField.setText(courses.get(1));
        startField.setValue(LocalDate.parse(courses.get(2)));
        endField.setValue(LocalDate.parse(courses.get(3)));
        admidIdField.setText(courses.get(4));
        coursePriceISField.setText(courses.get(5));
        courseIsField.setText(courses.get(6));
        DbOperations.disconnectFromDb(connection, statement);
    }

    private void refresh() throws SQLException {
        connection = DbOperations.connectToDb();
        List<String> loadCourses = new ArrayList<>();
        ObservableList loadElements = FXCollections.observableArrayList();
        statement = connection.prepareStatement("SELECT * FROM course");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            loadCourses.add(rs.getString(1) + ". " + rs.getString(2) + ", PRADZIA: " + rs.getString(3) + ", PABAIGA: " + rs.getString(4) + ", ADMIN ID: " + rs.getString(5) + ", COURSE PRICE: " + rs.getString(6) + ", COURSE IS: " + rs.getString(7));
            loadElements = FXCollections.observableArrayList();
            loadElements.addAll(loadCourses);
        }

        listView.setItems(loadElements);
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void clearSelection(ActionEvent actionEvent) {
        connection = DbOperations.connectToDb();
        nameField.setText("");
        startField.setValue(null);
        endField.setValue(null);
        admidIdField.setText("");
        coursePriceISField.setText("");
        courseIsField.setText("");
        course_id = "";
        courses.clear();
        DbOperations.disconnectFromDb(connection, statement);
    }
}
