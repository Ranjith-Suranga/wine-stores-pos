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
public class ItemDetailsDTO {

    private String itemName;
    private String itemCode;
    private int qty;
    private double sellingPrice;
    private double buyingPrice;

    public ItemDetailsDTO() {
    }

    public ItemDetailsDTO(String itemCode, int qty, double sellingPrice) {
        this.itemCode = itemCode;
        this.qty = qty;
        this.sellingPrice = sellingPrice;
    }

    public ItemDetailsDTO(String itemName, String itemCode, int qty, double sellingPrice, double buyingPrice) {
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.qty = qty;
        this.sellingPrice = sellingPrice;
        this.buyingPrice = buyingPrice;
    }

    public ItemDetailsDTO(String itemCode, int qty, double sellingPrice, double buyingPrice) {
        this.itemCode = itemCode;
        this.qty = qty;
        this.sellingPrice = sellingPrice;
        this.buyingPrice = buyingPrice;
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
     * @return the buyingPrice
     */
    public double getBuyingPrice() {
        return buyingPrice;
    }

    /**
     * @param buyingPrice the buyingPrice to set
     */
    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }
}
