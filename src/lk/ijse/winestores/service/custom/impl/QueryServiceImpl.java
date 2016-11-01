/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.QueryDAO;
import lk.ijse.winestores.dao.dto.ChequeDetailsDTO;
import lk.ijse.winestores.dao.dto.CreditOrderDTO;
import lk.ijse.winestores.dao.dto.CustomOrderDTO;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.dao.dto.OrderEmptyBottleDetailsDTO;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;
import lk.ijse.winestores.dao.dto.SupplierOrderDTO;
import lk.ijse.winestores.service.custom.QueryService;

/**
 *
 * @author Ranjith Suranga
 */
public class QueryServiceImpl implements QueryService{
    
    // Dependencies
    private QueryDAO queryDAO;

    // Dependency Injection
    public QueryServiceImpl() {
        queryDAO = (QueryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.QUERY);
    }
    
    @Override
    public SubCategoryDTO getSubCategoryByItemCode(String itemCode) throws ClassNotFoundException, SQLException {
        return queryDAO.readSubCategoryByItemCode(itemCode);
    }

    @Override
    public String getItemNameByItemCode(String itemCode) throws ClassNotFoundException, SQLException {
        return queryDAO.readItemNameByItemCode(itemCode);
    }

    @Override
    public SupplierOrderDTO getSupplierOrder(int orderId) throws ClassNotFoundException, SQLException {
        return queryDAO.readSupplierOrder(orderId);
    }

    @Override
    public ArrayList<EmptyBottleDTO> getAllEmptyBottles() throws ClassNotFoundException, SQLException {
        return queryDAO.readAllEmptyBottles();
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomers() throws ClassNotFoundException, SQLException {
        return queryDAO.readAllCustomers();
    }

    @Override
    public boolean hasCashOrder(int orderId) throws ClassNotFoundException, SQLException {
        return queryDAO.hasCashOrder(orderId);
    }

    @Override
    public ArrayList<OrderEmptyBottleDetailsDTO> getEmptyBottleDetails(int orderId) throws ClassNotFoundException, SQLException {
        return queryDAO.readEmptyBottleDetails(orderId);
    }

    @Override
    public CustomOrderDTO getOrder(int orderId) throws ClassNotFoundException, SQLException {
        return queryDAO.readOrder(orderId);
    }

    @Override
    public EmptyBottleDTO getEmptyBottle(String emptyBottleType) throws ClassNotFoundException, SQLException {
        return queryDAO.readEmptyBottle(emptyBottleType);
    }

    @Override
    public ChequeDetailsDTO getChequeDetails(int orderId) throws ClassNotFoundException, SQLException {
        return queryDAO.readChequeDetails(orderId);
    }

    @Override
    public CustomerDTO getCustomer(int customerId) throws ClassNotFoundException, SQLException {
        return queryDAO.readCustomer(customerId);
    }

    @Override
    public CreditOrderDTO getCreditOrder(int orderId) throws ClassNotFoundException, SQLException {
        return queryDAO.readCreditOrder(orderId);
    }

    @Override
    public ArrayList<CreditOrderDTO> getCreditOrdersByCustomerName(String customerName) throws ClassNotFoundException, SQLException {
        return queryDAO.readCreditOrdersByCustomerName(customerName);
    }

    @Override
    public ArrayList<CreditOrderDTO> getCreditOrdersByDatePeriod(Date fromDate, Date toDate) throws ClassNotFoundException, SQLException {
        return queryDAO.readCreditOrdersByDatePeriod(fromDate, toDate);
    }
    
}
