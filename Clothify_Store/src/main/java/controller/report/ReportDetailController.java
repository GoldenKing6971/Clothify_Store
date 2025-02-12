package controller.report;

import db.DBConnection;
import model.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ReportDetailController {
    public boolean addReportDetail(List<Report> reportDetails){
        for (Report report:reportDetails){
            boolean isAddReportDetail = addReportDetail(report);

            if (!isAddReportDetail){
                return false;
            }
        }
        return true;
    }

    public boolean addReportDetail(Report report){
        String SQL = "INSERT INTO report VALUES(?,?,?,?,?)";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(SQL);
            psTm.setObject(1,report.getDate());
            psTm.setObject(2,report.getOrderID());
            psTm.setObject(3,report.getItemCode());
            psTm.setObject(4,report.getOrderAmount());
            psTm.setObject(5,report.getTotalAmount());
            return psTm.executeUpdate()>0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
