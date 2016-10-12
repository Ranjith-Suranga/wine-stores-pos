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
public class GrnEmptyBottleDetailDTO {

    private String id;
    private int estimatedBottles;
    private int givenBottles;
    private String dueDate;
    private String grnId;
    private String bottleType;

    public GrnEmptyBottleDetailDTO() {
    }

    public GrnEmptyBottleDetailDTO(String id, int estimatedBottles, int givenBottles, String dueDate, String grnId, String bottleType) {
        this.id = id;
        this.estimatedBottles = estimatedBottles;
        this.givenBottles = givenBottles;
        this.dueDate = dueDate;
        this.grnId = grnId;
        this.bottleType = bottleType;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the estimatedBottles
     */
    public int getEstimatedBottles() {
        return estimatedBottles;
    }

    /**
     * @param estimatedBottles the estimatedBottles to set
     */
    public void setEstimatedBottles(int estimatedBottles) {
        this.estimatedBottles = estimatedBottles;
    }

    /**
     * @return the givenBottles
     */
    public int getGivenBottles() {
        return givenBottles;
    }

    /**
     * @param givenBottles the givenBottles to set
     */
    public void setGivenBottles(int givenBottles) {
        this.givenBottles = givenBottles;
    }

    /**
     * @return the dueDate
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
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

}
