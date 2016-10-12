/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom.impl;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.IdPoolDAO;
import lk.ijse.winestores.dao.custom.MajorCategoryDAO;
import lk.ijse.winestores.dao.dto.IdPoolDTO;
import lk.ijse.winestores.dao.dto.MajorCategoryDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.resource.db.DatabaseResourceConnection;

/**
 *
 * @author Ranjith Suranga
 */
public class MajorCategoryDAOImpl implements MajorCategoryDAO {

    @Override
    public int createMajorCategory(MajorCategoryDTO dto) throws ClassNotFoundException, MySQLIntegrityConstraintViolationException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAJOR_CATEGORY_INSERTION_SQL);
        pstm.setObject(1, dto.getMainCatId());
        pstm.setObject(2, dto.getCategoryName().toUpperCase());
        int result = pstm.executeUpdate();
        con.getConnection().close();
        return result;
    }

    @Override
    public int updateMajorCategory(MajorCategoryDTO dto) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAJOR_CATEGORY_UPDATE_SQL);
        pstm.setObject(1, dto.getMainCatId());
        pstm.setObject(2, dto.getCategoryName().toUpperCase());
        pstm.setObject(3, dto.getMainCatId());
        int result = pstm.executeUpdate();
        con.getConnection().close();
        return result;
    }

    @Override
    public boolean deleteMajorCategory(String categoryId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAJOR_CATEGORY_DELETION_SQL);
        con.getConnection().setAutoCommit(false);
        pstm.setObject(1, categoryId);
        int affectedRows = pstm.executeUpdate();
        if (affectedRows != 0){
            IdPoolDAO idPoolDAO = (IdPoolDAO) DAOFactory.getInstance().getDAO(DAOType.ID_POOL);
            IdPoolDTO idPoolDTO = new IdPoolDTO(categoryId, "main_category");
            affectedRows = idPoolDAO.createID(idPoolDTO);
            if (affectedRows != 0){
                con.getConnection().commit();
            }else{
                con.getConnection().rollback();
            }
        }
        con.getConnection().setAutoCommit(true);
        con.getConnection().close();
        return (affectedRows !=0);
    }

    @Override
    public MajorCategoryDTO readMajorCategoryById(String queryId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAJOR_CATEGORY_READ_SQL);
        pstm.setObject(1, queryId);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            MajorCategoryDTO majorCategoryDTO = new MajorCategoryDTO(queryId, rst.getString(2).toUpperCase());
            con.getConnection().close();
            return majorCategoryDTO;
        } else {
            con.getConnection().close();
            return null;
        }

    }

    @Override
    public ArrayList<MajorCategoryDTO> readAllMajorCategory() throws ClassNotFoundException, SQLException {
        ArrayList<MajorCategoryDTO> al = null;
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(READALL_SQL + TABLE_NAME);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            MajorCategoryDTO dto = new MajorCategoryDTO(rst.getString(1), rst.getString(2).toUpperCase());
            al.add(dto);

        }

        con.getConnection().close();
        return al;
    }

//    @Override
//    public boolean isMajorCategory(String queryId) throws ClassNotFoundException, SQLException {
//        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
//        PreparedStatement pstm = con.getConnection().prepareStatement(MAJOR_CATEGORY_CHECK_EXISTENCY_BY_ID);
//        pstm.setObject(1, queryId);
//        ResultSet rst = pstm.executeQuery();
//        return (rst.next()) ? true : false;
//    }
    @Override
    public boolean isExists(String queryId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAJOR_CATEGORY_CHECK_EXISTENCY_BY_ID);
        pstm.setObject(1, queryId);
        ResultSet rst = pstm.executeQuery();
        boolean result = rst.next();
        con.getConnection().close();
        return result;
    }

    @Override
    public String readLastId() throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAJOR_CATEGORY_READ_LAST_ID_SQL);
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

//    @Override
//    public boolean isExists(String columnName, String queryId) throws ClassNotFoundException, SQLException {
//        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
//        PreparedStatement pstm = con.getConnection().prepareStatement(MAJOR_CATEGORY_CHECK_EXISTENCY);
//        pstm.setObject(1, columnName);
//        pstm.setObject(1, queryId);        
//        ResultSet rst = pstm.executeQuery();
//        return (rst.next());
//    }
    @Override
    public MajorCategoryDTO readMajorCategoryByName(String majorCategoryName) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement("SELECT * FROM main_category WHERE category_name=?");
        pstm.setObject(1, majorCategoryName);
        ResultSet rst = pstm.executeQuery();
        MajorCategoryDTO dto = null;

        if (rst.next()) {
            dto = new MajorCategoryDTO(rst.getString(1), rst.getString(2));
        }
        
        con.getConnection().close();
        return dto;
    }

}
