package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private static Connection connection;

    private DBConnection() throws SQLException {
        String URL = "jdbc:mysql://localhost:3306/clothify_store";
        String userName = "root";
        String password = "12345";
        connection = DriverManager.getConnection(URL, userName, password);
    }

    public static DBConnection getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DBConnection();
        }
        return instance;
    }

    public static Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch all deliveries from the database
    public ResultSet getAllDeliveries() throws SQLException {
        String query = "SELECT * FROM Delivery";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    // Insert a new delivery
    public void addDelivery(model.Delivery delivery) throws SQLException {
        String query = "INSERT INTO Delivery (deliveryID, customerName, orderID, address, status, date) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, delivery.getDeliveryId());
        preparedStatement.setString(2, delivery.getCustomerName());
        preparedStatement.setString(3, delivery.getOrderId());
        preparedStatement.setString(4, delivery.getAddress());
        preparedStatement.setString(5, delivery.getStatus());
        preparedStatement.setString(6, delivery.getDate());
        preparedStatement.executeUpdate();
    }

    // Update a delivery
    public void updateDelivery(model.Delivery delivery) throws SQLException {
        String query = "UPDATE Delivery SET customerName=?, orderID=?, address=?, status=?, date=? WHERE deliveryID=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, delivery.getCustomerName());
        preparedStatement.setString(2, delivery.getOrderId());
        preparedStatement.setString(3, delivery.getAddress());
        preparedStatement.setString(4, delivery.getStatus());
        preparedStatement.setString(5, delivery.getDate());
        preparedStatement.setString(6, delivery.getDeliveryId());
        preparedStatement.executeUpdate();
    }

    // Delete a delivery
    public void deleteDelivery(String deliveryId) throws SQLException {
        String query = "DELETE FROM Delivery WHERE deliveryID=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, deliveryId);
        preparedStatement.executeUpdate();
    }

    // Update status
    public void updateStatus(String deliveryId, String status) throws SQLException {
        String query = "UPDATE Delivery SET status=? WHERE deliveryID=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, status);
        preparedStatement.setString(2, deliveryId);
        preparedStatement.executeUpdate();
    }
}
