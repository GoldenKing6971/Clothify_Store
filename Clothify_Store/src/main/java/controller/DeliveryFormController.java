package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Delivery;
import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveryFormController {

    public JFXTextField txtDeliveryId;
    public JFXTextField txtCustomerName;
    public JFXTextField txtOrderID;
    public JFXTextField txtStatus;
    public JFXTextField txtAddress;
    public JFXTextField txtDate;
    public JFXComboBox<String> cmbStatus;
    public TableView<Delivery> deliveryTable;
    public TableColumn<Delivery, String> colDeliveryID;
    public TableColumn<Delivery, String> colOrderID;
    public TableColumn<Delivery, String> colCustomerName;
    public TableColumn<Delivery, String> colAddress;
    public TableColumn<Delivery, String> colStatus;
    public TableColumn<Delivery, String> colDate;

    public void initialize() {
        // Set up the TableView columns
        colDeliveryID.setCellValueFactory(new PropertyValueFactory<>("deliveryId"));
        colOrderID.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

    }

    // Reload the table with data from the database
    public void btnReloadOnAction(ActionEvent actionEvent) {
        try {
            ResultSet resultSet = DBConnection.getInstance().getAllDeliveries();
            ObservableList<Delivery> deliveries = FXCollections.observableArrayList();

            while (resultSet.next()) {
                deliveries.add(new Delivery(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6)
                ));
            }
            deliveryTable.setItems(deliveries);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a new delivery
    public void btnaddOnAction(ActionEvent actionEvent) {
        try {
            Delivery delivery = new Delivery(
                    txtDeliveryId.getText(),
                    txtCustomerName.getText(),
                    txtOrderID.getText(),
                    txtAddress.getText(),
                    txtStatus.getText(),
                    txtDate.getText()
            );
            DBConnection.getInstance().addDelivery(delivery);
            btnReloadOnAction(actionEvent); // Reload the table to show updated data
            // Show success alert
            showAlert("Success", "Delivery added successfully!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error alert
            showAlert("Database Error", "An error occurred while adding the delivery.", Alert.AlertType.ERROR);
        }
    }

    // Update delivery
    public void btnUpdateOnAction(ActionEvent actionEvent) {
        try {
            Delivery delivery = new Delivery(
                    txtDeliveryId.getText(),
                    txtCustomerName.getText(),
                    txtOrderID.getText(),
                    txtAddress.getText(),
                    txtStatus.getText(),
                    txtDate.getText()
            );
            DBConnection.getInstance().updateDelivery(delivery);
            btnReloadOnAction(actionEvent); // Reload the table to show updated data
            // Show success alert
            showAlert("Success", "Delivery updated successfully!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error alert
            showAlert("Database Error", "An error occurred while updating the delivery.", Alert.AlertType.ERROR);
        }
    }

    // Delete delivery
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try {
            String deliveryId = txtDeliveryId.getText();
            DBConnection.getInstance().deleteDelivery(deliveryId);
            btnReloadOnAction(actionEvent); // Reload the table to show updated data
            // Show success alert
            showAlert("Success", "Delivery deleted successfully!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error alert
            showAlert("Database Error", "An error occurred while deleting the delivery.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnSearchOnAction(ActionEvent event) {

        // Get the orderId from the search input field (assuming there's a TextField for the orderId)
        String searchOrderId = txtDeliveryId.getText().trim();

        // Validate that the orderId is not empty
        if (searchOrderId.isEmpty()) {
            showAlert("Input Error", "Please enter a valid Order ID.", Alert.AlertType.ERROR);
            return;
        }

        // Database connection and query
        String query = "SELECT * FROM delivery WHERE deliveryID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, searchOrderId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // If order found, populate the details into the form
                txtDeliveryId.setText(resultSet.getString(1));
                txtOrderID.setText(resultSet.getString(2));
                txtCustomerName.setText(resultSet.getString(3));
                txtAddress.setText(resultSet.getString(4));
                txtStatus.setText(resultSet.getString(5));
                txtDate.setText(resultSet.getString(6));

                // Optionally show a success alert
                showAlert("Success", "Order details found!", Alert.AlertType.INFORMATION);
            } else {
                // If no order found with the given orderId
                showAlert("No Results", "No order found with the provided Order ID.", Alert.AlertType.WARNING);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while searching the database.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}

