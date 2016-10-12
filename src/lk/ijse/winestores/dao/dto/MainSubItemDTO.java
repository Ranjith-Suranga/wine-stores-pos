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
public class MainSubItemDTO {
    
    private String mainSubItemId;
    private String mainSubId;
    private String itemId;
    private String itemCode;
    private String capacity;
    private String barcode;

    public MainSubItemDTO() {
    }

//    public MainSubItemDTO(String mainSubItemId, String mainSubId, String itemId) {
//        this.mainSubItemId = mainSubItemId;
//        this.mainSubId = mainSubId;
//        this.itemId = itemId;
//    }

//    public MainSubItemDTO(String mainSubItemId, String mainSubId, String itemId, String itemCode) {
//        this.mainSubItemId = mainSubItemId;
//        this.mainSubId = mainSubId;
//        this.itemId = itemId;
//        this.itemCode = itemCode;
//    }

    public MainSubItemDTO(String mainSubItemId, String mainSubId, String itemId, String itemCode, String capacity, String barcode) {
        this.mainSubItemId = mainSubItemId;
        this.mainSubId = mainSubId;
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.capacity = capacity;
        this.barcode = barcode;
    }
    
    /**
     * @return the mainSubItemId
     */
    public String getMainSubItemId() {
        return mainSubItemId;
    }

    /**
     * @param mainSubItemId the mainSubItemId to set
     */
    public void setMainSubItemId(String mainSubItemId) {
        this.mainSubItemId = mainSubItemId;
    }

    /**
     * @return the mainSubId
     */
    public String getMainSubId() {
        return mainSubId;
    }

    /**
     * @param mainSubId the mainSubId to set
     */
    public void setMainSubId(String mainSubId) {
        this.mainSubId = mainSubId;
    }

    /**
     * @return the itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
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
     * @return the capacity
     */
    public String getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    
}
