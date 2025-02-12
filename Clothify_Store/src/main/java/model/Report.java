package model;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Report {
    private String date;
    private String orderID;
    private String itemCode;
    private Double orderAmount;
    private Double totalAmount;



}
