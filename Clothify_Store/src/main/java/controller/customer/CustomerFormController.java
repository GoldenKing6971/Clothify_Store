package controller.customer;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerFormController {

    @FXML
    private TableColumn colAddress;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colSalary;

    @FXML
    private TableView tblCustomer;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSalary;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        double salary = Double.parseDouble(txtSalary.getText());

        Customer customer = new Customer(id, name, address, salary);

        boolean isAdded = new CustomerController().addCustomer(customer);

        if (isAdded) {
            loadTable(); // Reload table after adding

            // Display success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add Successful");
            alert.setHeaderText(null);
            alert.setContentText("Customer has been successfully added.");
            alert.showAndWait();
        } else {
            // Display error alert if adding failed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to add customer. Please try again.");
            alert.showAndWait();
        }
    }


    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        // Get the customer details from the text fields
        String customerId = txtId.getText();  // Assuming you have a text field for customer ID
        String customerName = txtName.getText();  // If you have other fields like name

        // Check if a customer ID is entered
        if (customerId != null && !customerId.trim().isEmpty()) {
            // Attempt to delete the customer
            boolean isDeleted = new CustomerController().deleteCustomer(customerId);

            if (isDeleted) {
                loadTable();  // Reload the table after deletion
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer deleted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete customer.");
            }
        } else {
            // Show an error if no customer ID is provided
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please enter a valid customer ID.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadTable();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchId = txtId.getText();
        Customer customer = new CustomerController().searchCustomer(searchId);

        if (customer != null) {
            // If customer is found, display details in the text fields
            txtName.setText(customer.getName());
            txtAddress.setText(customer.getAddress());
            txtSalary.setText(String.valueOf(customer.getSalary()));

            // Display success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer Found");
            alert.setHeaderText(null);
            alert.setContentText("Customer details have been successfully found and loaded.");
            alert.showAndWait();
        } else {
            // If customer is not found, display an error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer Not Found");
            alert.setHeaderText(null);
            alert.setContentText("No customer found with the provided ID.");
            alert.showAndWait();
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        double salary = Double.parseDouble(txtSalary.getText());

        Customer customer = new Customer(id, name, address, salary);
        boolean isUpdated = new CustomerController().updateCustomer(customer);

        if (isUpdated) {
            loadTable(); // Reload table after update

            // Display success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Successful");
            alert.setHeaderText(null);
            alert.setContentText("Customer details have been successfully updated.");
            alert.showAndWait();
        } else {
            // Display error alert if update failed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update customer details. Please try again.");
            alert.showAndWait();
        }
    }


    private void loadTable(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();

        List<Customer> all = new CustomerController().getAll();
        all.forEach(customer -> {
            customerObservableList.add(customer);
        });

        tblCustomer.setItems(customerObservableList);

    }

}