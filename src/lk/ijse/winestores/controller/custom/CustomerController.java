/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom;

import java.sql.SQLException;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.dao.dto.CustomerDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface CustomerController extends SuperController {

    public int saveCustomer(CustomerDTO customer) throws ClassNotFoundException, SQLException;

    public boolean changeCustomer(int customerId, CustomerDTO customer) throws ClassNotFoundException, SQLException;

    public boolean deleteCustomer(int customerId) throws ClassNotFoundException, SQLException;

}
