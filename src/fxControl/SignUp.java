package fxControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DbOperations;
import utils.utilOperations;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO students(login, password, email, student_name, surname, acc_number, course_is) VALUES (?, ?, ?, ?, ?, ?, ?)");

                int accNumber = (int) (Math.random() * (101 - 1 + 1) + 1); // 100-1

                stmt.setString(1, loginField.getText());
                stmt.setString(2, passwordField.getText());
                stmt.setString(3, emailField.getText());
                stmt.setString(4, nameField.getText());
                stmt.setString(5, surnameField.getText());
                stmt.setInt(6, accNumber);
                stmt.setInt(7, courseIsId);
                stmt.executeUpdate();
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
