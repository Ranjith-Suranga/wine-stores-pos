/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.dao.dto.ChequeDetailsDTO;
import lk.ijse.winestores.dao.dto.CustomOrderDTO;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.dao.dto.OrderEmptyBottleDetailsDTO;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;
import lk.ijse.winestores.dao.dto.SupplierOrderDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface QueryController extends SuperController{
   
    // Sub Category Related Queries
    
    public SubCategoryDTO getSubCategoryByItemCode(String itemCode) throws ClassNotFoundException, SQLException;
    
    // Item Related Queries    
    
    public String getItemNameByItemCode(String itemCode) throws ClassNotFoundException, SQLException;
    
    // Supplier Order, Supplier Order Details Related Queries
    
    public SupplierOrderDTO getSupplierOrder(int orderId) throws ClassNotFoundException, SQLException;
    
    // EmptyBottle Related Queries
    
    public ArrayList<EmptyBottleDTO> getAllEmptyBottles()throws ClassNotFoundException, SQLException;
    public EmptyBottleDTO getEmptyBottle(String emptyBottleType) throws ClassNotFoundException, SQLException;
    public ArrayList<OrderEmptyBottleDetailsDTO> getEmptyBottleDetails(int orderId) throws ClassNotFoundException, SQLException;
    
    // Customer Related Queries
    
    public ArrayList<CustomerDTO> getAllCustomers()throws ClassNotFoundException, SQLException;
    
    // Cash Sales Related Queries
    
    public boolean hasCashOrder(int orderId) throws ClassNotFoundException, SQLException;
    public CustomOrderDTO getOrder(int orderId) throws ClassNotFoundException, SQLException;
    public ChequeDetailsDTO getChequeDetails(int orderId) throws ClassNotFoundException, SQLException;
}
