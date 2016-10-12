/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.custom.MainSubCategoryDAO;
import lk.ijse.winestores.dao.dto.MainSubCategoryDTO;
import lk.ijse.winestores.dao.dto.MajorCategoryDTO;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.resource.db.DatabaseResourceConnection;

/**
 *
 * @author Ranjith Suranga
 */
public class MainSubCategoryDAOImpl implements MainSubCategoryDAO {

    @Override
    public boolean isExists(String queryId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAIN_SUB_CATEGORY_CHECK_EXISTENCY_BY_ID);
        pstm.setObject(1, queryId);
        ResultSet rst = pstm.executeQuery();
        boolean result = rst.next();
        con.getConnection().close();
        return result;
    }

    @Override
    public String readLastId() throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAIN_SUB_CATEGORY_READ_LAST_ID_SQL);
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
    public int createMainSubCategory(MainSubCategoryDTO dto) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAIN_SUB_CATEGORY_INSERTION_SQL);
        pstm.setObject(1, dto.getMainSubCategoryId());
        pstm.setObject(2, dto.getMainCatId());
        pstm.setObject(3, dto.getSubCategoryId());
        int result = pstm.executeUpdate();
        con.getConnection().close();
        return result;
    }

    @Override
    public int updateMainSubCategory(MainSubCategoryDTO dto) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAIN_SUB_CATEGORY_UPDATE_SQL);
        pstm.setObject(1, dto.getMainSubCategoryId());
        pstm.setObject(2, dto.getMainCatId());
        pstm.setObject(3, dto.getSubCategoryId());
        pstm.setObject(4, dto.getMainSubCategoryId());
        int result = pstm.executeUpdate();
        con.getConnection().close();
        return result;
    }

    @Override
    public int deleteMainSubCategory(MainSubCategoryDTO dto) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAIN_SUB_CATEGORY_DELETION_SQL);
        pstm.setObject(1, dto.getMainSubCategoryId());
        int result = pstm.executeUpdate();
        con.getConnection().close();
        return result;
    }

    @Override
    public ArrayList<SubCategoryDTO> readAllSubCategoriesByMajorCategoryId(String majorCategoryId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(READ_ALL_SUB_CATEGORIES_BY_MAJOR_CATEGORY_ID);
        pstm.setObject(1, majorCategoryId);
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

    @Override
    public ArrayList<MajorCategoryDTO> readAllMajorCategoriesBySubCategoryId(String subCategoryId) throws ClassNotFoundException, SQLException {

        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(READ_ALL_MAJOR_CATEGORIES_BY_SUB_CATEGORY_ID);
        pstm.setObject(1, subCategoryId);
        ResultSet rst = pstm.executeQuery();

        ArrayList<MajorCategoryDTO> al = null;

        while (rst.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            MajorCategoryDTO dto = new MajorCategoryDTO(rst.getString(1), rst.getString(2));
            al.add(dto);
        }
        con.getConnection().close();
        return al;
    }

    @Override
    public MainSubCategoryDTO readMainSubCategory(String mainCategoryId, String subCategoryId) throws ClassNotFoundException, SQLException {

        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement("SELECT * FROM `main_sub_category` WHERE main_cat_id=? AND sub_category_id=?");
        pstm.setObject(1, mainCategoryId);
        pstm.setObject(2, subCategoryId);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            MainSubCategoryDTO mainSubCategoryDTO = new MainSubCategoryDTO(rst.getString(1), rst.getString(2), rst.getString(3));
            con.getConnection().close();
            return mainSubCategoryDTO;
        } else {
            con.getConnection().close();
            return null;
        }

    }

}
