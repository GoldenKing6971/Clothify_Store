package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Delivery {
    private String deliveryId;
    private String orderId;
    private String customerName;
    private String address;
    private String status;
    private String date;

}
