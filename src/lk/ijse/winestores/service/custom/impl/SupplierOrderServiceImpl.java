/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.QueryDAO;
import lk.ijse.winestores.dao.custom.SupplierOrderDAO;
import lk.ijse.winestores.dao.dto.SupplierOrderDTO;
import lk.ijse.winestores.dao.dto.SupplierOrderDetailDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.service.custom.SupplierOrderService;

/**
 *
 * @author Ranjith Suranga
 */
public class SupplierOrderServiceImpl implements SupplierOrderService{
    
    // Dependencies
    private SupplierOrderDAO supplierOrderDAO;
    private QueryDAO queryDAO;

    // Dependecy Injection
    public SupplierOrderServiceImpl() {
        supplierOrderDAO = (SupplierOrderDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUPPLIER_ORDER);
        queryDAO = (QueryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.QUERY);
    }
   
    @Override
    public ArrayList<SupplierOrderDTO> getAllSupplierOrders() throws ClassNotFoundException, SQLException {
        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        supplierOrderDAO.setConnection(connection);
        ArrayList<SupplierOrderDTO> allSupplierOrders = supplierOrderDAO.readAllSupplierOrders();
        connection.close();
        return allSupplierOrders;
    }

    @Override
    public ArrayList<SupplierOrderDTO> getSupplierOrdersByOrderId(int orderId) throws ClassNotFoundException, SQLException {
        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        supplierOrderDAO.setConnection(connection);
        ArrayList<SupplierOrderDTO> supplierOrders = supplierOrderDAO.readSupplierOrdersByOrderId(orderId);
        connection.close();
        return supplierOrders;
    }

    @Override
    public ArrayList<SupplierOrderDTO> getSupplierOrdersByOrderDate(String orderDate) throws ClassNotFoundException, SQLException {
        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        supplierOrderDAO.setConnection(connection);
        ArrayList<SupplierOrderDTO> supplierOrders = supplierOrderDAO.readSupplierOrdersByOrderDate(orderDate);
        connection.close();
        return supplierOrders;
    }

    @Override
    public ArrayList<SupplierOrderDTO> getSupplierOrdersBySupplierName(String supplierName) throws ClassNotFoundException, SQLException {
        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        supplierOrderDAO.setConnection(connection);
        ArrayList<SupplierOrderDTO> supplierOrders = supplierOrderDAO.readSupplierOrdersBySupplierName(supplierName);
        connection.close();
        return supplierOrders;
        
    }

    @Override
    public boolean removeSupplierOrder(int orderId) throws ClassNotFoundException, SQLException {
        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        supplierOrderDAO.setConnection(connection);
        boolean success = supplierOrderDAO.deleteSupplierOrder(orderId);
        connection.close();
        return success;
    }

    @Override
    public int getLastSupplierOrderId() throws ClassNotFoundException, SQLException {
        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        supplierOrderDAO.setConnection(connection);
        int lastId = supplierOrderDAO.readLastSupplierOrderId();
        connection.close();
        return lastId;
    }

    @Override
    public boolean saveSupplierOrder(SupplierOrderDTO supplierOrderDTO, ArrayList<SupplierOrderDetailDTO> supplierOrderDetails) throws ClassNotFoundException, SQLException {
        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        supplierOrderDAO.setConnection(connection);
        
        // Determine whether the supplier order is already exits
        if (queryDAO.isSupplierOrderExits(supplierOrderDTO.getOrderId())){
            // If that is the case, we are going to delete existing one
            boolean deleted = supplierOrderDAO.deleteSupplierOrder(supplierOrderDTO.getOrderId());
            
            if (!deleted){
                connection.close();
                return false;
            }
        }
        
        boolean success = supplierOrderDAO.createSupplierOrder(supplierOrderDTO, supplierOrderDetails);
        
        connection.close();
        
        return success;
    }
    
}
