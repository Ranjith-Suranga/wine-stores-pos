/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.dao.custom.GrnDAO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface GRNController extends SuperController {
    
    // I add this for testing
    
    public int saveGrn(GRNDetailsModel grnModel, ArrayList<GRNItemModel> items, ArrayList<GRNEmptyBottleDetailModel> emptyBottleDetails)throws ClassNotFoundException, SQLException;
    
    public int removeGrn(String grnId, boolean grnAuthorizedStatus) throws ClassNotFoundException, SQLException;   
    
    public int changeGrn(GRNDetailsModel grnModel, ArrayList<GRNItemModel> items, ArrayList<GRNEmptyBottleDetailModel> emptyBottleDetails,  boolean grnAuthorizedStatus)throws ClassNotFoundException, SQLException;
    
    public int authorizeGrn(String grnId) throws ClassNotFoundException, SQLException;    
    
    public String getNewGrnId() throws ClassNotFoundException, SQLException;
    
    public boolean isGrnAuthorized(String grnId)throws ClassNotFoundException, SQLException;      

    public ArrayList<EmptyBottleDTO> getAllEmptyBottleTypes() throws ClassNotFoundException, SQLException;
    
    public ArrayList<GRNDetailsModel> getGrns(GrnDAO.QueryType queryType, String queryWord)throws ClassNotFoundException, SQLException;

    public ArrayList<GRNEmptyBottleDetailModel> getEmptyBottleDetails(String grnId) throws ClassNotFoundException, SQLException;
    
    public ArrayList<GRNItemModel> getItemDetails(String grnId, boolean grnAuthorizedStatus)throws ClassNotFoundException,SQLException;    
    
    public class GRNDetailsModel {

        private String grnId;
        private String supplierId;
        private String invoiceDate;
        private String grnDate;
        private String user;
        private String lastEditedTime;
        private String invoiceId;

        public GRNDetailsModel() {
        }

        public GRNDetailsModel(String grnId, String supplierId, String invoiceDate, String grnDate, String user, String lastEditedTime, String invoiceId) {
            this.grnId = grnId;
            this.supplierId = supplierId;
            this.invoiceDate = invoiceDate;
            this.grnDate = grnDate;
            this.user = user;
            this.lastEditedTime = lastEditedTime;
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
         * @return the user
         */
        public String getUser() {
            return user;
        }

        /**
         * @param user the user to set
         */
        public void setUser(String user) {
            this.user = user;
        }

        /**
         * @return the lastEditedTime
         */
        public String getLastEditedTime() {
            return lastEditedTime;
        }

        /**
         * @param lastEditedTime the lastEditedTime to set
         */
        public void setLastEditedTime(String lastEditedTime) {
            this.lastEditedTime = lastEditedTime;
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

    public class GRNEmptyBottleDetailModel {

        private String id;
        private int estimatedBottles;
        private int givenBottles;
        private String dueDate;
        private String grnId;
        private String bottleType;

        public GRNEmptyBottleDetailModel() {
        }

        public GRNEmptyBottleDetailModel(String id, int estimatedBottles, int givenBottles, String dueDate, String grnId, String bottleType) {
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

    public class GRNItemModel {

        private String majorCategory;
        private String subCategory;
        private String itemCode;
        private String itemName;
        private int qty;
        private BigDecimal costPrice;
        private BigDecimal sellingPrice;
        private String expireDate;

        public GRNItemModel() {
        }

        public GRNItemModel(String majorCategory, String subCategory, String itemCode, String itemName, int qty, BigDecimal costPrice, BigDecimal sellingPrice, String expireDate) {
            this.majorCategory = majorCategory;
            this.subCategory = subCategory;
            this.itemCode = itemCode;
            this.itemName = itemName;
            this.qty = qty;
            this.costPrice = costPrice;
            this.sellingPrice = sellingPrice;
            this.expireDate = expireDate;
        }

        /**
         * @return the majorCategory
         */
        public String getMajorCategory() {
            return majorCategory;
        }

        /**
         * @param majorCategory the majorCategory to set
         */
        public void setMajorCategory(String majorCategory) {
            this.majorCategory = majorCategory;
        }

        /**
         * @return the subCategory
         */
        public String getSubCategory() {
            return subCategory;
        }

        /**
         * @param subCategory the subCategory to set
         */
        public void setSubCategory(String subCategory) {
            this.subCategory = subCategory;
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
         * @return the costPrice
         */
        public BigDecimal getCostPrice() {
            return costPrice;
        }

        /**
         * @param costPrice the costPrice to set
         */
        public void setCostPrice(BigDecimal costPrice) {
            this.costPrice = costPrice;
        }

        /**
         * @return the sellingPrice
         */
        public BigDecimal getSellingPrice() {
            return sellingPrice;
        }

        /**
         * @param sellingPrice the sellingPrice to set
         */
        public void setSellingPrice(BigDecimal sellingPrice) {
            this.sellingPrice = sellingPrice;
        }

        /**
         * @return the expireDate
         */
        public String getExpireDate() {
            return expireDate;
        }

        /**
         * @param expireDate the expireDate to set
         */
        public void setExpireDate(String expireDate) {
            this.expireDate = expireDate;
        }

    }

}
