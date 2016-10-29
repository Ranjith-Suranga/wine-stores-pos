/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom.impl;

import java.sql.SQLException;
import lk.ijse.winestores.controller.custom.CustomerController;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.service.ServiceFactory;
import lk.ijse.winestores.service.SuperService;
import lk.ijse.winestores.service.custom.CustomerService;

/**
 *
 * @author Ranjith Suranga
 */
public class CustomerControllerImpl implements CustomerController{
    
    // Dependencies
    private CustomerService srvCustomer;
    
    // Dependency Injection
    public CustomerControllerImpl(){
        srvCustomer = (CustomerService) ServiceFactory.getInstance().getService(SuperService.ServiceType.CUSTOMER);
    }

    @Override
    public int saveCustomer(CustomerDTO customer) throws SQLException, ClassNotFoundException {
        return srvCustomer.saveCustomer(customer);
    }

    @Override
    public boolean changeCustomer(int customerId, CustomerDTO customer) throws SQLException, ClassNotFoundException {
        return srvCustomer.changeCustomer(customerId, customer);
    }

    @Override
    public boolean deleteCustomer(int customerId) throws SQLException, ClassNotFoundException {
        return srvCustomer.deleteCustomer(customerId);
    }
    
    
    
}
