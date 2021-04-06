package fxControl;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Administrator;
import model.Student;
import model.User;
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

public class Login implements Initializable {

    @FXML
    public Button logIn;
    public Button signUp;
    public CheckBox checkBoxAdmin;
    public TextField loginField;
    public PasswordField passwordField;
    public ComboBox coursesBox;
    @FXML

    private Connection connection;
    private PreparedStatement statement;
    public static Administrator currentAdmin; //sutaisyti
    public static Student currentStudent; //sutaisyti
    private int courseIsId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> options = new ArrayList<>();
        connection = DbOperations.connectToDb();
        if (connection == null) {
            utilOperations.alertMessage("Unable to connect");
            Platform.exit();
        } else {
            try {
                statement = connection.prepareStatement("SELECT * FROM course_is");
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    options.add(rs.getString(2) + "(" + rs.getInt(1) + ")");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            coursesBox.getItems().addAll(options);
        }
        DbOperations.disconnectFromDb(connection, statement);
    }


    public void goToSignUp(ActionEvent actionEvent) throws IOException {
        courseIsId = Integer.parseInt(coursesBox.getValue().toString().split("\\(")[1].replace(")", ""));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/signUp.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) loginField.getScene().getWindow();
        SignUp signUp = loader.getController();
        signUp.setCourseID(courseIsId);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void loginAndValidate(ActionEvent actionEvent) throws SQLException, IOException {

        connection = DbOperations.connectToDb();
        if (checkBoxAdmin.isSelected()) {
            courseIsId = Integer.parseInt(coursesBox.getValue().toString().split("\\(")[1].replace(")", ""));
            String sql = "SELECT * FROM admins AS a WHERE a.login = ? AND a.password = ? AND a.course_is = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, loginField.getText());
            statement.setString(2, passwordField.getText());
            statement.setInt(3, courseIsId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/mainWindow.fxml"));
                currentAdmin = new Administrator(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), courseIsId);
                Parent root = loader.load();
                Stage stage = (Stage) loginField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                utilOperations.alertMessage("No such admin exists!");
            }
            DbOperations.disconnectFromDb(connection, statement);
        } else {
            courseIsId = Integer.parseInt(coursesBox.getValue().toString().split("\\(")[1].replace(")", ""));
            String sql = "SELECT * FROM students AS s WHERE s.login = ? AND s.password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, loginField.getText());
            statement.setString(2, passwordField.getText());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/mainWindow.fxml"));
                currentStudent = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), courseIsId);
                Parent root = loader.load();
                Stage stage = (Stage) loginField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                utilOperations.alertMessage("No such student exists!");
            }
            DbOperations.disconnectFromDb(connection, statement);
        }
    }
}

