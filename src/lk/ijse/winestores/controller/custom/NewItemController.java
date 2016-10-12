/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface NewItemController extends SuperController{
    
    public boolean saveItem(ItemDetailsDTO dto) throws ClassNotFoundException, SQLException;

    public boolean saveBarCode(String itemCode, String barCode) throws ClassNotFoundException, SQLException;
    
    // Queries
    
    public ArrayList<CustomItemDetailsDTO> getAllItems() throws ClassNotFoundException, SQLException;
    
    public int getQty(String itemCode) throws ClassNotFoundException,SQLException;    
    
}
