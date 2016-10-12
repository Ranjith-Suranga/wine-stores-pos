/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.Connection;
import java.sql.SQLException;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.SupplierOrderDetailDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface SupplierOrderDetailsDAO extends SuperDAO{
    
    public void setConnection(Connection connection);
    
    public boolean createSupplierOrderDetail(SupplierOrderDetailDTO supplierOrderDetailDTO)throws ClassNotFoundException, SQLException;
    
    public boolean deleteSupplierOrderDetails(int orderId) throws ClassNotFoundException, SQLException;
    
}
