/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.dto;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Ranjith Suranga
 */

public class SupplierOrderDTO{

    private Integer orderId;
    private String supplierId;
    private Date orderDate;
    private String madeBy;
    private String orderTotal;
    
    private ArrayList<SupplierOrderDetailDTO> orderDetails;

    public SupplierOrderDTO() {
    }

    public SupplierOrderDTO(Integer orderId, String supplierId, Date orderDate, String madeBy, String orderTotal) {
        this.orderId = orderId;
        this.supplierId = supplierId;
        this.orderDate = orderDate;
        this.madeBy = madeBy;
        this.orderTotal = orderTotal;
    }

    public SupplierOrderDTO(Integer orderId, String supplierId, Date orderDate, String madeBy, String orderTotal, ArrayList<SupplierOrderDetailDTO> orderDetails) {
        this.orderId = orderId;
        this.supplierId = supplierId;
        this.orderDate = orderDate;
        this.madeBy = madeBy;
        this.orderTotal = orderTotal;
        this.orderDetails = orderDetails;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(String madeBy) {
        this.madeBy = madeBy;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }    

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return the orderDetails
     */
    public ArrayList<SupplierOrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    /**
     * @param orderDetails the orderDetails to set
     */
    public void setOrderDetails(ArrayList<SupplierOrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
