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
public class CreditOrderEmptyBottleDetailsDTO {

    private String orderEmptyBottleDetailsId;
    private String orderId;
    private String bottleType;
    private int qty;
    private double total;

    public CreditOrderEmptyBottleDetailsDTO(String orderEmptyBottleEetailsId, String orderId, String bottleType, int qty, double total) {
        this.orderEmptyBottleDetailsId = orderEmptyBottleEetailsId;
        this.orderId = orderId;
        this.bottleType = bottleType;
        this.qty = qty;
        this.total = total;
    }

    /**
     * @return the orderEmptyBottleEetailsId
     */
    public String getOrderEmptyBottleEetailsId() {
        return orderEmptyBottleDetailsId;
    }

    /**
     * @param orderEmptyBottleEetailsId the orderEmptyBottleEetailsId to set
     */
    public void setOrderEmptyBottleEetailsId(String orderEmptyBottleEetailsId) {
        this.orderEmptyBottleDetailsId = orderEmptyBottleEetailsId;
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
     * @return the bottleType
     */
    public String getBottleType() {
        return bottleType;
    }

    /**
     * @param bottleType the bottleType to set
     */
    public void setBottleType(String bottleType) {
        this.bottleType = bottleType;
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
}
