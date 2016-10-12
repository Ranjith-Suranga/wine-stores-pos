/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.custom.NewItemController;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;
import lk.ijse.winestores.service.ServiceFactory;
import lk.ijse.winestores.service.SuperService;
import lk.ijse.winestores.service.custom.NewItemService;

/**
 *
 * @author Ranjith Suranga
 */
public class NewItemControllerImpl implements NewItemController{
    
    private NewItemService itemService;

    public NewItemControllerImpl() {
        itemService = (NewItemService) ServiceFactory.getInstance().getService(SuperService.ServiceType.NEW_ITEM);
    }
    
    @Override
    public ArrayList<CustomItemDetailsDTO> getAllItems() throws ClassNotFoundException, SQLException {
        return itemService.getAllItems();
    }

    @Override
    public boolean saveItem(ItemDetailsDTO dto) throws ClassNotFoundException, SQLException {
        return itemService.saveItem(dto);
    }

    @Override
    public int getQty(String itemCode) throws ClassNotFoundException, SQLException {
        return itemService.getQty(itemCode);
    }

    @Override
    public boolean saveBarCode(String itemCode, String barCode) throws ClassNotFoundException, SQLException {
        return itemService.saveBarCode(itemCode, barCode);
    }
    
}
