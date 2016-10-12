/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.custom.SupplierController;
import lk.ijse.winestores.dao.dto.SupplierDTO;
import lk.ijse.winestores.service.ServiceFactory;
import lk.ijse.winestores.service.SuperService;
import lk.ijse.winestores.service.custom.SupplierService;

/**
 *
 * @author Ranjith Suranga
 */
public class SupplierControllerImpl  implements SupplierController{

    @Override
    public ArrayList<SupplierModel> getAllSuppliers() throws ClassNotFoundException, SQLException {
        
        SupplierService service = (SupplierService) ServiceFactory.getInstance().getService(SuperService.ServiceType.SUPPLIER);
        ArrayList<SupplierDTO> allSuppliers = service.getAllSuppliers();
        
        if (allSuppliers == null){
            return null;
        }

        ArrayList<SupplierModel> al = new ArrayList<>();
        
        for (SupplierDTO supplier : allSuppliers) {
            SupplierModel model = new SupplierModel(supplier.getSupplierId(), supplier.getName(), supplier.getAddress(), supplier.getEmail(), supplier.getContact(), supplier.getFax(), supplier.getCordintatorName(), supplier.getCordinatorContact(), supplier.getAgentName(), supplier.getAgenNo(), supplier.getSupplierAdded());
            al.add(model);
        }  
        return al;
        
    }

    @Override
    public SupplierModel getSupplierById(String supplierId) throws ClassNotFoundException, SQLException {
        SupplierService service = (SupplierService) ServiceFactory.getInstance().getService(SuperService.ServiceType.SUPPLIER);
        SupplierDTO supplier = service.getSupplierById(supplierId);
        
        if (supplier == null){
            return null;
        }
        
        SupplierModel model = new SupplierModel(supplier.getSupplierId(), supplier.getName(), supplier.getAddress(), supplier.getEmail(), supplier.getContact(), supplier.getFax(), supplier.getCordintatorName(), supplier.getCordinatorContact(), supplier.getAgentName(), supplier.getAgenNo(), supplier.getSupplierAdded());
        return model;
    }

    @Override
    public int saveSupplier(SupplierDTO supplier) throws ClassNotFoundException, SQLException {
        SupplierService service = (SupplierService) ServiceFactory.getInstance().getService(SuperService.ServiceType.SUPPLIER);
        return service.saveSupplier(supplier);
    }

    @Override
    public int changeSupplier(SupplierDTO supplier) throws ClassNotFoundException, SQLException {
        SupplierService service = (SupplierService) ServiceFactory.getInstance().getService(SuperService.ServiceType.SUPPLIER);
        return service.changeSupplier(supplier);
    }

    @Override
    public int removeSupplier(String supplierId) throws ClassNotFoundException, SQLException {
        SupplierService service = (SupplierService) ServiceFactory.getInstance().getService(SuperService.ServiceType.SUPPLIER);
        return service.removeSupplier(supplierId);
    }

    @Override
    public ArrayList<SupplierDTO> getSuppliers(String supplierName) throws ClassNotFoundException, SQLException {
        SupplierService service = (SupplierService) ServiceFactory.getInstance().getService(SuperService.ServiceType.SUPPLIER);
        return service.getSuppliers(supplierName);
    }
    
}
