package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class DashboardFormController {

    public AnchorPane loadFormContent;

    private boolean isDarkMode = false;

    @FXML
    private BorderPane root;

    @FXML
    void btnCustomersLoderOnAction(ActionEvent event) {
        URL resource = getClass().getResource("/view/customer_form.fxml");

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
    void btnDashboardLoderOnAction(ActionEvent event) {
        URL resource = getClass().getResource("/view/dashboord_view.fxml");

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
    void btnOrdersOnAction(ActionEvent event) {
        URL resource = getClass().getResource("/view/order_form.fxml");

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
    void btnProductsOnAction(ActionEvent event) {
        URL resource = getClass().getResource("/view/product_form.fxml");

        assert resource != null;

        try {
            Parent load = FXMLLoader.load(resource);

            loadFormContent.getChildren().clear();
            loadFormContent.getChildren().add(load);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void deliveryOnAction(ActionEvent actionEvent) {
        URL resource = getClass().getResource("/view/delivery_form.fxml");

        assert resource != null;

        try {
            Parent load = FXMLLoader.load(resource);

            loadFormContent.getChildren().clear();
            loadFormContent.getChildren().add(load);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reportsOnAction(ActionEvent actionEvent) {
        URL resource = getClass().getResource("/view/report_form.fxml");

        assert resource != null;

        try {
            Parent load = FXMLLoader.load(resource);

            loadFormContent.getChildren().clear();
            loadFormContent.getChildren().add(load);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goInterFaceOnAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/main_interface_form.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }
}