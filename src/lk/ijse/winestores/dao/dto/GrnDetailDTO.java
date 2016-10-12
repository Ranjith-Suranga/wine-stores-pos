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
public class GrnDetailDTO {

    private String grnDetailId;
    private String itemId;
    private String batchId;
    private int batchQty;
    private String sellingPrice;
    private String buyingPrice;
    private String expiryDate;

    public GrnDetailDTO() {
    }

    public GrnDetailDTO(String grnDetailId, String itemId, String batchId, int batchQty, String sellingPrice, String buyingPrice, String expiryDate) {
        this.grnDetailId = grnDetailId;
        this.itemId = itemId;
        this.batchId = batchId;
        this.batchQty = batchQty;
        this.sellingPrice = sellingPrice;
        this.buyingPrice = buyingPrice;
        this.expiryDate = expiryDate;
    }



    /* @
    return the grnDetailId

    */
    public String getGrnDetailId() {
        return grnDetailId;
    }

    /**
     * @param grnDetailId the grnDetailId to set
     */
    public void setGrnDetailId(String grnDetailId) {
        this.grnDetailId = grnDetailId;
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
     * @return the batchId
     */
    public String getBatchId() {
        return batchId;
    }

    /**
     * @param batchId the batchId to set
     */
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    /**
     * @return the batchQty
     */
    public int getBatchQty() {
        return batchQty;
    }

    /**
     * @param batchQty the batchQty to set
     */
    public void setBatchQty(int batchQty) {
        this.batchQty = batchQty;
    }

    /**
     * @return the sellingPrice
     */
    public String getSellingPrice() {
        return sellingPrice;
    }

    /**
     * @param sellingPrice the sellingPrice to set
     */
    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    /**
     * @return the buyingPrice
     */
    public String getBuyingPrice() {
        return buyingPrice;
    }

    /**
     * @param buyingPrice the buyingPrice to set
     */
    public void setBuyingPrice(String buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    /**
     * @return the expiryDate
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
