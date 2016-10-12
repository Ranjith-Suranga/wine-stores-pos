/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom.impl;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.SupplierDAO;
import lk.ijse.winestores.dao.dto.SupplierDTO;
import lk.ijse.winestores.service.custom.SupplierService;
import lk.ijse.winestores.service.idgenerator.GenerateId;

/**
 *
 * @author Ranjith Suranga
 */
public class SupplierServiceImpl implements SupplierService{

    @Override
    public ArrayList<SupplierDTO> getAllSuppliers() throws ClassNotFoundException, SQLException {
        SupplierDAO dao = (SupplierDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUPPLIER);
        return dao.readAllSuppliers();
    }

    @Override
    public SupplierDTO getSupplierById(String supplierId) throws ClassNotFoundException, SQLException {
        SupplierDAO dao = (SupplierDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUPPLIER);
        return dao.readSupplierById(supplierId);
    }

    @Override
    public int saveSupplier(SupplierDTO supplier) throws ClassNotFoundException, SQLException {
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMaximumIntegerDigits(3);
        nf.setMinimumIntegerDigits(3);
        nf.setGroupingUsed(false);
        
        // Setting supplier id
        supplier.setSupplierId(GenerateId.getNewId(SuperDAO.DAOType.SUPPLIER, null, nf));
        
        SupplierDAO dao = (SupplierDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUPPLIER);
        return dao.createSupplier(supplier);
    }

    @Override
    public int changeSupplier(SupplierDTO supplier) throws ClassNotFoundException, SQLException {
        SupplierDAO dao = (SupplierDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUPPLIER);
        return dao.updateSupplier(supplier);
    }

    @Override
    public int removeSupplier(String supplierId) throws ClassNotFoundException, SQLException {
        SupplierDAO dao = (SupplierDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUPPLIER);
        return dao.deleteSupplier(supplierId);
    }

    @Override
    public ArrayList<SupplierDTO> getSuppliers(String supplierName) throws ClassNotFoundException, SQLException {
        SupplierDAO dao = (SupplierDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUPPLIER);
        return dao.readSuppliers(supplierName);
    }

}
