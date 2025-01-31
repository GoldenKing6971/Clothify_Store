package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class RegisterFormAndLoginFormLorder {

    @FXML
    private AnchorPane loadFormContent;

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        URL resource = getClass().getResource("/view/login_form.fxml");

        assert resource != null;

        try {
            Parent load = FXMLLoader.load(resource);

            loadFormContent.getChildren().clear();
            loadFormContent.getChildren().add(load);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        URL resource = getClass().getResource("/view/register_form.fxml");

        assert resource != null;

        try {
            Parent load = FXMLLoader.load(resource);

            loadFormContent.getChildren().clear();
            loadFormContent.getChildren().add(load);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
