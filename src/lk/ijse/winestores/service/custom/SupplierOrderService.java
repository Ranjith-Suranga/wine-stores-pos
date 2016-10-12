/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.dto.SupplierOrderDTO;
import lk.ijse.winestores.dao.dto.SupplierOrderDetailDTO;
import lk.ijse.winestores.service.SuperService;

/**
 *
 * @author Ranjith Suranga
 */
public interface SupplierOrderService extends SuperService {

    public boolean saveSupplierOrder(SupplierOrderDTO supplierOrderDTO, ArrayList<SupplierOrderDetailDTO> supplierOrderDetails) throws ClassNotFoundException, SQLException;

    public boolean removeSupplierOrder(int orderId) throws ClassNotFoundException, SQLException;

    // Queries
    public ArrayList<SupplierOrderDTO> getAllSupplierOrders() throws ClassNotFoundException, SQLException;

    public ArrayList<SupplierOrderDTO> getSupplierOrdersByOrderId(int orderId) throws ClassNotFoundException, SQLException;

    public ArrayList<SupplierOrderDTO> getSupplierOrdersByOrderDate(String orderDate) throws ClassNotFoundException, SQLException;

    public ArrayList<SupplierOrderDTO> getSupplierOrdersBySupplierName(String supplierName) throws ClassNotFoundException, SQLException;

    public int getLastSupplierOrderId() throws ClassNotFoundException, SQLException;
    
}
