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
import java.sql.Statement;
import java.util.ArrayList;
import lk.ijse.winestores.dao.custom.ItemDetailsDAO;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.resource.db.DatabaseResourceConnection;

/**
 *
 * @author Ranjith Suranga
 */
public class ItemDetailsDAOImpl implements ItemDetailsDAO {

    private Connection con;

    @Override
    public void setConnection(Connection con) throws ClassNotFoundException, SQLException {
        this.con = con;
    }

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        return con;
    }

    @Override
    public boolean isItemDetailsExists(String itemCode) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection connection = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = connection.getConnection().prepareStatement("SELECT * FROM item_details WHERE item_code=?");
        pstm.setString(1, itemCode);
        ResultSet rst = pstm.executeQuery();
        boolean result = rst.next();
        connection.getConnection().close();
        return result;
    }

    @Override
    public int readCurrentQty(String itemCode) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection connection = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = connection.getConnection().prepareStatement("SELECT * FROM item_details WHERE item_code=?");
        pstm.setString(1, itemCode);
        ResultSet rst = pstm.executeQuery();
        int qty = 0;
        if (rst.next()) {
            qty = rst.getInt(2);
        }
        connection.getConnection().close();
        return qty;
    }

    @Override
    public boolean updateCurrentQty(Connection con, String itemCode, int newQty) throws ClassNotFoundException, SQLException {

        boolean closeConnection = false;

        if (con == null) {
            DatabaseResourceConnection dBconnection = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
            con = dBconnection.getConnection();
            closeConnection = true;
        }

        PreparedStatement pstm = con.prepareStatement("UPDATE item_details SET qty=? WHERE item_code=?");
        pstm.setInt(1, newQty);
        pstm.setString(2, itemCode);
        int result = pstm.executeUpdate();

        if (closeConnection) {
            con.close();
        }

        return (result != 0);
    }

    @Override
    public ArrayList<ItemDetailsDTO> readItemDetails(QueryType queryType, String queryWord) throws ClassNotFoundException, SQLException {

        String sql = "SELECT * FROM main_sub_item INNER JOIN item_details ON main_sub_item.item_code = item_details.item_code INNER JOIN item ON item.item_id = main_sub_item.item_id WHERE ";

        switch (queryType) {
            case ITEM_CODE:
                sql += " main_sub_item.item_code";
                break;
            case BARCODE:
                sql += "barcode";
                break;
            case ITEM_NAME:
                sql += "item_name";
                break;
            case SELLING_PRICE:
                sql += "selling_price";
                break;
        }

        if (queryType != QueryType.BARCODE) {
            sql += " LIKE '" + queryWord + "%'";
        }else{
            sql += " = '" + queryWord + "'";
        }

        DatabaseResourceConnection connection = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        Statement stm = connection.getConnection().createStatement();
        ResultSet rst = stm.executeQuery(sql);

        ArrayList<ItemDetailsDTO> al = null;

        while (rst.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            ItemDetailsDTO dto = new ItemDetailsDTO(rst.getString("item_name"), rst.getString("item_code"), rst.getInt("qty"), rst.getDouble("selling_price"), rst.getDouble("buying_price"));
            al.add(dto);
        }

        return al;
    }

    @Override
    public boolean isExists(String queryId) throws ClassNotFoundException, SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String readLastId() throws ClassNotFoundException, SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateItemDetails(Connection con, ItemDetailsDTO dto) throws ClassNotFoundException, SQLException {

        boolean closeConnection = false;

        // First let's check whether we have to use new connection or not?
        if (con == null) {
            DatabaseResourceConnection dbConnection = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
            con = dbConnection.getConnection();
            closeConnection = true;
        }

        PreparedStatement pstm = con.prepareStatement("UPDATE item_details SET item_code=?, qty=?, selling_price=?, buying_price=? WHERE item_code=?");
        pstm.setObject(1, dto.getItemCode());
        pstm.setObject(2, dto.getQty());
        pstm.setObject(3, dto.getSellingPrice());
        pstm.setObject(4, dto.getBuyingPrice());
        pstm.setObject(5, dto.getItemCode());

        int result = pstm.executeUpdate();

        if (closeConnection) {
            con.close();
        }

        return (result != 0);

    }

    @Override
    public boolean createItemDetails(Connection con, ItemDetailsDTO dto) throws ClassNotFoundException, SQLException {

        boolean closeConnection = false;

        // First let's check whether we have to use new connection or not?
        if (con == null) {
            DatabaseResourceConnection dbConnection = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
            con = dbConnection.getConnection();
            closeConnection = true;
        }

        PreparedStatement pstm = con.prepareStatement("INSERT INTO item_details VALUES(?,?,?,?)");
        pstm.setString(1, dto.getItemCode());
        pstm.setInt(2, dto.getQty());
        pstm.setDouble(3, dto.getSellingPrice());
        pstm.setDouble(4, dto.getBuyingPrice());

        int result = pstm.executeUpdate();

        if (closeConnection) {
            con.close();
        }

        return (result != 0);

    }

    @Override
    public ArrayList<CustomItemDetailsDTO> readAllItems() throws ClassNotFoundException, SQLException {
        Statement stm = con.createStatement();
        ResultSet rstItems = stm.executeQuery("SELECT main_sub_item.item_code, item.item_name, main_sub_item.barcode, item_details.buying_price, item_details.selling_price, item_details.qty FROM item_details RIGHT OUTER JOIN  main_sub_item ON  item_details.item_code = main_sub_item.item_code INNER JOIN item ON item.item_id = main_sub_item.item_id ORDER BY item_name;");
        ArrayList<CustomItemDetailsDTO> al = null;
        while (rstItems.next()) {
            if (al == null) {
                al = new ArrayList<>();
            }
            CustomItemDetailsDTO dto = new CustomItemDetailsDTO(rstItems.getString(1),
                    rstItems.getString(2),
                    rstItems.getString(3),
                    rstItems.getDouble(4),
                    rstItems.getDouble(5),
                    rstItems.getInt(6));
            al.add(dto);
        }
        return al;
    }

}
