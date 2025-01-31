package controller.customer;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerController implements CustomerService{
    @Override
    public boolean addCustomer(Customer customer) {
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String query = "INSERT INTO customer (id, name, address, salary) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, customer.getId());
                preparedStatement.setString(2, customer.getName());
                preparedStatement.setString(3, customer.getAddress());
                preparedStatement.setDouble(4, customer.getSalary());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String query = "UPDATE customer SET name = ?, address = ?, salary = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, customer.getName());
                preparedStatement.setString(2, customer.getAddress());
                preparedStatement.setDouble(3, customer.getSalary());
                preparedStatement.setString(4, customer.getId());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Customer searchCustomer(String id) {
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String query = "SELECT * FROM customer WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return new Customer(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            resultSet.getString("address"),
                            resultSet.getDouble("salary")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Customer> getAll() {
        ArrayList<Customer> customerArrayList = new ArrayList<>();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customer");

            while (resultSet.next()) {
                Customer customer = new Customer(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4)
                );
                customerArrayList.add(customer);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customerArrayList;
    }

    @Override
    public boolean deleteCustomer(String id) {
        String query = "DELETE FROM customer WHERE id = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the customer ID to be deleted
            statement.setString(1, id);  // Ensure you pass the customer ID as a string

            // Execute the update and check how many rows were affected
            int rowsAffected = statement.executeUpdate();

            // If rows were affected, return true
            if (rowsAffected > 0) {
                System.out.println("Customer with ID " + id + " deleted.");
                return true;
            } else {
                System.out.println("No customer found with ID: " + id);
                return false;
            }

        } catch (SQLException e) {
            // Log the error message and exception stack trace
            System.err.println("Error deleting customer: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public ObservableList<String> getCustomerIds(){
        List<Customer> customerList = getAll();
        ObservableList<String> customerIdList = FXCollections.observableArrayList();

        customerList.forEach(customer -> {
            customerIdList.add(customer.getId());
        });

        return customerIdList;

    }

}
