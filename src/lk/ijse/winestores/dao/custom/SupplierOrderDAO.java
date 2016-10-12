/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.SupplierOrderDTO;
import lk.ijse.winestores.dao.dto.SupplierOrderDetailDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface SupplierOrderDAO extends SuperDAO {

    public void setConnection(Connection connection);

    public boolean createSupplierOrder(SupplierOrderDTO supplierOrderDTO, ArrayList<SupplierOrderDetailDTO> supplierOrderDetails) throws ClassNotFoundException, SQLException;

    public boolean deleteSupplierOrder(int orderId) throws ClassNotFoundException, SQLException;

    public ArrayList<SupplierOrderDTO> readAllSupplierOrders() throws ClassNotFoundException, SQLException;

    public ArrayList<SupplierOrderDTO> readSupplierOrdersByOrderId(int orderId) throws ClassNotFoundException, SQLException;

    public ArrayList<SupplierOrderDTO> readSupplierOrdersByOrderDate(String orderDate) throws ClassNotFoundException, SQLException;

    public ArrayList<SupplierOrderDTO> readSupplierOrdersBySupplierId(String supplierId) throws ClassNotFoundException, SQLException;

    public ArrayList<SupplierOrderDTO> readSupplierOrdersBySupplierName(String supplierName) throws ClassNotFoundException, SQLException;

    public int readLastSupplierOrderId() throws ClassNotFoundException, SQLException;

}
