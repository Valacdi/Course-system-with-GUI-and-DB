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
import javafx.scene.control.ListView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminManagement implements Initializable {

    @FXML
    public ListView listView = new ListView();
    public TextField loginField;
    public TextField pswField;
    public TextField emailField;
    public TextField phoneField;
    public TextField courseISField;
    @FXML

    private Connection connection;
    private PreparedStatement statement;
    private List<String> admins = new ArrayList<>();
    private String admin_id = null;
    private ObservableList<String> elements;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = DbOperations.connectToDb();
        List<String> loadAdmins = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT * FROM admins");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                loadAdmins.add(rs.getInt(1) + ". LOGIN: " + rs.getString(2) + ", PASSWORD: " + rs.getString(3) + ", EMAIL: " + rs.getString(4) + ", PHONE NUMBER: " + rs.getString(5) + ", COURSE_IS: " + rs.getString(6));
                ObservableList loadElements = FXCollections.observableArrayList();
                loadElements.addAll(loadAdmins);
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
    }

    public void addAdmin(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        ObservableList<String> elements;
        elements = listView.getSelectionModel().getSelectedItems();
        String sql = "SELECT * FROM admins WHERE login = ? AND password = ? AND email = ? AND phone_number = ? AND course_is = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, loginField.getText());
        statement.setString(2, pswField.getText());
        statement.setString(3, emailField.getText());
        statement.setInt(4, Integer.parseInt(phoneField.getText()));
        statement.setInt(5, Integer.parseInt(courseISField.getText()));
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            utilOperations.alertMessage("Admin already exists!");
        } else {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO admins(login, password, email, phone_number, course_is) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, loginField.getText());
            stmt.setString(2, pswField.getText());
            stmt.setString(3, emailField.getText());
            stmt.setInt(4, Integer.parseInt(phoneField.getText()));
            stmt.setInt(5, Integer.parseInt(courseISField.getText()));
            stmt.executeUpdate();
            utilOperations.alertMessage("You have successfully added a new admin to the DB!");
            refresh();

            loginField.setText("");
            loginField.setText("");
            pswField.setText("");
            emailField.setText("");
            phoneField.setText("");
            courseISField.setText("");
            admin_id = "";
            admins.clear();
        }
    }

    public void deleteAdmin(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        ObservableList<String> elements;
        elements = listView.getSelectionModel().getSelectedItems();
        String sql = "DELETE FROM admins WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, Integer.parseInt(elements.get(0).substring(0, 1)));
        statement.executeUpdate();
        utilOperations.alertMessage("You have successfully removed an admin!");
        DbOperations.disconnectFromDb(connection, statement);
        refresh();
    }

    public void updateAdmin(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        List<String> saveChanges = new ArrayList<>();

        saveChanges.add(loginField.getText());
        saveChanges.add(pswField.getText());
        saveChanges.add(emailField.getText());
        saveChanges.add(phoneField.getText());
        saveChanges.add(courseISField.getText());

        statement = connection.prepareStatement("UPDATE admins SET login = ?, password = ?, email = ?, phone_number = ?, course_is = ? WHERE id = ?");
        statement.setString(6, admins.get(0));
        statement.setString(1, saveChanges.get(0));
        statement.setString(2, saveChanges.get(1));
        statement.setString(3, saveChanges.get(2));
        statement.setString(4, saveChanges.get(3));
        statement.setString(5, saveChanges.get(4));
        statement.executeUpdate();
        utilOperations.alertMessage("You have successfully updated the admin!");
        DbOperations.disconnectFromDb(connection, statement);
        refresh();
        loginField.setText("");
        loginField.setText("");
        pswField.setText("");
        emailField.setText("");
        phoneField.setText("");
        courseISField.setText("");
        admin_id = "";
        admins.clear();
    }

    public void selectAdmin(ActionEvent actionEvent) throws SQLException {
        connection = DbOperations.connectToDb();
        elements = listView.getSelectionModel().getSelectedItems();
        admin_id = elements.get(0).substring(0, 2).replace(".", "");
        statement = connection.prepareStatement("SELECT * FROM admins WHERE id ='" + admin_id + "'");
        //statement.setString(1, student_id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            admins.add(rs.getString(1));
            admins.add(rs.getString(2));
            admins.add(rs.getString(3));
            admins.add(rs.getString(4));
            admins.add(rs.getString(5));
            admins.add(rs.getString(6));
        }
        loginField.setText(admins.get(1));
        pswField.setText(admins.get(2));
        emailField.setText(admins.get(3));
        phoneField.setText(admins.get(4));
        courseISField.setText(admins.get(5));
        DbOperations.disconnectFromDb(connection, statement);
    }

    private void refresh() throws SQLException {
        connection = DbOperations.connectToDb();
        List<String> loadAdmins = new ArrayList<>();
        ObservableList loadElements = FXCollections.observableArrayList();
        statement = connection.prepareStatement("SELECT * FROM admins");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            loadAdmins.add(rs.getInt(1) + ". LOGIN: " + rs.getString(2) + ", PASSWORD: " + rs.getString(3) + ", EMAIL: " + rs.getString(4) + ", PHONE NUMBER: " + rs.getString(5) + ", COURSE_IS: " + rs.getString(6));
            loadElements = FXCollections.observableArrayList();
            loadElements.addAll(loadAdmins);
        }

        listView.setItems(loadElements);
        DbOperations.disconnectFromDb(connection, statement);
    }

    public void clearSelection(ActionEvent actionEvent) {
        connection = DbOperations.connectToDb();
        loginField.setText("");
        loginField.setText("");
        pswField.setText("");
        emailField.setText("");
        phoneField.setText("");
        courseISField.setText("");
        admin_id = "";
        admins.clear();
        DbOperations.disconnectFromDb(connection, statement);
    }
}
