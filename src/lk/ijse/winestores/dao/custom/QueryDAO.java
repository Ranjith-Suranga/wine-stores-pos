/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.ChequeDetailsDTO;
import lk.ijse.winestores.dao.dto.CreditOrderDTO;
import lk.ijse.winestores.dao.dto.CustomOrderDTO;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.dao.dto.DayEndDTO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;
import lk.ijse.winestores.dao.dto.OrderEmptyBottleDetailsDTO;
import lk.ijse.winestores.dao.dto.OrderItemDetailsDTO;
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
    public ArrayList<ItemDetailsDTO> readAllItemDetails()throws ClassNotFoundException, SQLException;
    
    // Supplier Order, Supplier Order Details Related Queries
    
    public boolean isSupplierOrderExits(int supplierOrderId) throws ClassNotFoundException, SQLException;
    
    public SupplierOrderDTO readSupplierOrder(int orderId) throws ClassNotFoundException, SQLException;      
    
    // EmptyBottle Related Queries
    
    public EmptyBottleDTO readEmptyBottle(String emptyBottleType) throws ClassNotFoundException, SQLException;
    public ArrayList<EmptyBottleDTO> readAllEmptyBottles()throws ClassNotFoundException, SQLException;
    public ArrayList<OrderEmptyBottleDetailsDTO> readEmptyBottleDetails(int orderId) throws ClassNotFoundException, SQLException;
    
    // Customer Related Queries
    
    public ArrayList<CustomerDTO> readAllCustomers() throws ClassNotFoundException, SQLException;
    public CustomerDTO readCustomer(int customerId) throws ClassNotFoundException, SQLException; 
    
    // Cash Sales Related Queries
    
    public boolean hasCashOrder(int orderId) throws ClassNotFoundException, SQLException;
    public CustomOrderDTO readOrder(int orderId) throws ClassNotFoundException, SQLException;
//    public ArrayList<CustomOrderDTO> readOrdersByDate(Date date)throws ClassNotFoundException, SQLException;
    public ArrayList<OrderItemDetailsDTO> readOrderItemDetails(int orderId) throws ClassNotFoundException, SQLException;
    public ChequeDetailsDTO readChequeDetails(int orderId) throws ClassNotFoundException, SQLException;
//    public ArrayList<Date> readOrderDates (Date from, String toDate) throws ClassNotFoundException, SQLException;
    public int readSalesQty(Date date, String itemCode) throws ClassNotFoundException, SQLException;
    public boolean hasOrderEmptyBottleDetailsFor(int orderId) throws ClassNotFoundException, SQLException;    
    
    // Credit Sales Related Queries
    
    public CreditOrderDTO readCreditOrder(int orderId) throws ClassNotFoundException, SQLException;
    public ArrayList<CreditOrderDTO> readCreditOrdersByCustomerName(String customerName) throws ClassNotFoundException, SQLException;
    public ArrayList<CreditOrderDTO> readCreditOrdersByDatePeriod(Date fromDate, Date toDate) throws ClassNotFoundException, SQLException;    
    
    // Initial Stock Taking, Day End, Month End Related Queries
    
    public Date readLastDayEnd() throws ClassNotFoundException, SQLException;
    public boolean hasDayEndDone(Date date) throws ClassNotFoundException, SQLException;
    public DayEndDTO readDayEnd(Date date, String itemCode) throws ClassNotFoundException, SQLException;
            
    // GRN Related Queries
    public int readGRNQty(Date date, String itemCode) throws ClassNotFoundException, SQLException;            
    
}
