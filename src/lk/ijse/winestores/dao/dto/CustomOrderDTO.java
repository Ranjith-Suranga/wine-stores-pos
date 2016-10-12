/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.dto;

/**
 *
 * @author Ranjith Suranga
 */
public class CustomOrderDTO {

    private String orderId;
    private String orderDate;
    private String madeBy;
    private double total;
    private String paymentId;

    public CustomOrderDTO() {
    }

    public CustomOrderDTO(String orderId, String orderDate, String madeBy, double total, String paymentId) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.madeBy = madeBy;
        this.total = total;
        this.paymentId = paymentId;
    }
    
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
     * @return the paymentId
     */
    public String getPaymentId() {
        return paymentId;
    }

    /**
     * @param paymentId the paymentId to set
     */
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

}
