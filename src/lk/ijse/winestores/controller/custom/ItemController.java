/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.dao.custom.CustomDAO;
import lk.ijse.winestores.dao.custom.ItemDetailsDAO;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface ItemController extends SuperController {

    public int saveItem(String majorCategoryName, String subCategoryNam, String itemName, String size, String barCode) throws ClassNotFoundException, SQLException;

    public int changeItem(String itemName, String itemCode, String newItemName, String newSize, String newBarCode) throws ClassNotFoundException, SQLException;

    public ArrayList<ItemModel> getItems(String majorCategoryName, String subCategoryName) throws ClassNotFoundException, SQLException;

    public ArrayList<ItemModel> getAllItems() throws ClassNotFoundException, SQLException;

    public ArrayList<ItemDetailsDTO> getItemDetails(ItemDetailsDAO.QueryType queryType, String queryWord) throws ClassNotFoundException, SQLException;

    public ArrayList<CustomItemDetailsDTO> getItems(CustomDAO.ItemQueryType queryType, String queryWord) throws ClassNotFoundException, SQLException;

    public String getBarCodeByItemCode(String itemCode) throws ClassNotFoundException, SQLException;

    public boolean removeItem(String itemCode) throws ClassNotFoundException, SQLException;

    public class ItemModel {

        private String itemId;
        private String itemName;
        private String capacity;
        private String barcode;
        private String itemCode;

        public ItemModel() {
        }

        public ItemModel(String itemId, String itemName) {
            this.itemId = itemId;
            this.itemName = itemName;
        }

        public ItemModel(String itemId, String itemName, String capacity, String barcode) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.capacity = capacity;
            this.barcode = barcode;
        }

        public ItemModel(String itemId, String itemName, String capacity, String barcode, String itemCode) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.capacity = capacity;
            this.barcode = barcode;
            this.itemCode = itemCode;
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

}
