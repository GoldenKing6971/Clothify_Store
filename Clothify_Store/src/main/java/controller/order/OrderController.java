package controller.order;

import controller.item.ItemController;
import controller.report.ReportDetailController;
import db.DBConnection;
import model.Order;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderController {

    public static String getLastOrderId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT id FROM Orders ORDER BY id DESC LIMIT 1");
        return rst.next() ? rst.getString("id") : null;
    }

    public boolean placeOrder(Order order) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String SQL = "INSERT INTO orders VALUES(?,?,?)";

        try {

            connection.setAutoCommit(false);
            PreparedStatement psTm = connection.prepareStatement(SQL);
            psTm.setObject(1, order.getId());
            psTm.setObject(2, order.getDate());
            psTm.setObject(3, order.getCustomerId());
            Boolean isOrderAdd = psTm.executeUpdate() > 0;
            if (isOrderAdd){
                boolean isOrderDetailAdd = new OrderDetailController().addOrderDetail(order.getOrderDetails());

                if (isOrderDetailAdd){
                    boolean isReportDetailAdd = new ReportDetailController().addReportDetail(order.getReportDetail());

                    if (isReportDetailAdd) {
                        boolean isUpdateStock = new ItemController().updateStock(order.getOrderDetails());

                        if (isUpdateStock) {
                            connection.commit();
                            return true;
                        }
                    }
                }
            }

        }finally {
            connection.setAutoCommit(true);
        }
        connection.rollback();
        return false;
    }
}
