/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom.impl;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.CustomDAO;
import lk.ijse.winestores.dao.custom.ItemDAO;
import lk.ijse.winestores.dao.custom.ItemDetailsDAO;
import lk.ijse.winestores.dao.custom.MainSubCategoryDAO;
import lk.ijse.winestores.dao.custom.MainSubItemDAO;
import lk.ijse.winestores.dao.custom.MajorCategoryDAO;
import lk.ijse.winestores.dao.custom.OrderItemDetailsDAO;
import lk.ijse.winestores.dao.custom.SubCategoryDAO;
import lk.ijse.winestores.dao.dto.CustomItemDTO;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;
import lk.ijse.winestores.dao.dto.ItemDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;
import lk.ijse.winestores.dao.dto.MainSubItemDTO;
import lk.ijse.winestores.service.custom.ItemService;
import lk.ijse.winestores.service.idgenerator.GenerateId;

/**
 *
 * @author Ranjith Suranga
 */
public class ItemServiceImpl implements ItemService {

    @Override
    public int saveItem(String majorCategoryName, String subCategoryName, ItemDTO newItemDTO, String capcity, String barCode) throws ClassNotFoundException, SQLException {

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumIntegerDigits(4);
        numberFormat.setMinimumIntegerDigits(4);

        ItemDAO itemDAO = (ItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.ITEM);
        ItemDTO itemDTO = itemDAO.readItemByItemName(newItemDTO.getItemName());

        // Check whether there is an item already in item table
        if (itemDTO == null) {
            itemDTO = newItemDTO;
            itemDTO.setItemId(GenerateId.getNewId(SuperDAO.DAOType.ITEM, null, numberFormat));
            itemDAO.createItem(itemDTO);
        }

        numberFormat.setMaximumIntegerDigits(4);
        numberFormat.setMinimumIntegerDigits(8);

        MainSubItemDAO dao = (MainSubItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_ITEM);
        MainSubCategoryDAO mainSubCategoryDAO = (MainSubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_CATEGORY);

        //MajorCategoryDAO majorDAO = (MajorCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAJOR_CATEGORY);
        String majorCategoryId = getMajorCategoryId(majorCategoryName);

        //SubCategoryDAO subDAO = (SubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUB_CATEGORY);
        String subCategoryId = getSubCategoryId(subCategoryName);

        MainSubItemDTO dto = new MainSubItemDTO(GenerateId.getNewId(SuperDAO.DAOType.MAIN_SUB_ITEM, null, numberFormat), mainSubCategoryDAO.readMainSubCategory(majorCategoryId, subCategoryId).getMainSubCategoryId(), itemDTO.getItemId(), generateItemCode(majorCategoryId, subCategoryId, itemDTO.getItemId()), capcity, barCode);

        return dao.createMainSubItem(dto);
    }

    @Override
    public ArrayList<CustomItemDTO> getItems(String majorCategoryName, String subCategoryName) throws ClassNotFoundException, SQLException {

        MainSubItemDAO dao = (MainSubItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_ITEM);

        //MajorCategoryDAO majorDAO = (MajorCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAJOR_CATEGORY);
        String majorCategoryId = getMajorCategoryId(majorCategoryName);

        //SubCategoryDAO subDAO = (SubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUB_CATEGORY);
        String subCategoryId = getSubCategoryId(subCategoryName);

        return dao.readItems(majorCategoryId, subCategoryId);

    }

    @Override
    public ArrayList<ItemDTO> getAllItems() throws ClassNotFoundException, SQLException {
        ItemDAO itemDAO = (ItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.ITEM);
        return itemDAO.readAllItems();
    }

    private String generateItemCode(String majorCategoryId, String subCategoryId, String itemId) {

        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumIntegerDigits(2);

        int iId = Integer.parseInt(itemId);
        return majorCategoryId + subCategoryId + nf.format(iId);

    }

    private String getSubCategoryId(String subCategoryName) throws ClassNotFoundException, SQLException {
        SubCategoryDAO subDAO = (SubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUB_CATEGORY);
        return subDAO.readSubCategoryByName(subCategoryName).getSubCategoryId();
    }

    private String getMajorCategoryId(String majorCategoryName) throws ClassNotFoundException, SQLException {
        MajorCategoryDAO majorDAO = (MajorCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAJOR_CATEGORY);
        return majorDAO.readMajorCategoryByName(majorCategoryName).getMainCatId();
    }

    @Override
    public String getItemCode(String majorCategoryName, String subCategoryName, String itemId) throws ClassNotFoundException, SQLException {
        return generateItemCode(getMajorCategoryId(majorCategoryName), getSubCategoryId(subCategoryName), itemId);
    }

    @Override
    public int changeItem(String itemName, String itemCode, String newItemName, String newSize, String newBarCode) throws ClassNotFoundException, SQLException {

        ItemDAO dao = (ItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.ITEM);
        ItemDTO dto = dao.readItemByItemName(itemName);

        MainSubItemDAO mainSubItemDAO = (MainSubItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_ITEM);
        MainSubItemDTO mainSubItemDTO = new MainSubItemDTO(null, null, null, itemCode, newSize, newBarCode);
        int result = mainSubItemDAO.updateMainSubItem(mainSubItemDTO);

        if (mainSubItemDAO.isItemDepeneded(dto.getItemId())) {
            result = -100;          // Item is already in used
        } else if (dao.readItemByItemName(newItemName) == null) {
            dto.setItemName(newItemName);
            dao.updateItem(dto);
        } else {
            result = -200;          // Item already exsits..
        }

        return result;
    }

    @Override
    public String[] getItemMainDetails(String itemCode) throws ClassNotFoundException, SQLException {

        MainSubItemDAO mainSubItemDAO = (MainSubItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_ITEM);
        return mainSubItemDAO.getItemMainDetails(itemCode);

    }

    @Override
    public ArrayList<ItemDetailsDTO> getItemDetails(ItemDetailsDAO.QueryType queryType, String queryWord) throws ClassNotFoundException, SQLException {
        
        ItemDetailsDAO itemDetailsDAO = (ItemDetailsDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.ITEM_DETAILS);
        return itemDetailsDAO.readItemDetails(queryType, queryWord);
    }

    @Override
    public ArrayList<CustomItemDetailsDTO> getItems(CustomDAO.ItemQueryType queryType, String queryWord) throws ClassNotFoundException, SQLException {
        
        CustomDAO customDAO = (CustomDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.CUSTOM);
        return customDAO.readItems(queryType, queryWord);
        
    }

    @Override
    public String getBarCodeByItemCode(String itemCode) throws ClassNotFoundException, SQLException {
        
        MainSubItemDAO mainSubItemDAO = (MainSubItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_ITEM);
        return mainSubItemDAO.readBarCodeByItemCode(itemCode);
        
    }

    @Override
    public boolean removeItem(String itemCode) throws ClassNotFoundException, SQLException {
        MainSubItemDAO mainSubItemDAO = (MainSubItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_ITEM);
        
        // Before we are going to delete the item, we are going to check whether there is any
        // releated records in the order_item_details table or credit_order_item_details table
        // This is done, because they are not related in the database
        // We don't want to do this for GRN's because they are related :)
        OrderItemDetailsDAO orderItemDetailsDAO = (OrderItemDetailsDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.ORDER_ITEM_DETAILS);
        if (orderItemDetailsDAO.readOrderItemDetailsCount(itemCode) > 0){
            SQLException exception = new SQLException(null, null,1451);
            throw exception;
        }
        
        return mainSubItemDAO.deleteMainSubItem(itemCode);
    }

}
