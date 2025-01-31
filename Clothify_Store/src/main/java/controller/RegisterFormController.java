package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import org.jasypt.util.text.BasicTextEncryptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterFormController {

    public TextField txtUserName;
    @FXML
    private PasswordField txtCPassword;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    void btnRegisterOnAction(ActionEvent event) throws SQLException {

        String key = "sl6971vd@&$";

        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();

        basicTextEncryptor.setPassword(key);

        String SQL = "INSERT INTO users (username,email,password_hash) VALUES(?,?,?)";

        if(txtPassword.getText().equals(txtCPassword.getText())){

            Connection connection = DBConnection.getInstance().getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM users WHERE email=" + "'" + txtEmail.getText() + "'");

                if (!resultSet.next()){
                    User user = new User(
                            txtUserName.getText(),
                            txtEmail.getText(),
                            txtPassword.getText()
                    );
                    PreparedStatement psTm = connection.prepareStatement(SQL);
                    psTm.setString(1, user.getUserName());
                    psTm.setString(2, user.getEmail());
                    psTm.setString(3, basicTextEncryptor.encrypt(user.getPassword()));
                    psTm.executeUpdate();

                    new Alert(Alert.AlertType.INFORMATION,"Successfully Registered!!").show();
                }else {
                    new Alert(Alert.AlertType.ERROR,"Already Register Email !!").show();
                }

        }else {
            new Alert(Alert.AlertType.ERROR, "Check Your Password !!").show();
        }

    }

}
