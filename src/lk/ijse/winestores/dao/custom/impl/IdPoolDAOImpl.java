/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lk.ijse.winestores.dao.custom.IdPoolDAO;
import lk.ijse.winestores.dao.dto.IdPoolDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.resource.db.DatabaseResourceConnection;

/**
 *
 * @author Ranjith Suranga
 */
public class IdPoolDAOImpl implements IdPoolDAO {

    @Override
    public int createID(IdPoolDTO dto) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection dbResourceConnection = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstmIdExists = dbResourceConnection.getConnection().prepareStatement("SELECT * FROM id_pool WHERE old_id=? AND table_name=?");
        pstmIdExists.setObject(1, dto.getOldId());
        pstmIdExists.setObject(2, dto.getTableName());
        ResultSet rst = pstmIdExists.executeQuery();
        int result = 1;
        if (!rst.next()) {
            PreparedStatement pstm = dbResourceConnection.getConnection().prepareStatement(ID_POOL_INSERTION_SQL);
            pstm.setObject(1, dto.getOldId());
            pstm.setObject(2, dto.getTableName());
            result = pstm.executeUpdate();
        }
        dbResourceConnection.getConnection().close();
        return result;
    }
    
    @Override
    public int createID(Connection con, IdPoolDTO dto) throws ClassNotFoundException, SQLException {
        PreparedStatement pstmIdExists = con.prepareStatement("SELECT * FROM id_pool WHERE old_id=? AND table_name=?");
        pstmIdExists.setObject(1, dto.getOldId());
        pstmIdExists.setObject(2, dto.getTableName());
        ResultSet rst = pstmIdExists.executeQuery();
        int result = 1;
        if (!rst.next()) {
            PreparedStatement pstm = con.prepareStatement(ID_POOL_INSERTION_SQL);
            pstm.setObject(1, dto.getOldId());
            pstm.setObject(2, dto.getTableName());
            result = pstm.executeUpdate();
        }
        return result;
    }    

    @Override
    public int updateID(IdPoolDTO dto) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection dbResourceConnection = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = dbResourceConnection.getConnection().prepareStatement(ID_POOL_UPDATE_SQL);
        pstm.setObject(1, dto.getOldId());
        pstm.setObject(2, dto.getTableName());
        pstm.setObject(3, dto.getOldId());
        int result = pstm.executeUpdate();
        dbResourceConnection.getConnection().close();
        return result;
    }

    @Override
    public int deleteID(IdPoolDTO dto) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection dbResourceConnection = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = dbResourceConnection.getConnection().prepareStatement(ID_POOL_DELETION_SQL);
        pstm.setObject(1, dto.getOldId());
        pstm.setObject(2, dto.getTableName());
        int result = pstm.executeUpdate();
        dbResourceConnection.getConnection().close();
        return result;
    }

    @Override
    public String isIDExists(DAOType table) throws ClassNotFoundException, SQLException {

        String tblName;

        switch (table) {
            case MAJOR_CATEGORY:
                tblName = "main_category";
                break;
            case SUB_CATEGORY:
                tblName = "sub_category";
                break;
            case MAIN_SUB_CATEGORY:
                tblName = "main_sub_category";
                break;
            case ITEM:
                tblName = "item";
                break;
            case SUPPLIER:
                tblName = "supplier";
                break;
            case GRN:
                tblName = "grn";
                break;
            case GRN_EMPTY_BOTTLE_DETAIL:
                tblName = "grn_empty_bottle_detail";
                break;
            case BATCH:
                tblName = "batch";
                break;
            case GRN_DETAIL:
                tblName = "grn_detail";
                break;
            case ITEM_BATCH_GRN_DETAIL:
                tblName = "item_batch_grn_detail";
            default:
                tblName = null;
        }

        if (tblName == null) {
            return null;
        }

        DatabaseResourceConnection dbResourceConnection = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = dbResourceConnection.getConnection().prepareStatement("SELECT * FROM id_pool WHERE table_name=? ;");
        pstm.setObject(1, tblName);
        ResultSet rst = pstm.executeQuery();
        String result = null;
        if (rst.next()) {
            result = rst.getString(1);
        }
        dbResourceConnection.getConnection().close();
        return result;
    }

    @Override
    public boolean isExists(String queryId) {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String readLastId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
//    public boolean isExists(String columnName, String queryId) throws ClassNotFoundException, SQLException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
