/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom.impl;

import java.sql.Connection;
import java.sql.SQLException;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.CustomerDAO;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.service.custom.CustomerService;

/**
 *
 * @author Ranjith Suranga
 */
public class CustomerServiceImpl implements CustomerService{
    
    // Dependencies
    private CustomerDAO daoCustomer;

    // Dependency Injection
    public CustomerServiceImpl() {
        daoCustomer = (CustomerDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.CUSTOMER);
    }

    @Override
    public int saveCustomer(CustomerDTO customer) throws ClassNotFoundException, SQLException {
        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        daoCustomer.setConnection(connection);
        String customerId = daoCustomer.create(customer);
        connection.close();
        return Integer.parseInt(customerId);
    }

    @Override
    public boolean changeCustomer(int customerId, CustomerDTO customer) throws ClassNotFoundException, SQLException {
        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        daoCustomer.setConnection(connection);
        boolean result =  daoCustomer.update(customerId, customer);
        connection.close();
        return result;
    }

    @Override
    public boolean deleteCustomer(int customerId) throws ClassNotFoundException, SQLException {
        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        daoCustomer.setConnection(connection);
        boolean result =  daoCustomer.delete(customerId);
        connection.close();
        return result;
    }


    
}
