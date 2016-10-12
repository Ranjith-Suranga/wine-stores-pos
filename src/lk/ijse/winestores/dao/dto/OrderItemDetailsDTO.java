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
public class OrderItemDetailsDTO {

    private String orderItemDetailsId;
    private String orderId;
    private String itemCode;
    private int qty;
    private double sellingPrice;

    public OrderItemDetailsDTO(String orderItemDetailsId, String orderId, String itemCode, int qty, double sellingPrice) {
        this.orderItemDetailsId = orderItemDetailsId;
        this.orderId = orderId;
        this.itemCode = itemCode;
        this.qty = qty;
        this.sellingPrice = sellingPrice;
    }

    /**
     * @return the orderItemDetailsId
     */
    public String getOrderItemDetailsId() {
        return orderItemDetailsId;
    }

    /**
     * @param orderItemDetailsId the orderItemDetailsId to set
     */
    public void setOrderItemDetailsId(String orderItemDetailsId) {
        this.orderItemDetailsId = orderItemDetailsId;
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
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return the qty
     */
    public int getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(int qty) {
        this.qty = qty;
    }

    /**
     * @return the sellingPrice
     */
    public double getSellingPrice() {
        return sellingPrice;
    }

    /**
     * @param sellingPrice the sellingPrice to set
     */
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
