package fxControl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Administrator;
import model.Student;
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

public class MyCourses implements Initializable {
    @FXML
    public ListView listView;
    @FXML

    private Connection connection;
    private PreparedStatement statement;
    private List<String> courses = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = DbOperations.connectToDb();
        if (Login.currentAdmin == null) {
            ObservableList elements = FXCollections.observableArrayList();
            try {
                statement = connection.prepareStatement("SELECT course_id FROM student_enroll_course WHERE student_id = '" + Login.currentStudent.getId() + "'");
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    statement = connection.prepareStatement("SELECT * FROM course WHERE id = '" + rs.getInt(1) + "'");
                    ResultSet rs2 = statement.executeQuery();
                    while (rs2.next()) {
                        courses.add(rs2.getString(1) + ". " + rs2.getString(2) + ", PRADZIA: " + rs2.getDate(3) + ", PABAIGA: " + rs2.getDate(4) + ", KAINA: " + rs2.getDouble(6));
                        elements.addAll(courses);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            listView.setItems(elements);
        }
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void leaveCourse(ActionEvent actionEvent) throws SQLException {
        if (Login.currentAdmin == null)
        {
            connection = DbOperations.connectToDb();
            ObservableList<String> elements;
            elements = listView.getSelectionModel().getSelectedItems();
            String sql = "DELETE FROM student_enroll_course WHERE course_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(elements.get(0).substring(0, 1)));
            statement.executeUpdate();
            utilOperations.alertMessage("You have successfully left the course!");
            DbOperations.disconnectFromDb(connection, statement);
            refresh();
        }
        else
            utilOperations.alertMessage("Admins cannot do actions with this button!");
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

    public void refreshButton(ActionEvent actionEvent) throws SQLException {
        refresh();
    }

    private void refresh() throws SQLException {
        connection = DbOperations.connectToDb();
        if (Login.currentAdmin == null) {
            ObservableList elementsStudent = FXCollections.observableArrayList();
            statement = connection.prepareStatement("SELECT course_id FROM student_enroll_course WHERE student_id = '" + Login.currentStudent.getId() + "'");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                statement = connection.prepareStatement("SELECT * FROM course WHERE id = '" + rs.getInt(1) + "'");
                ResultSet rs2 = statement.executeQuery();
                while (rs2.next()) {
                    courses.add(rs2.getString(1) + ". " + rs2.getString(2) + ", PRADZIA: " + rs2.getDate(3) + ", PABAIGA: " + rs2.getDate(4) + ", KAINA: " + rs2.getDouble(6));
                    elementsStudent.addAll(courses);
                }
            }
            listView.setItems(elementsStudent);
        } else
            utilOperations.alertMessage("Admins cannot do actions with this button!");
        DbOperations.disconnectFromDb(connection, statement);
    }
}
