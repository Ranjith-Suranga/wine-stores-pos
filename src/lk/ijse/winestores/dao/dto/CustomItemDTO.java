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
public class CustomItemDTO {
    
    private String itemId;
    private String itemName;
    private String itemCode;
    private String capacity;
    private String barcode;
    private String majorCategoryId;
    private String subCategoryId;

    public CustomItemDTO() {
    }

    public CustomItemDTO(String itemId, String itemName, String itemCode, String capacity, String barcode, String majorCategoryId, String subCategoryId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.capacity = capacity;
        this.barcode = barcode;
        this.majorCategoryId = majorCategoryId;
        this.subCategoryId = subCategoryId;
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
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    /**
     * @return the majorCategoryId
     */
    public String getMajorCategoryId() {
        return majorCategoryId;
    }

    /**
     * @param majorCategoryId the majorCategoryId to set
     */
    public void setMajorCategoryId(String majorCategoryId) {
        this.majorCategoryId = majorCategoryId;
    }

    /**
     * @return the subCategoryId
     */
    public String getSubCategoryId() {
        return subCategoryId;
    }

    /**
     * @param subCategoryId the subCategoryId to set
     */
    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
    
}
