/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lk.ijse.winestores.dao.custom.ChequeDetailsDAO;
import lk.ijse.winestores.dao.dto.ChequeDetailsDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;

/**
 *
 * @author Ranjith Suranga
 */
public class ChequeDetailsDAOImpl implements ChequeDetailsDAO {

    private Connection connection;

    public ChequeDetailsDAOImpl() {
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
    public boolean create(ChequeDetailsDTO dto) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO cheque_details (order_id,cheque_number,bank,branch) VALUES (?,?,?,?)");
        pstm.setObject(1, dto.getOrderId());
        pstm.setObject(2, dto.getChequeNumber());
        pstm.setObject(3, dto.getBank());
        pstm.setObject(4, dto.getBranch());
        return (pstm.executeUpdate() != 0);
    }

    @Override
    public boolean deleteByOrderId(int orderId) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM cheque_details WHERE order_id=?");
        pstm.setInt(1, orderId);
        return (pstm.executeUpdate() != 0);
    }

}
