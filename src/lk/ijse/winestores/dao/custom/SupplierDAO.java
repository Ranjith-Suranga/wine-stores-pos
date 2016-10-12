/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.SupplierDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface SupplierDAO extends SuperDAO{
    
    // ID Generator
    public static final String SUPPLIER_CHECK_EXISTENCY_BY_ID = "SELECT * FROM supplier WHERE supplier_id=? ;";
    public static final String SUPPLIER_READ_LAST_ID_SQL = "SELECT * FROM supplier ORDER BY supplier_id DESC LIMIT 1;";
    
    public static final String READ_ALL_SUPPLIERS_SQL = "SELECT * FROM `supplier`";
    public static final String READ_SUPPLIER_BY_ID = "SELECT * FROM `supplier` WHERE supplier_id=?";
    
    public ArrayList<SupplierDTO> readAllSuppliers()throws ClassNotFoundException, SQLException;
    public SupplierDTO readSupplierById(String supplierId) throws ClassNotFoundException, SQLException;
    
    public int createSupplier(SupplierDTO supplier)throws ClassNotFoundException, SQLException;
    public int updateSupplier(SupplierDTO supplier)throws ClassNotFoundException, SQLException;
    public int deleteSupplier(String supplierId)throws ClassNotFoundException, SQLException;
    public ArrayList<SupplierDTO> readSuppliers(String supplierName)throws ClassNotFoundException, SQLException;
    
}
