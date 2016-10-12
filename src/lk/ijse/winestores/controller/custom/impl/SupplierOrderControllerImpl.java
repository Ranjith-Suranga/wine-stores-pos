/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.dto.SupplierOrderDTO;
import lk.ijse.winestores.controller.custom.SupplierOrderController;
import lk.ijse.winestores.dao.dto.SupplierOrderDetailDTO;
import lk.ijse.winestores.service.ServiceFactory;
import lk.ijse.winestores.service.SuperService;
import lk.ijse.winestores.service.custom.SupplierOrderService;

/**
 *
 * @author Ranjith Suranga
 */
public class SupplierOrderControllerImpl implements SupplierOrderController{
    
    private SupplierOrderService supplierOrderService;

    public SupplierOrderControllerImpl(){
        supplierOrderService = (SupplierOrderService) ServiceFactory.getInstance().getService(SuperService.ServiceType.SUPPLIER_ORDER);
    }

    @Override
    public ArrayList<SupplierOrderDTO> getAllSupplierOrders() throws ClassNotFoundException, SQLException {
        return supplierOrderService.getAllSupplierOrders();
    }

    @Override
    public ArrayList<SupplierOrderDTO> getSupplierOrdersByOrderId(int orderId) throws ClassNotFoundException, SQLException {
        return supplierOrderService.getSupplierOrdersByOrderId(orderId);
    }

    @Override
    public ArrayList<SupplierOrderDTO> getSupplierOrdersByOrderDate(String orderDate) throws ClassNotFoundException, SQLException {
        return supplierOrderService.getSupplierOrdersByOrderDate(orderDate);
    }

    @Override
    public ArrayList<SupplierOrderDTO> getSupplierOrdersBySupplierName(String supplierName) throws ClassNotFoundException, SQLException {
        return supplierOrderService.getSupplierOrdersBySupplierName(supplierName);
    }

    @Override
    public boolean removeSupplierOrder(int orderId) throws ClassNotFoundException, SQLException {
        return supplierOrderService.removeSupplierOrder(orderId);
    }

    @Override
    public int getLastSupplierOrderId() throws ClassNotFoundException, SQLException {
        return supplierOrderService.getLastSupplierOrderId();
    }

    @Override
    public boolean saveSupplierOrder(SupplierOrderDTO supplierOrderDTO, ArrayList<SupplierOrderDetailDTO> supplierOrderDetails) throws ClassNotFoundException, SQLException {
        return supplierOrderService.saveSupplierOrder(supplierOrderDTO, supplierOrderDetails);
    }
    
}
