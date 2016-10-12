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
import lk.ijse.winestores.dao.custom.ItemDAO;
import lk.ijse.winestores.dao.dto.ItemDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.resource.db.DatabaseResourceConnection;

/**
 *
 * @author Ranjith Suranga
 */
public class ItemDAOImpl implements ItemDAO {

    @Override
    public boolean isExists(String queryId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(ITEM_CHECK_EXISTENCY_BY_ID);
        pstm.setObject(1, queryId);
        ResultSet rst = pstm.executeQuery();
        boolean result = rst.next();
        con.getConnection().close();
        return result;
    }

    @Override
    public String readLastId() throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(ITEM_READ_LAST_ID_SQL);
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
    public int createItem(ItemDTO dto) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(ITEM_INSERTION_SQL);
        pstm.setObject(1, dto.getItemId());
        pstm.setObject(2, dto.getItemName());
        int result = pstm.executeUpdate();
        con.getConnection().close();
        return result;
    }

    @Override
    public int updateItem(ItemDTO dto) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(ITEM_UPDATE_SQL);
        pstm.setObject(1, dto.getItemId());
        pstm.setObject(2, dto.getItemName());
        pstm.setObject(3, dto.getItemId());
        int result = pstm.executeUpdate();
        con.getConnection().close();
        return result;
    }

    @Override
    public int deleteItem(ItemDTO dto) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(ITEM_UPDATE_SQL);
        pstm.setObject(1, dto.getItemId());
        int result = pstm.executeUpdate();
        con.getConnection().close();
        return result;
    }

    @Override
    public ItemDTO readItemByItemName(String itemName) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(READ_ITEM_BY_ITEM_NAME);
        pstm.setObject(1, itemName);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            ItemDTO dto =  new ItemDTO(rst.getString(1), rst.getString(2));
            con.getConnection().close();
            return dto;
        } else {
            con.getConnection().close();
            return null;
        }
    }

    @Override
    public ArrayList<ItemDTO> readAllItems() throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(READ_ALL_ITEMS);
        ResultSet rst = pstm.executeQuery();

        ArrayList<ItemDTO> al = null;

        while (rst.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            ItemDTO dto = new ItemDTO(rst.getString(1), rst.getString(2));
            al.add(dto);

        }

        con.getConnection().close();
        return al;
    }

}
