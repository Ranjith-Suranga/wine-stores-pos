/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import lk.ijse.winestores.dao.custom.QueryDAO;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;
import lk.ijse.winestores.dao.dto.SupplierOrderDTO;
import lk.ijse.winestores.dao.dto.SupplierOrderDetailDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.resource.db.DatabaseResourceConnection;

/**
 *
 * @author Ranjith Suranga
 */
public class QueryDAOImpl implements QueryDAO {

    // Dependencies
    /*
    * When you open a connection, it's really important to close that connection
     */
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection dbConnection = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        return dbConnection.getConnection();
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
    public SubCategoryDTO readSubCategoryByItemCode(String itemCode) throws ClassNotFoundException, SQLException {
        Connection connection = this.getConnection();

        PreparedStatement pstm = connection.prepareStatement("SELECT sub_category.* FROM main_sub_item INNER JOIN main_sub_category ON main_sub_item.main_sub_id = main_sub_category.main_sub_category_id INNER JOIN sub_category ON main_sub_category.sub_category_id = sub_category.sub_category_id WHERE main_sub_item.item_code=?");
        pstm.setString(1, itemCode);
        ResultSet rst = pstm.executeQuery();

        SubCategoryDTO dto = null;
        if (rst.next()) {
            dto = new SubCategoryDTO(rst.getString(1), rst.getString(2));
        }

        connection.close();

        return dto;
    }

    @Override
    public boolean isSupplierOrderExits(int supplierOrderId) throws ClassNotFoundException, SQLException {

        Connection connection = this.getConnection();

        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM wine_stores.supplier_order WHERE order_id=?");
        pstm.setInt(1, supplierOrderId);
        ResultSet rst = pstm.executeQuery();
        boolean exits = rst.next();

        connection.close();

        return exits;

    }

    @Override
    public String readItemNameByItemCode(String itemCode) throws ClassNotFoundException, SQLException {

        Connection connection = this.getConnection();

        PreparedStatement pstm = connection.prepareStatement("SELECT item_name FROM wine_stores.main_sub_item INNER JOIN item ON item.item_id = main_sub_item.item_id WHERE item_code=?;");
        pstm.setString(1, itemCode);
        ResultSet rst = pstm.executeQuery();

        String itemName = null;

        if (rst.next()) {
            itemName = rst.getString(1);
        }

        connection.close();

        return itemName;

    }

    @Override
    public SupplierOrderDTO readSupplierOrder(int orderId) throws ClassNotFoundException, SQLException {

        Connection connection = this.getConnection();

        PreparedStatement pstm1 = connection.prepareStatement("SELECT * FROM wine_stores.supplier_order WHERE order_id=?;");
        pstm1.setInt(1, orderId);
        ResultSet rstSupplierOrder = pstm1.executeQuery();

        SupplierOrderDTO supplierOrderDTO = null;

        if (rstSupplierOrder.next()) {

            PreparedStatement pstm2 = connection.prepareStatement("SELECT * FROM wine_stores.supplier_order_detail WHERE order_id = ?;");
            pstm2.setInt(1, orderId);

            ResultSet rstOrderDetails = pstm2.executeQuery();

            ArrayList<SupplierOrderDetailDTO> al = null;

            while (rstOrderDetails.next()) {

                if (al == null) {
                    al = new ArrayList<>();
                }

                SupplierOrderDetailDTO dto = new SupplierOrderDetailDTO(
                        rstOrderDetails.getInt(1),
                        rstOrderDetails.getInt(2),
                        rstOrderDetails.getString(3),
                        rstOrderDetails.getInt(4),
                        new BigDecimal(rstOrderDetails.getString(5))
                );
                
                al.add(dto);

            }
            
            if (al.size() > 0){
                
                supplierOrderDTO = new SupplierOrderDTO(
                        orderId,
                        rstSupplierOrder.getString(2),
                        rstSupplierOrder.getDate(3),
                        rstSupplierOrder.getString(4), 
                        rstSupplierOrder.getString(5),
                        al);
                
            }

        }

        connection.close();

        return supplierOrderDTO;

    }

    @Override
    public ArrayList<EmptyBottleDTO> readAllEmptyBottles() throws ClassNotFoundException, SQLException {
        Connection connection = this.getConnection();
        
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM wine_stores.empty_bottle");
        
        ArrayList<EmptyBottleDTO> al = null;
        
        while (rst.next()){
            
            if (al == null){
                al = new ArrayList<>();
            }
            
            EmptyBottleDTO dto = new EmptyBottleDTO(rst.getString(1),rst.getDouble(2));
            al.add(dto);
        }
        
        connection.close();
        return al;
    }

    @Override
    public ArrayList<CustomerDTO> readAllCustomers() throws ClassNotFoundException, SQLException {
        
        Connection connection = this.getConnection();
        
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM wine_stores.customer");
        
        ArrayList<CustomerDTO> al = null;
        
        while(rst.next()){
            
            if (al == null){
                al = new ArrayList<>();
            }
            
            CustomerDTO dto = new CustomerDTO(
                    rst.getInt(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4));
            
            al.add(dto);
            
        }
        
        connection.close();
        
        return al;
    }

}
