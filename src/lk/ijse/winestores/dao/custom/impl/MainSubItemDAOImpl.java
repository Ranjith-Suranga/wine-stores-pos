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
import lk.ijse.winestores.dao.custom.MainSubCategoryDAO;
import lk.ijse.winestores.dao.custom.MainSubItemDAO;
import lk.ijse.winestores.dao.dto.CustomItemDTO;
import lk.ijse.winestores.dao.dto.IdPoolDTO;
import lk.ijse.winestores.dao.dto.MainSubCategoryDTO;
import lk.ijse.winestores.dao.dto.MainSubItemDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.resource.db.DatabaseResourceConnection;

/**
 *
 * @author Ranjith Suranga
 */
public class MainSubItemDAOImpl implements MainSubItemDAO {

    // Dependencies
    private Connection connection;

    // Dependecy Injection
    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean isExists(String queryId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAIN_SUB_ITEM_CHECK_EXISTENCY_BY_ID);
        pstm.setObject(1, queryId);
        ResultSet rst = pstm.executeQuery();
        boolean result = rst.next();
        con.getConnection().close();
        return result;
    }

    @Override
    public String readLastId() throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAIN_SUB_ITEM_READ_LAST_ID_SQL);
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
    public int createMainSubItem(MainSubItemDTO dto) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAIN_SUB_ITEM_INSERTION_SQL);
        pstm.setObject(1, dto.getMainSubItemId());
        pstm.setObject(2, dto.getMainSubId());
        pstm.setObject(3, dto.getItemId());
        pstm.setObject(4, dto.getItemCode());
        pstm.setObject(5, dto.getCapacity());
        pstm.setObject(6, dto.getBarcode());
        int result = pstm.executeUpdate();
        con.getConnection().close();
        return result;
    }

    @Override
    public int updateMainSubItem(MainSubItemDTO dto) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAIN_SUB_ITEM_UPDATE_SQL);
        pstm.setObject(1, dto.getCapacity());
        pstm.setObject(2, dto.getBarcode());
        pstm.setObject(3, dto.getItemCode());
        int result = pstm.executeUpdate();
        con.getConnection().close();
        return result;
    }

    @Override
    public boolean deleteMainSubItem(String itemCode) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection dbCon = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        Connection con = dbCon.getConnection();

        // Setting the transaction
        con.setAutoCommit(false);

        PreparedStatement pstm = con.prepareStatement("SELECT item_id,item_code FROM main_sub_item WHERE item_code=?");
        pstm.setObject(1, itemCode);
        ResultSet rst = pstm.executeQuery();
        int affectedRows = 0;
        if (rst.next()) {

            String itemId = rst.getString(1);

            pstm = con.prepareStatement("DELETE FROM main_sub_item WHERE item_code=?");
            pstm.setObject(1, itemCode);
            affectedRows = pstm.executeUpdate();
            if (affectedRows != 0) {

                // Let's check whether that item name is used more than once...
                pstm = con.prepareStatement("SELECT * FROM wine_stores.main_sub_item where item_id =?");
                pstm.setString(1, itemId);
                rst = pstm.executeQuery();
                
                if (!rst.next()) {

                    pstm = con.prepareStatement("DELETE FROM item WHERE item_id =?");
                    pstm.setObject(1, itemId);
                    affectedRows = pstm.executeUpdate();
                    if (affectedRows != 0) {
                        IdPoolDAO dao = (IdPoolDAO) DAOFactory.getInstance().getDAO(DAOType.ID_POOL);
                        IdPoolDTO dto = new IdPoolDTO(itemId, "item");
                        affectedRows = dao.createID(con, dto);
                        if (affectedRows != 0) {
                            con.commit();
                        } else {
                            con.rollback();
                        }
                    } else {
                        con.rollback();
                    }
                    
                } else{
                    // If it is used more than once, we can't delete it
                    
                    con.commit();
                }
            }
        }

        // Setting connection to it's default state
        con.setAutoCommit(true);
        con.close();
        return (affectedRows != 0);
    }

    @Override
    public ArrayList<CustomItemDTO> readItems(String majorCategoryId, String subCategoryId) throws ClassNotFoundException, SQLException {

        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(READ_ITEMS_BY_MAJOR_SUB_IDS);

        MainSubCategoryDAO dao = (MainSubCategoryDAO) DAOFactory.getInstance().getDAO(DAOType.MAIN_SUB_CATEGORY);
        MainSubCategoryDTO mainSubCategory = dao.readMainSubCategory(majorCategoryId, subCategoryId);

        if (mainSubCategory == null) {
            con.getConnection().close();
            return null;
        }

        pstm.setObject(1, mainSubCategory.getMainSubCategoryId());
        ResultSet rst = pstm.executeQuery();

        ArrayList<CustomItemDTO> al = null;

        while (rst.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            CustomItemDTO dto = new CustomItemDTO(rst.getString(1), rst.getString(5), rst.getString(2), rst.getString(3), rst.getString(4), majorCategoryId, subCategoryId);
            al.add(dto);
        }

        con.getConnection().close();
        return al;

    }

    @Override
    public boolean isItemDepeneded(String itemId) throws ClassNotFoundException, SQLException {

        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(MAIN_SUB_ITEM_CHECK_DEPENDECY);
        pstm.setObject(1, itemId);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {

            boolean result = rst.getInt(1) > 1;
            con.getConnection().close();
            return result;
        } else {
            con.getConnection().close();
            return false;
        }
    }

    @Override
    public String readId(String itemCode) throws ClassNotFoundException, SQLException {

        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(READ_MAIN_SUB_ITEM_ID_BY_ITEM_CODE);
        pstm.setObject(1, itemCode);
        ResultSet rst = pstm.executeQuery();
        String id = null;
        if (rst.next()) {
            id = rst.getString(1);
        }
        con.getConnection().close();
        return id;

    }

    @Override
    public String[] getItemMainDetails(String itemCode) throws ClassNotFoundException, SQLException {

        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement("SELECT main_category.category_name, sub_category.sub_category_name, item.item_name, main_sub_item.item_code FROM main_sub_item JOIN item ON item.item_id = main_sub_item.item_id JOIN main_sub_category ON main_sub_category.main_sub_category_id = main_sub_item.main_sub_id JOIN main_category ON main_category.main_cat_id = main_sub_category.main_cat_id JOIN sub_category ON sub_category.sub_category_id = main_sub_category.sub_category_id WHERE main_sub_item.main_sub_item_id = ?");
        pstm.setString(1, itemCode);

        ResultSet rst = pstm.executeQuery();

        String[] itemDetails = null;

        if (rst.next()) {
            itemDetails = new String[4];
            itemDetails[0] = rst.getString(1);
            itemDetails[1] = rst.getString(2);
            itemDetails[2] = rst.getString(3);
            itemDetails[3] = rst.getString(4);
        }

        con.getConnection().close();
        return itemDetails;

    }

    @Override
    public MainSubItemDTO readMainSubItemByPK(String pK) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection dbConnection = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        Connection con = dbConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement("SELECT * FROM main_sub_item WHERE main_sub_item_id=?");
        pstm.setObject(1, pK);
        ResultSet rst = pstm.executeQuery();
        MainSubItemDTO dto = null;

        if (rst.next()) {
            dto = new MainSubItemDTO(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6));
        }
        con.close();
        return dto;
    }

    @Override
    public String readBarCodeByItemCode(String itemCode) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection dbConnection = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        Connection con = dbConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement("SELECT * FROM main_sub_item WHERE item_code=?");
        pstm.setObject(1, itemCode);
        ResultSet rst = pstm.executeQuery();
        String barCode = null;
        if (rst.next()) {
            barCode = rst.getString(6);
        }
        con.close();
        return barCode;
    }

    @Override
    public boolean updateBarCode(String itemCode, String barCode) throws ClassNotFoundException, SQLException {

        PreparedStatement pstm = connection.prepareStatement("UPDATE main_sub_item SET barcode =? WHERE item_code = ? ");
        pstm.setString(1, barCode);
        pstm.setString(2, itemCode);
        int affectedRows = pstm.executeUpdate();

        return (affectedRows != 0);
    }

}
