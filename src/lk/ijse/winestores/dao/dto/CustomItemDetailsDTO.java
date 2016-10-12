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
public class CustomItemDetailsDTO {
    
    private String itemCode;
    private String itemName;
    private String barCode;    
    private double buyingPrice;
    private double sellingPrice;
    private int qty;

    public CustomItemDetailsDTO(String barCode, String itemCode, String itemName, int qty, double sellingPrice) {
        this.barCode = barCode;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.qty = qty;
        this.sellingPrice = sellingPrice;
    }

    public CustomItemDetailsDTO(String itemCode, String itemName, String barCode, double buyingPrice, double sellingPrice, int qty) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.barCode = barCode;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.qty = qty;
    }
    
    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }    

    /**
     * @return the barCode
     */
    public String getBarCode() {
        return barCode;
    }

    /**
     * @param barCode the barCode to set
     */
    public void setBarCode(String barCode) {
        this.barCode = barCode;
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
     * @return the sellingPrice
     */
    public double getSellingPrice() {
        return sellingPrice;
    }

    /**
     * @param sellingPrice the sellingPrice to set
     */
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    /**
     * @return the qty
     */
    public int getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(int qty) {
        this.qty = qty;
    }
    
}
