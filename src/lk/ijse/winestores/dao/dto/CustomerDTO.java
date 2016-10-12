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
public class CustomerDTO{

    private int customerId;
    private String customerName;
    private String telephoneNumber;
    private String address;

    public CustomerDTO() {
    }

    public CustomerDTO(int customerId) {
        this.customerId = customerId;
    }

    public CustomerDTO(int customerId, String customerName, String telephoneNumber, String address) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.telephoneNumber = telephoneNumber;
        this.address = address;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
}
