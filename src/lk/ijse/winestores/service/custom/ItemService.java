/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.custom.CustomDAO;
import lk.ijse.winestores.dao.custom.ItemDetailsDAO;
import lk.ijse.winestores.dao.dto.CustomItemDTO;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;
import lk.ijse.winestores.dao.dto.ItemDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;
import lk.ijse.winestores.service.SuperService;

/**
 *
 * @author Ranjith Suranga
 */
public interface ItemService extends SuperService {

    public int saveItem(String majorCategoryName, String subCategoryName, ItemDTO itemDTO, String capcity, String barCode) throws ClassNotFoundException, SQLException;

    public int changeItem(String itemName, String itemCode, String newItemName, String newSize, String newBarCode) throws ClassNotFoundException, SQLException;

    public ArrayList<CustomItemDTO> getItems(String majorCategoryName, String subCategoryName) throws ClassNotFoundException, SQLException;

    public ArrayList<ItemDTO> getAllItems() throws ClassNotFoundException, SQLException;

    public String getItemCode(String majorCategoryName, String subCategoryName, String itemId) throws ClassNotFoundException, SQLException;

    public String[] getItemMainDetails(String itemCode) throws ClassNotFoundException, SQLException;

    public ArrayList<ItemDetailsDTO> getItemDetails(ItemDetailsDAO.QueryType queryType, String queryWord) throws ClassNotFoundException, SQLException;
    
    public ArrayList<CustomItemDetailsDTO> getItems(CustomDAO.ItemQueryType queryType, String queryWord)throws ClassNotFoundException, SQLException;
    
    public String getBarCodeByItemCode(String itemCode)throws ClassNotFoundException, SQLException;

    public boolean removeItem(String itemCode)throws ClassNotFoundException, SQLException;

}
