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
import java.util.logging.Level;
import java.util.logging.Logger;
import lk.ijse.winestores.dao.custom.OrderItemDetailsDAO;
import lk.ijse.winestores.dao.dto.OrderItemDetailsDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;

/**
 *
 * @author Ranjith Suranga
 */
public class OrderItemDetailsDAOImpl implements OrderItemDetailsDAO {

    private Connection connection;

    public OrderItemDetailsDAOImpl() {
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
    public boolean create(OrderItemDetailsDTO dto) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO order_item_details (order_id,item_code,qty,selling_price) VALUES(?,?,?,?)");
        pstm.setObject(1, dto.getOrderId());
        pstm.setObject(2, dto.getItemCode());
        pstm.setObject(3, dto.getQty());
        pstm.setObject(4, dto.getSellingPrice());
        return (pstm.executeUpdate() != 0);    
    }

    @Override
    public int readOrderItemDetailsCount(String itemCode) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT COUNT(*) FROM order_item_details WHERE item_code=?");
        pstm.setString(1, itemCode);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()){
            return rst.getInt(1);
        }else{
            return 0;
        }
    }

    @Override
    public boolean deleteByOrderId(int orderID) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM order_item_details WHERE order_id=?");
        pstm.setInt(1, orderID);
        int affectedRows = pstm.executeUpdate();
        return (affectedRows > 0);
    }

}
