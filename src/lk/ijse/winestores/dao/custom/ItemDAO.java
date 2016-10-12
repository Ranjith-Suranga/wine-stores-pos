/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.ItemDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface ItemDAO extends SuperDAO{
    
    public static final String TABLE_NAME = "item";
    public static final String ITEM_INSERTION_SQL = "INSERT INTO item VALUES(?,?);";
    public static final String ITEM_UPDATE_SQL = "UPDATE item SET item_id=?, item_name=? WHERE item_id=?;";
    public static final String ITEM_DELETION_SQL = "DELETE FROM item WHERE item_id=? ;";
//    public static final String MAJOR_CATEGORY_CHECK_EXISTENCY = "SELECT * FROM main_category WHERE ?=? ;";
    public static final String ITEM_CHECK_EXISTENCY_BY_ID = "SELECT * FROM item WHERE item_id=? ;";
    public static final String ITEM_READ_LAST_ID_SQL = "SELECT * FROM item ORDER BY item_id DESC LIMIT 1;"; 
    public static final String READ_ITEM_BY_ITEM_NAME = "SELECT * FROM item WHERE BINARY item_name=?";
    public static final String READ_ALL_ITEMS = "SELECT * FROM "+ TABLE_NAME;
    
    public int createItem(ItemDTO dto)throws ClassNotFoundException, SQLException;
    
    public int updateItem(ItemDTO dto)throws ClassNotFoundException, SQLException;
    
    public int deleteItem(ItemDTO dto)throws ClassNotFoundException, SQLException;
    
    public ItemDTO readItemByItemName(String itemName) throws ClassNotFoundException, SQLException;
    
    public ArrayList<ItemDTO> readAllItems()throws ClassNotFoundException, SQLException;
    
}
