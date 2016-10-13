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
import java.util.ArrayList;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.custom.IdPoolDAO;
import lk.ijse.winestores.dao.custom.SubCategoryDAO;
import lk.ijse.winestores.dao.dto.IdPoolDTO;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.resource.db.DatabaseResourceConnection;

/**
 *
 * @author Ranjith Suranga
 */
public class SubCategoryDAOImpl implements SubCategoryDAO {

    @Override
    public boolean isExists(String queryId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(SUB_CATEGORY_CHECK_EXISTENCY_BY_ID);
        pstm.setObject(1, queryId);
        ResultSet rst = pstm.executeQuery();
        boolean result = rst.next();
        con.getConnection().close();
        return result;
    }

    @Override
    public String readLastId() throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(SUB_CATEGORY_READ_LAST_ID_SQL);
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
    public int createSubCategory(SubCategoryDTO dto) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(SUB_CATEGORY_INSERTION_SQL);
        pstm.setObject(1, dto.getSubCategoryId());
        pstm.setObject(2, dto.getSubCategoryName());
        int result = pstm.executeUpdate();
        con.getConnection().close();
        return result;
    }

    @Override
    public int updateSubCategory(SubCategoryDTO dto) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(SUB_CATEGORY_UPDATE_SQL);
        pstm.setObject(1, dto.getSubCategoryId());
        pstm.setObject(2, dto.getSubCategoryName());
        pstm.setObject(3, dto.getSubCategoryId());
        int result = pstm.executeUpdate();
        con.getConnection().close();
        return result;
    }

    @Override
    public boolean deleteSubCategory(String majorCategoryId, String subCategoryId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection dbCon = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        Connection con = dbCon.getConnection();
        // Starting transaction
        con.setAutoCommit(false);
        PreparedStatement pstm = con.prepareStatement("DELETE FROM main_sub_category WHERE sub_category_id=? AND main_cat_id=?");
        pstm.setObject(1, subCategoryId);
        pstm.setObject(2, majorCategoryId);
        int affectedRows = pstm.executeUpdate();
        if (affectedRows != 0) {

            // Now we need to find out whether this sub category is used by another main category
            pstm = con.prepareStatement("SELECT * FROM main_sub_category WHERE sub_category_id=?");
            pstm.setString(1, subCategoryId);
            ResultSet rst = pstm.executeQuery();

            if (rst.next()) {
                // So, yep, there are other main categories which use this sub cateogry
                con.commit();
            } else {
                pstm = con.prepareStatement(SUB_CATEGORY_DELETION_SQL);
                pstm.setObject(1, subCategoryId);
                affectedRows = pstm.executeUpdate();
                if (affectedRows != 0) {
                    IdPoolDAO dao = (IdPoolDAO) DAOFactory.getInstance().getDAO(DAOType.ID_POOL);
                    IdPoolDTO dto = new IdPoolDTO(subCategoryId, "sub_category");
                    affectedRows = dao.createID(con, dto);
                    if (affectedRows != 0) {
                        con.commit();
                    } else {
                        con.rollback();
                    }
                } else {
                    con.rollback();
                }
            }

        }
        con.setAutoCommit(true);
        con.close();
        return (affectedRows != 0);
    }

    @Override
    public SubCategoryDTO readSubCategoryByName(String subCateogryName) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(SUB_CATEGORY_READ_BY_NAME);
        pstm.setObject(1, subCateogryName);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            SubCategoryDTO subCategoryDTO = new SubCategoryDTO(rst.getString(1), rst.getString(2));
            con.getConnection().close();
            return subCategoryDTO;
        }
        con.getConnection().close();
        return null;
    }

    @Override
    public ArrayList<SubCategoryDTO> readAllSubCategories() throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(READALL_SQL + TABLE_NAME);
        ResultSet rst = pstm.executeQuery();
        ArrayList<SubCategoryDTO> al = null;

        while (rst.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            SubCategoryDTO dto = new SubCategoryDTO(rst.getString(1), rst.getString(2));
            al.add(dto);
        }
        con.getConnection().close();
        return al;
    }

}
