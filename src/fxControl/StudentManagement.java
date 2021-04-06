package fxControl;

import com.google.protobuf.Internal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DbOperations;
import utils.utilOperations;

import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class StudentManagement implements Initializable {

    @FXML
    public TextField accNumberField;
    public TextField courseIsField;
    public TextField surnameField;
    public TextField nameField;
    public TextField emailField;
    public TextField passwordField;
    public TextField loginField;
    public ListView listView = new ListView();
    @FXML

    private Connection connection;
    private PreparedStatement statement;
    private List<String> students = new ArrayList<>();
    private String student_id = null;
    private ObservableList<String> elements;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = DbOperations.connectToDb();
        List<String> loadStudents = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT * FROM students");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                loadStudents.add(rs.getInt(1) + ". LOGIN: " + rs.getString(2) + ", PASSWORD: " + rs.getString(3) + ", EMAIL: " + rs.getString(4) + ", NAME: " + rs.getString(5) + ", SURNAME: " + rs.getString(6) + ", ACC_NUMBER: " + rs.getString(7) + ", COURSE_IS: " + rs.getString(8));
                ObservableList loadElements = FXCollections.observableArrayList();
                loadElements.addAll(loadStudents);
                listView.setItems(loadElements);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void returnToLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Login.currentAdmin = null;
        Login.currentStudent = null;
    }

    public void addStudent(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM students WHERE login = ? AND password = ? AND email = ? AND student_name = ? AND surname = ? AND acc_number = ? AND course_is = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, loginField.getText());
        statement.setString(2, passwordField.getText());
        statement.setString(3, emailField.getText());
        statement.setString(4, nameField.getText());
        statement.setString(5, surnameField.getText());
        statement.setInt(6, Integer.parseInt(accNumberField.getText()));
        statement.setInt(7, Integer.parseInt(courseIsField.getText()));
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            utilOperations.alertMessage("Student already exists!");
        } else {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO students(login, password, email, student_name, surname, acc_number, course_is) VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, loginField.getText());
            stmt.setString(2, passwordField.getText());
            stmt.setString(3, emailField.getText());
            stmt.setString(4, nameField.getText());
            stmt.setString(5, surnameField.getText());
            stmt.setInt(6, Integer.parseInt(accNumberField.getText()));
            stmt.setInt(7, Integer.parseInt(courseIsField.getText()));
            stmt.executeUpdate();
            utilOperations.alertMessage("You have successfully added a new student to the DB!");
        }
        DbOperations.disconnectFromDb(connection, statement);
        refresh();

        loginField.setText("");
        passwordField.setText("");
        emailField.setText("");
        nameField.setText("");
        surnameField.setText("");
        accNumberField.setText("");
        courseIsField.setText("");
        student_id = "";
        students.clear();
    }

    public void deleteStudent(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        ObservableList<String> elements;
        elements = listView.getSelectionModel().getSelectedItems();
        String sql = "DELETE FROM students WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, Integer.parseInt(elements.get(0).substring(0, 2).replace(".", "")));
        statement.executeUpdate();
        utilOperations.alertMessage("You have successfully removed the student!");
        refresh();
    }

    public void updateStudent(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        List<String> saveChanges = new ArrayList<>();

        saveChanges.add(loginField.getText());
        saveChanges.add(passwordField.getText());
        saveChanges.add(emailField.getText());
        saveChanges.add(nameField.getText());
        saveChanges.add(surnameField.getText());
        saveChanges.add(accNumberField.getText());
        saveChanges.add(courseIsField.getText());

        statement = connection.prepareStatement("UPDATE students SET login = ?, password = ?, email = ?, student_name = ?, surname = ?, acc_number = ?, course_is = ? WHERE id = ?");
        statement.setString(8, students.get(0));
        statement.setString(1, saveChanges.get(0));
        statement.setString(2, saveChanges.get(1));
        statement.setString(3, saveChanges.get(2));
        statement.setString(4, saveChanges.get(3));
        statement.setString(5, saveChanges.get(4));
        statement.setString(6, saveChanges.get(5));
        statement.setString(7, saveChanges.get(6));
        statement.executeUpdate();
        utilOperations.alertMessage("You have successfully updated the student!");
        DbOperations.disconnectFromDb(connection, statement);
        refresh();

        loginField.setText("");
        passwordField.setText("");
        emailField.setText("");
        nameField.setText("");
        surnameField.setText("");
        accNumberField.setText("");
        courseIsField.setText("");
        student_id = "";
        students.clear();
    }

    public void selectStudent(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        elements = listView.getSelectionModel().getSelectedItems();
        student_id = elements.get(0).substring(0, 2).replace(".", "");
        statement = connection.prepareStatement("SELECT * FROM students WHERE id ='" + student_id + "'");
        //statement.setString(1, student_id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            students.add(rs.getString(1));
            students.add(rs.getString(2));
            students.add(rs.getString(3));
            students.add(rs.getString(4));
            students.add(rs.getString(5));
            students.add(rs.getString(6));
            students.add(rs.getString(7));
            students.add(rs.getString(8));
        }
        loginField.setText(students.get(1));
        passwordField.setText(students.get(2));
        emailField.setText(students.get(3));
        nameField.setText(students.get(4));
        surnameField.setText(students.get(5));
        accNumberField.setText(students.get(6));
        courseIsField.setText(students.get(7));
        DbOperations.disconnectFromDb(connection, statement);
    }

    private void refresh() throws SQLException {
        connection = DbOperations.connectToDb();
        List<String> loadStudents = new ArrayList<>();
        ObservableList loadElements = FXCollections.observableArrayList();
        statement = connection.prepareStatement("SELECT * FROM students");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            loadStudents.add(rs.getInt(1) + ". LOGIN: " + rs.getString(2) + ", PASSWORD: " + rs.getString(3) + ", EMAIL: " + rs.getString(4) + ", NAME: " + rs.getString(5) + ", SURNAME: " + rs.getString(6) + ", ACC_NUMBER: " + rs.getString(7) + ", COURSE_IS: " + rs.getString(8));
            loadElements = FXCollections.observableArrayList();
            loadElements.addAll(loadStudents);
        }

        listView.setItems(loadElements);
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void clearSelection(ActionEvent actionEvent) {
        connection = DbOperations.connectToDb();
        loginField.setText("");
        passwordField.setText("");
        emailField.setText("");
        nameField.setText("");
        surnameField.setText("");
        accNumberField.setText("");
        courseIsField.setText("");
        student_id = "";
        students.clear();
        DbOperations.disconnectFromDb(connection, statement);
    }
}
