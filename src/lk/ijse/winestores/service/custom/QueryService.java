/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import lk.ijse.winestores.dao.dto.ChequeDetailsDTO;
import lk.ijse.winestores.dao.dto.CreditOrderDTO;
import lk.ijse.winestores.dao.dto.CustomOrderDTO;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.dao.dto.OrderEmptyBottleDetailsDTO;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;
import lk.ijse.winestores.dao.dto.SupplierOrderDTO;
import lk.ijse.winestores.service.SuperService;

/**
 *
 * @author Ranjith Suranga
 */
public interface QueryService extends SuperService {

    public SubCategoryDTO getSubCategoryByItemCode(String itemCode) throws ClassNotFoundException, SQLException;

    // Item Related Queries    
    public String getItemNameByItemCode(String itemCode) throws ClassNotFoundException, SQLException;

    // Supplier Order, Supplier Order Details Related Queries
    public SupplierOrderDTO getSupplierOrder(int orderId) throws ClassNotFoundException, SQLException;
    
    // EmptyBottle Related Queries
    
    public EmptyBottleDTO getEmptyBottle(String emptyBottleType) throws ClassNotFoundException, SQLException;
    public ArrayList<EmptyBottleDTO> getAllEmptyBottles()throws ClassNotFoundException, SQLException;  
    public ArrayList<OrderEmptyBottleDetailsDTO> getEmptyBottleDetails(int orderId) throws ClassNotFoundException, SQLException;
    
    // Customer Related Queries
    
    public ArrayList<CustomerDTO> getAllCustomers() throws ClassNotFoundException, SQLException;
    public CustomerDTO getCustomer(int customerId) throws ClassNotFoundException, SQLException;    
    
    // Cash Sales Related Queries
    
    public boolean hasCashOrder(int orderId) throws ClassNotFoundException, SQLException;  
    public CustomOrderDTO getOrder(int orderId) throws ClassNotFoundException, SQLException;
    public ChequeDetailsDTO getChequeDetails(int orderId) throws ClassNotFoundException, SQLException;
    
    // Credit Sales Related Queries
    
    public CreditOrderDTO getCreditOrder(int orderId) throws ClassNotFoundException, SQLException;
    public ArrayList<CreditOrderDTO> getCreditOrdersByCustomerName(String customerName) throws ClassNotFoundException, SQLException;
    public ArrayList<CreditOrderDTO> getCreditOrdersByDatePeriod(Date fromDate, Date toDate) throws ClassNotFoundException, SQLException;
    
    // Initial Stock Taking Queries
    
    public boolean hasFinishedInitalStockTaking();    
    public Date getLastDayEnd() throws ClassNotFoundException, SQLException;
    public boolean hasDayEndDone(Date date) throws ClassNotFoundException, SQLException;    
    
}
