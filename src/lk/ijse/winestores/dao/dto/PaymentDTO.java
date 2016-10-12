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
public class PaymentDTO {

    private String paymentId;
    private String paymentMethod;

    public PaymentDTO() {
    }

    public PaymentDTO(String paymentId, String paymentMethod) {
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
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

    /**
     * @return the paymentMethod
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * @param paymentMethod the paymentMethod to set
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

}
