package fxControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DbOperations;
import utils.utilOperations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUp {

    @FXML
    public PasswordField passwordField;
    public PasswordField passwordFieldRepeat;
    public TextField nameField;
    public TextField surnameField;
    public TextField loginField;
    public TextField emailField;
    @FXML

    private Connection connection;
    private PreparedStatement statement;
    private int courseIsId;

    public void returnToLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Login.currentAdmin = null;
        Login.currentStudent = null;
    }

    public void createUser(ActionEvent actionEvent) throws SQLException {

        if (passwordField.getText().equals(passwordFieldRepeat.getText())) {
            connection = DbOperations.connectToDb();
            String sql = "SELECT * FROM students AS s WHERE s.login = ? AND s.password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, loginField.getText());
            statement.setString(2, passwordField.getText());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                utilOperations.alertMessage("Such student already exists!");
            } else {
                int accNumber = (int) (Math.random() * (101 - 1 + 1) + 1); // 100-1
                DbOperations.insertStudent(loginField.getText(), passwordField.getText(), emailField.getText(), nameField.getText(), surnameField.getText(), String.valueOf(accNumber), courseIsId);
                utilOperations.alertMessage("You have successfully registered!");
            }
            DbOperations.disconnectFromDb(connection, statement);
        } else {
            utils.utilOperations.alertMessage("Passwords do not match!");
        }
    }

    public void setCourseID(int courseIsId) {
        this.courseIsId = courseIsId;
    }
}
