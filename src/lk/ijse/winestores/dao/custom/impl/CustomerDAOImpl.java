/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import lk.ijse.winestores.dao.custom.CustomerDAO;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;

/**
 *
 * @author Ranjith Suranga
 */
public class CustomerDAOImpl implements CustomerDAO {

    // Dependencies
    private Connection connection;

    // Dependency Injection
    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public CustomerDAOImpl() {
        try {
            connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.SINGELTON_DATABASE_CONNECTION).getConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomOrderDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomOrderDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean isExists(String queryId) throws ClassNotFoundException, SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String readLastId() throws ClassNotFoundException, SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String create(CustomerDTO customerDTO) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO customer (customer_name,telephone_number,address) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
        pstm.setObject(1, customerDTO.getCustomerName());
        pstm.setObject(2, customerDTO.getTelephoneNumber());
        pstm.setObject(3, customerDTO.getAddress());
        pstm.executeUpdate();
        ResultSet rstKeys = pstm.getGeneratedKeys();
        String pK = null;
        if (rstKeys.next()) {
            pK = rstKeys.getString(1);
        }
        return pK;
    }

    @Override
    public boolean update(int customerId, CustomerDTO customer) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = connection.prepareStatement("UPDATE customer SET customer_name=?, telephone_number=?, address=? WHERE customer_id=?");
        pstm.setString(1, customer.getCustomerName());
        pstm.setString(2, customer.getTelephoneNumber());
        pstm.setString(3, customer.getAddress());
        pstm.setInt(4, customerId);
        int affectedRows = pstm.executeUpdate();
        return (affectedRows != 0);
    }

    @Override
    public boolean delete(int customerId) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM customer WHERE customer_id=?");
        pstm.setInt(1, customerId);
        int affectedRows = pstm.executeUpdate();
        return (affectedRows != 0);
    }

}
