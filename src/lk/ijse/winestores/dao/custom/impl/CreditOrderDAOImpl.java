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
import lk.ijse.winestores.dao.custom.CreditOrderDAO;
import lk.ijse.winestores.dao.dto.CreditOrderDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;

/**
 *
 * @author Ranjith Suranga
 */
public class CreditOrderDAOImpl implements CreditOrderDAO{
    
    private Connection connection;

    public CreditOrderDAOImpl() {
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
    public String create(CreditOrderDTO dto) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO credit_order (order_date, made_by, total, customer_id) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        pstm.setObject(1, dto.getOrderDate());
        pstm.setObject(2, dto.getMadeBy());
        pstm.setObject(3, dto.getTotal());
        pstm.setObject(4, dto.getCustomerId());
        pstm.executeUpdate();
        ResultSet rstKeys = pstm.getGeneratedKeys();
        if (rstKeys.next()){
            return rstKeys.getString(1);
        }
        return null;
    }
    
}
