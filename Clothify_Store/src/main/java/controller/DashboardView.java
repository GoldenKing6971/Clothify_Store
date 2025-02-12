package controller;

import db.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardView implements Initializable {

    @FXML
    private Label lblCustomer;

    @FXML
    private Label lblOrders;

    @FXML
    private Label lblProduct;

    @FXML
    private Label lblTotal;

    // Initialize the connection to the database
    private Connection connection;

    public DashboardView() {
        // Establish connection
        connection = DBConnection.getConnection();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateDashboard(); // Update the labels with values
    }

    private void updateDashboard() {
        try {
            // Fetch customer count (Modify if your table is named differently)
            String customerQuery = "SELECT COUNT(*) FROM customer";
            PreparedStatement customerStatement = connection.prepareStatement(customerQuery);
            ResultSet customerResultSet = customerStatement.executeQuery();
            if (customerResultSet.next()) {
                lblCustomer.setText(" " + customerResultSet.getInt(1));
            }

            // Fetch order count
            String orderQuery = "SELECT COUNT(*) FROM orders";  // Change 'orders' if needed
            PreparedStatement orderStatement = connection.prepareStatement(orderQuery);
            ResultSet orderResultSet = orderStatement.executeQuery();
            if (orderResultSet.next()) {
                lblOrders.setText(" " + orderResultSet.getInt(1));
            }

            // Fetch product count
            String productQuery = "SELECT COUNT(*) FROM item";  // Change 'products' if needed
            PreparedStatement productStatement = connection.prepareStatement(productQuery);
            ResultSet productResultSet = productStatement.executeQuery();
            if (productResultSet.next()) {
                lblProduct.setText(" " + productResultSet.getInt(1));
            }

            // Fetch total amount from orders
            String totalQuery = "SELECT SUM(totalAmount) FROM report";  // Change 'orders' if needed
            PreparedStatement totalStatement = connection.prepareStatement(totalQuery);
            ResultSet totalResultSet = totalStatement.executeQuery();
            if (totalResultSet.next()) {
                lblTotal.setText(" " + totalResultSet.getDouble(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
