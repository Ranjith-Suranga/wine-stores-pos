/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.dto.SupplierDTO;
import lk.ijse.winestores.service.SuperService;

/**
 *
 * @author Ranjith Suranga
 */
public interface SupplierService extends SuperService{
    
    public ArrayList<SupplierDTO> getAllSuppliers()throws ClassNotFoundException, SQLException;
    public SupplierDTO getSupplierById(String supplierId) throws ClassNotFoundException, SQLException;
    
    public int saveSupplier(SupplierDTO supplier)throws ClassNotFoundException, SQLException;
    public int changeSupplier(SupplierDTO supplier) throws ClassNotFoundException, SQLException;
    public int removeSupplier(String supplierId) throws ClassNotFoundException, SQLException;
    public ArrayList<SupplierDTO> getSuppliers(String supplierName) throws ClassNotFoundException,SQLException;
    
}
