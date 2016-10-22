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
import lk.ijse.winestores.dao.custom.CustomOrderDAO;
import lk.ijse.winestores.dao.dto.CustomOrderDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;

/**
 *
 * @author Ranjith Suranga
 */
public class CustomOrderDAOImpl implements CustomOrderDAO{
    
    private Connection connection;

    public CustomOrderDAOImpl() {
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
    public String create(CustomOrderDTO dto) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO custom_order (order_date, made_by, total, payment_id, tendered_cash) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        pstm.setObject(1, dto.getOrderDate());
        pstm.setObject(2, dto.getMadeBy());
        pstm.setObject(3, dto.getTotal());
        pstm.setObject(4, dto.getPaymentId());
        pstm.setObject(5, dto.getTenderedCash().toPlainString());
        pstm.executeUpdate();
        ResultSet rstKeys = pstm.getGeneratedKeys();
        if (rstKeys.next()){
            return rstKeys.getString(1);
        }
        return null;
    }
    
}
