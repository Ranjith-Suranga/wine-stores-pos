/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lk.ijse.winestores.dao.custom.SupplierOrderDetailsDAO;
import lk.ijse.winestores.dao.dto.SupplierOrderDetailDTO;

/**
 *
 * @author Ranjith Suranga
 */
public class SupplierOrderDetailsDAOImpl implements SupplierOrderDetailsDAO{
    
    // Dependencies
    private Connection connection;
    
    // Depenedecy Injection    
    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
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
    public boolean createSupplierOrderDetail(SupplierOrderDetailDTO supplierOrderDetailDTO) throws ClassNotFoundException, SQLException {
        
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO supplier_order_detail (order_id, item_code, qty, buying_price) VALUES (?,?,?,?)");
        pstm.setInt(1, supplierOrderDetailDTO.getOrderId());
        pstm.setString(2, supplierOrderDetailDTO.getItemCode());
        pstm.setInt(3, supplierOrderDetailDTO.getQty());
        pstm.setString(4, (supplierOrderDetailDTO.getBuyingPrice() == null) ? "0" : supplierOrderDetailDTO.getBuyingPrice().toString());
        return (pstm.executeUpdate() != 0);
        
    }

    @Override
    public boolean deleteSupplierOrderDetails(int orderId) throws ClassNotFoundException, SQLException {
        
        PreparedStatement pstm = connection.prepareStatement("DELETE FROM supplier_order_detail WHERE order_id=?");
        pstm.setInt(1, orderId);
        int affectedRows = pstm.executeUpdate();
        return (affectedRows != 0);
        
    }


    
}
