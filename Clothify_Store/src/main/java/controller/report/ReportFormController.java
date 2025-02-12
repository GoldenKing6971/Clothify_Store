package controller.report;

import controller.order.OrderFormController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Report;
import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReportFormController {

    public Label lblNetTotal;
    @FXML
    private TableColumn<Report, LocalDate> colDate;

    @FXML
    private TableColumn<Report, String> colItemCode;

    @FXML
    private TableColumn<Report, Integer> colOrderAmount;

    @FXML
    private TableColumn<Report, String> colOrderID;

    @FXML
    private TableColumn<Report, Double> colTotalAmount;

    @FXML
    private DatePicker dateSearch;

    @FXML
    private TableView<Report> tblRepoart;

    private ObservableList<Report> reportList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadReportData();
    }

    private void loadReportData() {
        reportList.clear();
        String query = "SELECT * FROM Report"; // Adjust table name and columns as needed

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Report report = new Report(
                        resultSet.getDate(1).toString(),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getDouble(5)
                );
                reportList.add(report);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        tblRepoart.setItems(reportList);
    }


    private void calcNetTotal() {
        Double netTotal = 0.0;

        for (Report report : reportList) {
            netTotal += report.getTotalAmount();
        }

        lblNetTotal.setText(netTotal.toString());
    }




    @FXML
    void btnReloadOnAction(ActionEvent event) {

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colOrderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colOrderAmount.setCellValueFactory(new PropertyValueFactory<>("orderAmount"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        LocalDate selectedDate = dateSearch.getValue();
        if (selectedDate != null) {
            loadReportDataByDate(selectedDate);
        } else {
            loadReportData();
        }

        calcNetTotal();
    }

    private void loadReportDataByDate(LocalDate date) {
        reportList.clear();
        String query = "SELECT * FROM Report WHERE date = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDate(1, java.sql.Date.valueOf(date));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Report report = new Report(
                        resultSet.getDate(1).toString(),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getDouble(5)
                );
                reportList.add(report);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        tblRepoart.setItems(reportList);
    }

}
