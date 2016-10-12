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
public class ItemDTO {
    
    private String itemId;
    private String itemName;
//    private String capacity;
//    private String barcode;

    public ItemDTO() {
    }

    public ItemDTO(String itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }
    
    

//    public ItemDTO(String itemId, String itemName, String capacity, String barcode) {
//        this.itemId = itemId;
//        this.itemName = itemName;
//        this.capacity = capacity;
//        this.barcode = barcode;
//    }

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

//    /**
//     * @return the capacity
//     */
//    public String getCapacity() {
//        return capacity;
//    }
//
//    /**
//     * @param capacity the capacity to set
//     */
//    public void setCapacity(String capacity) {
//        this.capacity = capacity;
//    }
//
//    /**
//     * @return the barcode
//     */
//    public String getBarcode() {
//        return barcode;
//    }
//
//    /**
//     * @param barcode the barcode to set
//     */
//    public void setBarcode(String barcode) {
//        this.barcode = barcode;
//    }
    
}
