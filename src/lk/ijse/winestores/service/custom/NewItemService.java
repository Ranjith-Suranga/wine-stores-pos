/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;
import lk.ijse.winestores.service.SuperService;

/**
 *
 * @author Ranjith Suranga
 */
public interface NewItemService extends SuperService {
    
    public boolean saveItem(ItemDetailsDTO dto) throws ClassNotFoundException, SQLException;    
    
    public boolean saveBarCode(String itemCode, String barCode) throws ClassNotFoundException, SQLException;    
    
    // Queries

    public ArrayList<CustomItemDetailsDTO> getAllItems() throws ClassNotFoundException, SQLException;

    public int getQty(String itemCode) throws ClassNotFoundException, SQLException;
    
}
