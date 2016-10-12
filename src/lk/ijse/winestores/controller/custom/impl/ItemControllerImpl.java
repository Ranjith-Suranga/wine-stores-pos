/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.custom.ItemController;
import lk.ijse.winestores.dao.custom.CustomDAO;
import lk.ijse.winestores.dao.custom.ItemDetailsDAO;
import lk.ijse.winestores.dao.dto.CustomItemDTO;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;
import lk.ijse.winestores.dao.dto.ItemDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;
import lk.ijse.winestores.service.ServiceFactory;
import lk.ijse.winestores.service.SuperService;
import lk.ijse.winestores.service.custom.ItemService;

/**
 *
 * @author Ranjith Suranga
 */
public class ItemControllerImpl implements ItemController {

    @Override
    public int saveItem(String majorCategoryName, String subCategoryNam, String itemName, String size, String barCode) throws ClassNotFoundException, SQLException {

        ItemDTO dto = new ItemDTO(null, itemName);
        ItemService service = (ItemService) ServiceFactory.getInstance().getService(SuperService.ServiceType.ITEM);
        return service.saveItem(majorCategoryName, subCategoryNam, dto, size, barCode);
    }

    @Override
    public ArrayList<ItemModel> getItems(String majorCategoryName, String subCategoryName) throws ClassNotFoundException, SQLException {

        ItemService service = (ItemService) ServiceFactory.getInstance().getService(SuperService.ServiceType.ITEM);
        ArrayList<CustomItemDTO> items = service.getItems(majorCategoryName, subCategoryName);

        if (items == null) {
            return null;
        }

        ArrayList<ItemModel> al = new ArrayList<>();

        for (CustomItemDTO item : items) {
            ItemModel model = new ItemModel(item.getItemId(), item.getItemName(), item.getCapacity(), item.getBarcode(), item.getItemCode());
            al.add(model);
        }
        return al;

    }

    @Override
    public ArrayList<ItemModel> getAllItems() throws ClassNotFoundException, SQLException {

        ItemService service = (ItemService) ServiceFactory.getInstance().getService(SuperService.ServiceType.ITEM);
        ArrayList<ItemDTO> allItems = service.getAllItems();

        if (allItems == null) {
            return null;
        }

        ArrayList<ItemModel> al = new ArrayList<>();

        for (ItemDTO item : allItems) {
            al.add(new ItemModel(item.getItemId(), item.getItemName()));
        }
        return al;
    }

    @Override
    public int changeItem(String itemName, String itemCode, String newItemName, String newSize, String newBarCode) throws ClassNotFoundException, SQLException {

        ItemService service = (ItemService) ServiceFactory.getInstance().getService(SuperService.ServiceType.ITEM);
        return service.changeItem(itemName, itemCode, newItemName, newSize, newBarCode);

    }

    @Override
    public ArrayList<ItemDetailsDTO> getItemDetails(ItemDetailsDAO.QueryType queryType, String queryWord) throws ClassNotFoundException, SQLException {

        ItemService service = (ItemService) ServiceFactory.getInstance().getService(SuperService.ServiceType.ITEM);
        return service.getItemDetails(queryType, queryWord);
    }

    @Override
    public ArrayList<CustomItemDetailsDTO> getItems(CustomDAO.ItemQueryType queryType, String queryWord) throws ClassNotFoundException, SQLException {
        ItemService service = (ItemService) ServiceFactory.getInstance().getService(SuperService.ServiceType.ITEM);
        return service.getItems(queryType, queryWord);
    }

    @Override
    public String getBarCodeByItemCode(String itemCode) throws ClassNotFoundException, SQLException {
        
        ItemService itemService = (ItemService) ServiceFactory.getInstance().getService(SuperService.ServiceType.ITEM);
        return itemService.getBarCodeByItemCode(itemCode);
        
    }

    @Override
    public boolean removeItem(String itemCode) throws ClassNotFoundException, SQLException {
        ItemService itemService = (ItemService) ServiceFactory.getInstance().getService(SuperService.ServiceType.ITEM);
        return itemService.removeItem(itemCode);
    }



}
