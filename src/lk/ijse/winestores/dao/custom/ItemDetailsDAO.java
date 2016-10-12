/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface ItemDetailsDAO extends SuperDAO {

    public enum QueryType {
        ITEM_CODE, ITEM_NAME, BARCODE, SELLING_PRICE
    }

    public boolean isItemDetailsExists(String itemCode) throws ClassNotFoundException, SQLException;

    public int readCurrentQty(String itemCode) throws ClassNotFoundException, SQLException;

    public boolean updateCurrentQty(Connection con, String itemCode, int newQty) throws ClassNotFoundException, SQLException;

    public ArrayList<ItemDetailsDTO> readItemDetails(QueryType queryType, String queryWord) throws ClassNotFoundException, SQLException;

    public boolean createItemDetails(Connection con, ItemDetailsDTO dto) throws ClassNotFoundException, SQLException;

    public boolean updateItemDetails(Connection con, ItemDetailsDTO dto) throws ClassNotFoundException, SQLException;

    public ArrayList<CustomItemDetailsDTO> readAllItems() throws ClassNotFoundException, SQLException;

    public void setConnection(Connection con) throws ClassNotFoundException, SQLException;

    public Connection getConnection() throws ClassNotFoundException, SQLException;
}
