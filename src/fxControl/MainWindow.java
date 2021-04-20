package fxControl;

import javafx.application.Platform;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {

    @FXML
    public ListView listView;
    public Tab studentManagementTab;
    public Tab AdminManagementTab;
    public Tab courseManagementTab;
    public Tab fileManagementTab;
    @FXML

    private Connection connection;
    private PreparedStatement statement;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = DbOperations.connectToDb();
        List<String> courses = new ArrayList<>();
        Platform.runLater(() -> {
            if (Login.currentAdmin != null) {
                studentManagementTab.setDisable(false);
                AdminManagementTab.setDisable(false);
                courseManagementTab.setDisable(false);
                fileManagementTab.setDisable(false);
            }
        });
        if (Login.currentAdmin == null) {
            listView.getItems().clear();
            try {
                DbOperations.getAllCoursesFromDb(Login.currentStudent.getCourse_is()).forEach(c -> listView.getItems().add(c.getName() + ". PRADZIA:" + c.getStartDate() + ", PABAIGA: " + c.getEndDate() + ", KAINA: " + c.getCoursePrice()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            try {
                statement = connection.prepareStatement("SELECT * FROM course");
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    courses.add(rs.getString(1) + ". " + rs.getString(2) + ", PRADZIA: " + rs.getDate(3) + ", PABAIGA: " + rs.getDate(4) + ", KAINA: " + rs.getDouble(6));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ObservableList elements = FXCollections.observableArrayList();
            elements.addAll(courses);
            listView.setItems(elements);
            DbOperations.disconnectFromDb(connection, statement);
        }
    }

    public void checkInfo(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        ObservableList<String> elements = listView.getSelectionModel().getSelectedItems();
        int courseId = Integer.parseInt(elements.get(0).substring(0, 1));
        String sql = "SELECT COUNT(*) FROM student_enroll_course WHERE course_id = '" + courseId + "'";
        statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        utilOperations.alertMessage("There is currently " + count + " student(s) enrolled into this course.");
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void enrollToCourse(ActionEvent actionEvent) throws SQLException {

        if (Login.currentAdmin == null) {
            connection = DbOperations.connectToDb();
            ObservableList<String> elements;
            elements = listView.getSelectionModel().getSelectedItems();
            String sql = "SELECT * FROM student_enroll_course WHERE student_id = ? AND course_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, Login.currentStudent.getId());
            statement.setInt(2, Integer.parseInt(elements.get(0).substring(0, 1)));
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                utilOperations.alertMessage("You are already in this course!");
            } else {
                DbOperations.insertStudentToEnroll(Login.currentStudent.getId(), Integer.parseInt(elements.get(0).substring(0, 1)));
                utilOperations.alertMessage("You have successfully enrolled into the course!");
            }
        } else {
            utilOperations.alertMessage("Admins cannot enroll to courses!");
        }
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void goToLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) listView.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Login.currentAdmin = null;
        Login.currentStudent = null;
    }

    public void refresh(ActionEvent actionEvent) throws SQLException {

        List<String> courses = new ArrayList<>();
        connection = DbOperations.connectToDb();
        ObservableList elementsStudent = FXCollections.observableArrayList();
        if (Login.currentAdmin == null) {
            statement = connection.prepareStatement("SELECT * FROM course WHERE course_is = '" + Login.currentStudent.getCourse_is() + "'");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                courses.add(rs.getString(1) + ". " + rs.getString(2) + ", PRADZIA: " + rs.getDate(3) + ", PABAIGA: " + rs.getDate(4) + ", KAINA: " + rs.getDouble(6));
                elementsStudent.addAll(courses);
            }
            listView.setItems(elementsStudent);
            DbOperations.disconnectFromDb(connection, statement);
        } else {
            ObservableList elementsAdmin = FXCollections.observableArrayList();
            statement = connection.prepareStatement("SELECT * FROM course");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                courses.add(rs.getString(1) + ". " + rs.getString(2) + ", PRADZIA: " + rs.getDate(3) + ", PABAIGA: " + rs.getDate(4) + ", KAINA: " + rs.getDouble(6));
                elementsAdmin.addAll(courses);
            }
            listView.setItems(elementsAdmin);
            DbOperations.disconnectFromDb(connection, statement);
        }
    }
}
