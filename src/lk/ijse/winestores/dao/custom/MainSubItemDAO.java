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
import lk.ijse.winestores.dao.dto.CustomItemDTO;
import lk.ijse.winestores.dao.dto.MainSubItemDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface MainSubItemDAO extends SuperDAO {

    public static final String TABLE_NAME = "main_sub_item";
    public static final String MAIN_SUB_ITEM_INSERTION_SQL = "INSERT INTO main_sub_item VALUES(?,?,?,?,?,?);";
    public static final String MAIN_SUB_ITEM_UPDATE_SQL = "UPDATE main_sub_item SET capacity=?, barcode=? WHERE item_code=?;";
    public static final String MAIN_SUB_ITEM_DELETION_SQL = "DELETE FROM main_sub_item WHERE main_sub_item_id=? ;";
//    public static final String MAJOR_CATEGORY_CHECK_EXISTENCY = "SELECT * FROM main_category WHERE ?=? ;";
    public static final String MAIN_SUB_ITEM_CHECK_EXISTENCY_BY_ID = "SELECT * FROM main_sub_item WHERE main_sub_item_id=? ;";
    public static final String MAIN_SUB_ITEM_READ_LAST_ID_SQL = "SELECT * FROM main_sub_item ORDER BY main_sub_item_id DESC LIMIT 1;";
    public static final String READ_ITEMS_BY_MAJOR_SUB_IDS = "SELECT main_sub_item.item_id,main_sub_item.item_code, main_sub_item.capacity, main_sub_item.barcode, item.item_name FROM wine_stores.main_sub_item JOIN item ON main_sub_item.item_id = item.item_id WHERE main_sub_id=?;";
    public static final String MAIN_SUB_ITEM_CHECK_DEPENDECY = "SELECT COUNT(*) FROM wine_stores.main_sub_item WHERE item_id=?";
    public static final String READ_MAIN_SUB_ITEM_ID_BY_ITEM_CODE = "SELECT * FROM main_sub_item WHERE item_code=?";
    
    public void setConnection(Connection connection);
    
    public int createMainSubItem(MainSubItemDTO dto) throws ClassNotFoundException, SQLException;

    public int updateMainSubItem(MainSubItemDTO dto) throws ClassNotFoundException, SQLException;

    public boolean deleteMainSubItem(String itemCode) throws ClassNotFoundException, SQLException;
    
    public boolean updateBarCode(String itemCode, String barCode) throws ClassNotFoundException, SQLException;
    
    public ArrayList<CustomItemDTO> readItems(String majorCategoryId, String subCategoryId) throws ClassNotFoundException, SQLException;
            
    /* This method returns whether the item is used by others or not */
    public boolean isItemDepeneded(String itemId) throws ClassNotFoundException, SQLException;
    
    public String readId(String itemCode)throws ClassNotFoundException, SQLException;
    
    public String[] getItemMainDetails(String itemCode) throws ClassNotFoundException, SQLException;
    
    public MainSubItemDTO readMainSubItemByPK(String pK) throws ClassNotFoundException, SQLException;
    
    public String readBarCodeByItemCode(String itemCode) throws ClassNotFoundException,SQLException;

   
}
