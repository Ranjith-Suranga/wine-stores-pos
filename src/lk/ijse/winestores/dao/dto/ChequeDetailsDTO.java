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
public class ChequeDetailsDTO {

    private String chequeDetailsId;
    private String orderId;
    private String chequeNumber;
    private String bank;
    private String branch;

    public ChequeDetailsDTO(String chequeDetailsId, String orderId, String chequeNumber, String bank, String branch) {
        this.chequeDetailsId = chequeDetailsId;
        this.orderId = orderId;
        this.chequeNumber = chequeNumber;
        this.bank = bank;
        this.branch = branch;
    }

    /**
     * @return the chequeDetailsId
     */
    public String getChequeDetailsId() {
        return chequeDetailsId;
    }

    /**
     * @param chequeDetailsId the chequeDetailsId to set
     */
    public void setChequeDetailsId(String chequeDetailsId) {
        this.chequeDetailsId = chequeDetailsId;
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
     * @return the chequeNumber
     */
    public String getChequeNumber() {
        return chequeNumber;
    }

    /**
     * @param chequeNumber the chequeNumber to set
     */
    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    /**
     * @return the bank
     */
    public String getBank() {
        return bank;
    }

    /**
     * @param bank the bank to set
     */
    public void setBank(String bank) {
        this.bank = bank;
    }

    /**
     * @return the branch
     */
    public String getBranch() {
        return branch;
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

}
