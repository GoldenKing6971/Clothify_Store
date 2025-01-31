package controller.order;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.customer.CustomerController;
import controller.item.ItemController;
import db.DBConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.Customer;
import model.Item;
import model.Order;
import model.OrderDetail;
import model.TM.CartTM;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class OrderFormController implements Initializable {

    @FXML
    private JFXComboBox cmbCustomerID;

    @FXML
    private JFXComboBox cmbItemID;

    @FXML
    private TableColumn colDescription;

    @FXML
    private TableColumn colItem;

    @FXML
    private TableColumn colQTYOnHand;

    @FXML
    private TableColumn colTotal;

    @FXML
    private TableColumn colUnityPrice;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblTime;

    @FXML
    private TableView<CartTM> tblCard;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtQTY;

    @FXML
    private JFXTextField txtStock;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    private JFXTextField txtorderID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItem.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQTYOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnityPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        setDateAndTime();
        loadCustomerIds();
        loadItemCodes();

        cmbCustomerID.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue!=null){
                searchCustomerData(newValue.toString());
            }
        });

        cmbItemID.getSelectionModel().selectedItemProperty().addListener((observableValue, oldvalue, newValue) -> {
            if (newValue != null) {
                searchItemData(newValue.toString());
            }
        });

    }

    private void loadItemCodes() {
        cmbItemID.setItems(new ItemController().getItemCodes());
    }

    private void searchItemData(String code) {
        Item item = new ItemController().searchItem(code);

        txtDescription.setText(item.getDescription());
        txtStock.setText(item.getStock().toString());
        txtUnitPrice.setText(item.getUnitPrice().toString());
    }

    private void searchCustomerData(String cmbCustomerID) {
        Customer customer = new CustomerController().searchCustomer(cmbCustomerID);

        txtCustomerName.setText(customer.getName());
        txtAddress.setText(customer.getAddress());
    }

    private void loadCustomerIds(){
        ObservableList<String> customerIds = new CustomerController().getCustomerIds();
        cmbCustomerID.setItems(customerIds);
    }

    private void setDateAndTime(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = dateFormat.format(date);
        lblDate.setText(format);

//--------------------------------------------------------------

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    LocalTime now = LocalTime.now();
                    lblTime.setText(now.getHour()+":"+now.getMinute()+":"+now.getSecond());
                }),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


    }

    ObservableList<CartTM> cartTMS = FXCollections.observableArrayList();

    @FXML
    public void btnAddToCartOnAction(ActionEvent actionEvent) {

        String code = cmbItemID.getValue().toString();
        String description = txtDescription.getText();
        Integer qtyOnHand = Integer.parseInt(txtQTY.getText());
        Double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        Double total = qtyOnHand*unitPrice;

        cartTMS.add(new CartTM(code,description,qtyOnHand,unitPrice,total));

        tblCard.setItems(cartTMS);

        calcNetTotal();

    }

    private void calcNetTotal(){
        Double netTotal=0.0;

        for (CartTM tm: cartTMS){
            netTotal+=tm.getTotal();
        }

        lblNetTotal.setText(netTotal.toString());
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) throws SQLException {
        String orderId = txtorderID.getText();
        String date = lblDate.getText();
        String customerId = cmbCustomerID.getValue().toString();

        ArrayList<OrderDetail> orderDetails = new ArrayList<>();

        cartTMS.forEach(cartTM -> {
            orderDetails.add(
                    new OrderDetail(
                        orderId,
                        cartTM.getItemCode(),
                        cartTM.getQtyOnHand(),
                        cartTM.getUnitPrice()
                    )
            );

        });

        Order order = new Order(orderId, date, customerId, orderDetails);

        if (new OrderController().placeOrder(order)){
            new Alert(Alert.AlertType.INFORMATION, "Order Placed !!").show();
        }else{
            new Alert(Alert.AlertType.ERROR, "Order Not Placed !!").show();
        }

    }
}
