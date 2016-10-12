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
public class GrnDTO {

    private String grnId;
    private String supplierId;
    private String invoiceDate;
    private String grnDate;
    private String whoMade;
    private String editDateTime;
    private String invoiceId;

    public GrnDTO() {
    }

    public GrnDTO(String grnId, String supplierId, String invoiceDate, String grnDate, String whoMade, String editDateTime, String invoiceId) {
        this.grnId = grnId;
        this.supplierId = supplierId;
        this.invoiceDate = invoiceDate;
        this.grnDate = grnDate;
        this.whoMade = whoMade;
        this.editDateTime = editDateTime;
        this.invoiceId = invoiceId;
    }

    /**
     * @return the grnId
     */
    public String getGrnId() {
        return grnId;
    }

    /**
     * @param grnId the grnId to set
     */
    public void setGrnId(String grnId) {
        this.grnId = grnId;
    }

    /**
     * @return the supplierId
     */
    public String getSupplierId() {
        return supplierId;
    }

    /**
     * @param supplierId the supplierId to set
     */
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return the invoiceDate
     */
    public String getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * @param invoiceDate the invoiceDate to set
     */
    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /**
     * @return the grnDate
     */
    public String getGrnDate() {
        return grnDate;
    }

    /**
     * @param grnDate the grnDate to set
     */
    public void setGrnDate(String grnDate) {
        this.grnDate = grnDate;
    }

    /**
     * @return the whoMade
     */
    public String getWhoMade() {
        return whoMade;
    }

    /**
     * @param whoMade the whoMade to set
     */
    public void setWhoMade(String whoMade) {
        this.whoMade = whoMade;
    }

    /**
     * @return the editDateTime
     */
    public String getEditDateTime() {
        return editDateTime;
    }

    /**
     * @param editDateTime the editDateTime to set
     */
    public void setEditDateTime(String editDateTime) {
        this.editDateTime = editDateTime;
    }

    /**
     * @return the invoiceId
     */
    public String getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId the invoiceId to set
     */
    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }
}
