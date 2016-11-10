/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Ranjith Suranga
 */
public class DayEndDTO {

    private Date date;
    private String itemCode;
    private int openingQty;
    private int grnQty;
    private int salesQty;
    private BigDecimal sellingPrice;
    private BigDecimal buyingPrice;

    public DayEndDTO() {
    }

    public DayEndDTO(Date date, String itemCode, int openingQty, int grnQty, int currentQty, BigDecimal sellingPrice, BigDecimal buyingPrice) {
        this.date = date;
        this.itemCode = itemCode;
        this.openingQty = openingQty;
        this.grnQty = grnQty;
        this.salesQty = currentQty;
        this.sellingPrice = sellingPrice;
        this.buyingPrice = buyingPrice;
    }

    public int getOpeningQty() {
        return openingQty;
    }

    public void setOpeningQty(int openingQty) {
        this.openingQty = openingQty;
    }

    public int getGrnQty() {
        return grnQty;
    }

    public void setGrnQty(int grnQty) {
        this.grnQty = grnQty;
    }

    public int getSalesQty() {
        return salesQty;
    }

    public void setSalesQty(int currentQty) {
        this.salesQty = currentQty;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(BigDecimal buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
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

}
