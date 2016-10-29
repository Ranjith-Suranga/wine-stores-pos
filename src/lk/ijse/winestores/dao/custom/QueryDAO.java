/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;
import lk.ijse.winestores.dao.dto.SupplierOrderDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface QueryDAO extends SuperDAO{
    
    // Sub Cateogry Related Queries
    
    public SubCategoryDTO readSubCategoryByItemCode(String itemCode) throws ClassNotFoundException, SQLException;
    
    // Item Related Queries    
    
    public String readItemNameByItemCode(String itemCode) throws ClassNotFoundException, SQLException;    
    
    // Supplier Order, Supplier Order Details Related Queries
    
    public boolean isSupplierOrderExits(int supplierOrderId) throws ClassNotFoundException, SQLException;
    
    public SupplierOrderDTO readSupplierOrder(int orderId) throws ClassNotFoundException, SQLException;      
    
    // EmptyBottle Related Queries
    
    public ArrayList<EmptyBottleDTO> readAllEmptyBottles()throws ClassNotFoundException, SQLException;
    
    // Customer Related Queries
    
    public ArrayList<CustomerDTO> readAllCustomers() throws ClassNotFoundException, SQLException;
    
}
