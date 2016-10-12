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
public class SupplierOrderDetailDTO {

    private Integer orderDetailId;
    private int orderId;
    private String itemCode;
    private int qty;
    private BigDecimal buyingPrice;

    public SupplierOrderDetailDTO(Integer orderDetailId, int orderId, String itemCode, int qty, BigDecimal buyingPrice) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.itemCode = itemCode;
        this.qty = qty;
        this.buyingPrice = buyingPrice;
    }

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(BigDecimal buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    /**
     * @return the orderId
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

}
