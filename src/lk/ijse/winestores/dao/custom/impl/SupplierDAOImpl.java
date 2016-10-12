/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.custom.IdPoolDAO;
import lk.ijse.winestores.dao.custom.SupplierDAO;
import lk.ijse.winestores.dao.dto.IdPoolDTO;
import lk.ijse.winestores.dao.dto.SupplierDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.resource.db.DatabaseResourceConnection;

/**
 *
 * @author Ranjith Suranga
 */
public class SupplierDAOImpl implements SupplierDAO {

    @Override
    public ArrayList<SupplierDTO> readAllSuppliers() throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(READ_ALL_SUPPLIERS_SQL);
        ResultSet rst = pstm.executeQuery();
        ArrayList<SupplierDTO> al = null;

        while (rst.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }
            SupplierDTO dto = new SupplierDTO(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6), rst.getString(7), rst.getString(8), rst.getString(9), rst.getString(10), rst.getString(11));
            al.add(dto);
        }
        con.getConnection().close();
        return al;
    }

    /*
    =============================
        ID Generator Methods
    =============================
     */
    @Override
    public boolean isExists(String queryId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(SUPPLIER_CHECK_EXISTENCY_BY_ID);
        pstm.setObject(1, queryId);
        ResultSet rst = pstm.executeQuery();
        boolean result = rst.next();
        con.getConnection().close();
        return result;
    }

    @Override
    public String readLastId() throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(SUPPLIER_READ_LAST_ID_SQL);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            String lastId = rst.getString(1);
            con.getConnection().close();
            return lastId;
        } else {
            con.getConnection().close();
            return null;
        }
    }

    @Override
    public SupplierDTO readSupplierById(String supplierId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(READ_SUPPLIER_BY_ID);
        pstm.setObject(1, supplierId);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            SupplierDTO dto = new SupplierDTO(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6), rst.getString(7), rst.getString(8), rst.getString(9), rst.getString(10), rst.getString(11));
            con.getConnection().close();
            return dto;
        }
        con.getConnection().close();
        return null;
    }

    @Override
    public int createSupplier(SupplierDTO supplier) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection dbCon = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = dbCon.getConnection().prepareStatement("INSERT INTO supplier VALUES(?,?,?,?,?,?,?,?,?,?,?)");
        pstm.setObject(1, supplier.getSupplierId());
        pstm.setObject(2, supplier.getName().toUpperCase());
        pstm.setObject(3, supplier.getAddress());
        pstm.setObject(4, supplier.getEmail());
        pstm.setObject(5, supplier.getContact());
        pstm.setObject(6, supplier.getFax());
        pstm.setObject(7, supplier.getCordintatorName());
        pstm.setObject(8, supplier.getCordinatorContact());
        pstm.setObject(9, supplier.getAgentName());
        pstm.setObject(10, supplier.getAgenNo());
        pstm.setObject(11, supplier.getSupplierAdded());
        int result = pstm.executeUpdate();
        dbCon.getConnection().close();
        return result;
    }

    @Override
    public int updateSupplier(SupplierDTO supplier) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection dbCon = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = dbCon.getConnection().prepareStatement("UPDATE supplier SET name=?,address=?,email=?,contact=?,fax=?,cordintator_name=?,cordinator_contact=?,agent_name=?,agen_no=?,supplier_added=? WHERE supplier_id=?");
        pstm.setObject(1, supplier.getName());
        pstm.setObject(2, supplier.getAddress());
        pstm.setObject(3, supplier.getEmail());
        pstm.setObject(4, supplier.getContact());
        pstm.setObject(5, supplier.getFax());
        pstm.setObject(6, supplier.getCordintatorName());
        pstm.setObject(7, supplier.getCordinatorContact());
        pstm.setObject(8, supplier.getAgentName());
        pstm.setObject(9, supplier.getAgenNo());
        pstm.setObject(10, supplier.getSupplierAdded());
        pstm.setObject(11, supplier.getSupplierId());
        int result = pstm.executeUpdate();
        dbCon.getConnection().close();
        return result;
    }

    @Override
    public int deleteSupplier(String supplierId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection dbCon = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = dbCon.getConnection().prepareStatement("DELETE FROM supplier WHERE supplier_id=?");
        dbCon.getConnection().setAutoCommit(false);
        pstm.setObject(1, supplierId);
        int result = pstm.executeUpdate();
        if (result > 0) {
            IdPoolDAO dao = (IdPoolDAO) DAOFactory.getInstance().getDAO(DAOType.ID_POOL);
            IdPoolDTO dto = new IdPoolDTO(supplierId, "supplier");
            result = dao.createID(dbCon.getConnection(), dto);
            if (result != 0) {
                dbCon.getConnection().commit();
            } else {
                dbCon.getConnection().rollback();
            }
        } else {
            dbCon.getConnection().rollback();
        }
        dbCon.getConnection().setAutoCommit(true);
        dbCon.getConnection().close();
        return result;
    }

    @Override
    public ArrayList<SupplierDTO> readSuppliers(String supplierName) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection dbCon = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        Statement stm = dbCon.getConnection().createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM supplier WHERE name LIKE '" + supplierName + "%'");
        ArrayList<SupplierDTO> al = null;

        while (rst.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            SupplierDTO dto = new SupplierDTO(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6), rst.getString(7), rst.getString(8), rst.getString(9), rst.getString(10), rst.getString(11));
            al.add(dto);
        }

        dbCon.getConnection().close();
        return al;
    }

}
