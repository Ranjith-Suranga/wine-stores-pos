/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import lk.ijse.winestores.controller.custom.QueryController;
import lk.ijse.winestores.dao.dto.ChequeDetailsDTO;
import lk.ijse.winestores.dao.dto.CreditOrderDTO;
import lk.ijse.winestores.dao.dto.CustomOrderDTO;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.dao.dto.OrderEmptyBottleDetailsDTO;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;
import lk.ijse.winestores.dao.dto.SupplierOrderDTO;
import lk.ijse.winestores.service.ServiceFactory;
import lk.ijse.winestores.service.SuperService;
import lk.ijse.winestores.service.custom.QueryService;

/**
 *
 * @author Ranjith Suranga
 */
public class QueryControllerImpl implements QueryController{
    
    // Dependencies
    private QueryService queryService;

    // Dependecy Injection
    public QueryControllerImpl() {
        queryService = (QueryService) ServiceFactory.getInstance().getService(SuperService.ServiceType.QUERY);
    }
    
    @Override
    public SubCategoryDTO getSubCategoryByItemCode(String itemCode) throws ClassNotFoundException, SQLException {
        return queryService.getSubCategoryByItemCode(itemCode);
    }

    @Override
    public SupplierOrderDTO getSupplierOrder(int orderId) throws ClassNotFoundException, SQLException {
        return queryService.getSupplierOrder(orderId);
    }

    @Override
    public String getItemNameByItemCode(String itemCode) throws ClassNotFoundException, SQLException {
        return queryService.getItemNameByItemCode(itemCode);
    }

    @Override
    public ArrayList<EmptyBottleDTO> getAllEmptyBottles() throws ClassNotFoundException, SQLException {
        return queryService.getAllEmptyBottles();
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomers() throws ClassNotFoundException, SQLException {
        return queryService.getAllCustomers();
    }

    @Override
    public boolean hasCashOrder(int orderId) throws ClassNotFoundException, SQLException {
        return queryService.hasCashOrder(orderId);
    }

    @Override
    public ArrayList<OrderEmptyBottleDetailsDTO> getEmptyBottleDetails(int orderId) throws ClassNotFoundException, SQLException {
        return queryService.getEmptyBottleDetails(orderId);
    }

    @Override
    public CustomOrderDTO getCashOrder(int orderId) throws ClassNotFoundException, SQLException {
        return queryService.getOrder(orderId);
    }

    @Override
    public EmptyBottleDTO getEmptyBottle(String emptyBottleType) throws ClassNotFoundException, SQLException {
        return queryService.getEmptyBottle(emptyBottleType);
    }

    @Override
    public ChequeDetailsDTO getChequeDetails(int orderId) throws ClassNotFoundException, SQLException {
        return queryService.getChequeDetails(orderId);
    }

    @Override
    public ArrayList<CreditOrderDTO> getCreditOrdersByCustomerName(String customerName) throws ClassNotFoundException, SQLException {
        return queryService.getCreditOrdersByCustomerName(customerName);
    }

    @Override
    public ArrayList<CreditOrderDTO> getCreditOrdersByDatePeriod(Date fromDate, Date toDate) throws ClassNotFoundException, SQLException {
        return queryService.getCreditOrdersByDatePeriod(fromDate, toDate);
    }

    @Override
    public CustomerDTO getCustomer(int customerId) throws ClassNotFoundException, SQLException {
        return queryService.getCustomer(customerId);
    }

    @Override
    public CreditOrderDTO getCreditOrder(int orderId) throws ClassNotFoundException, SQLException {
        return queryService.getCreditOrder(orderId);
    }

    @Override
    public boolean hasFinishedInitalStockTaking() {
        return queryService.hasFinishedInitalStockTaking();
    }

    @Override
    public Date getLastDayEnd() throws ClassNotFoundException, SQLException{
        return queryService.getLastDayEnd();
    }

    @Override
    public boolean hasDayEndDone(Date date) throws ClassNotFoundException, SQLException{
        return queryService.hasDayEndDone(date);
    }
    
}
