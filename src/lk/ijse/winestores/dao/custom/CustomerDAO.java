/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.Connection;
import java.sql.SQLException;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.CustomerDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface CustomerDAO extends SuperDAO{
    
    public void setConnection(Connection connection);
    
    public String create(CustomerDTO customerDTO)throws ClassNotFoundException, SQLException;
    
    public boolean update(int customerId, CustomerDTO customer) throws ClassNotFoundException, SQLException;
    
    public boolean delete(int customerId) throws ClassNotFoundException, SQLException;
    
}
