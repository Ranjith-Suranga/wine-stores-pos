/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.dto;

import java.math.BigDecimal;

/**
 *
 * @author Ranjith Suranga
 */
public class CreditOrderDTO {

    private String orderId;
    private String orderDate;
    private String madeBy;
    private double total;
    private String customerId;
//    private BigDecimal tenderedCash;

    public CreditOrderDTO() {
    }

    public CreditOrderDTO(String orderId, String orderDate, String madeBy, double total, String customerId) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.madeBy = madeBy;
        this.total = total;
        this.customerId = customerId;
//        this.tenderedCash = tenderedCash;
    }
    
//    public CreditOrderDTO(String orderId, String orderDate, String madeBy, double total, String customerId ) {
//        this.orderId = orderId;
//        this.orderDate = orderDate;
//        this.madeBy = madeBy;
//        this.total = total;
//        this.customerId = customerId;
//    }
    
    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the orderDate
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * @return the madeBy
     */
    public String getMadeBy() {
        return madeBy;
    }

    /**
     * @param madeBy the madeBy to set
     */
    public void setMadeBy(String madeBy) {
        this.madeBy = madeBy;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

//    /**
//     * @return the tenderedCash
//     */
//    public BigDecimal getTenderedCash() {
//        return tenderedCash;
//    }
//
//    /**
//     * @param tenderedCash the tenderedCash to set
//     */
//    public void setTenderedCash(BigDecimal tenderedCash) {
//        this.tenderedCash = tenderedCash;
//    }

}
