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
import java.util.Date;
import lk.ijse.winestores.dao.custom.QueryDAO;
import lk.ijse.winestores.dao.dto.ChequeDetailsDTO;
import lk.ijse.winestores.dao.dto.CreditOrderDTO;
import lk.ijse.winestores.dao.dto.CustomOrderDTO;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.dao.dto.DayEndDTO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;
import lk.ijse.winestores.dao.dto.OrderEmptyBottleDetailsDTO;
import lk.ijse.winestores.dao.dto.OrderItemDetailsDTO;
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

            if (al.size() > 0) {

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

        while (rst.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            EmptyBottleDTO dto = new EmptyBottleDTO(rst.getString(1), rst.getDouble(2));
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

        while (rst.next()) {

            if (al == null) {
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

    @Override
    public boolean hasCashOrder(int orderId) throws ClassNotFoundException, SQLException {
        Connection connection = this.getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT order_id FROM custom_order WHERE order_id=?");
        pstm.setInt(1, orderId);
        ResultSet rst = pstm.executeQuery();
        boolean hasOrder = rst.next();
        connection.close();
        return hasOrder;
    }

    @Override
    public ArrayList<OrderEmptyBottleDetailsDTO> readEmptyBottleDetails(int orderId) throws ClassNotFoundException, SQLException {
        Connection connection = this.getConnection();

        ArrayList<OrderEmptyBottleDetailsDTO> al = null;

        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM order_empty_bottle_details WHERE order_id=?");
        pstm.setObject(1, orderId);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            OrderEmptyBottleDetailsDTO dto = new OrderEmptyBottleDetailsDTO(rst.getString(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getDouble(5));
            al.add(dto);

        }

        connection.close();
        return al;
    }

    @Override
    public CustomOrderDTO readOrder(int orderId) throws ClassNotFoundException, SQLException {
        Connection connection = this.getConnection();

        CustomOrderDTO dto = null;

        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM custom_order WHERE order_id=?");
        pstm.setObject(1, orderId);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            dto = new CustomOrderDTO(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getBigDecimal(6),
                    rst.getString(5)
            );
        }

        connection.close();

        return dto;
    }

    @Override
    public EmptyBottleDTO readEmptyBottle(String emptyBottleType) throws ClassNotFoundException, SQLException {
        Connection connection = this.getConnection();

        EmptyBottleDTO dto = null;

        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM empty_bottle WHERE bottle_type=?");
        pstm.setString(1, emptyBottleType);

        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            dto = new EmptyBottleDTO(rst.getString(1), rst.getDouble(2));
        }

        connection.close();

        return dto;
    }

    @Override
    public ChequeDetailsDTO readChequeDetails(int orderId) throws ClassNotFoundException, SQLException {
        Connection connection = this.getConnection();

        ChequeDetailsDTO dto = null;

        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM cheque_details WHERE order_id=?");
        pstm.setInt(1, orderId);

        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            dto = new ChequeDetailsDTO(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5));
        }

        connection.close();

        return dto;
    }

    @Override
    public CustomerDTO readCustomer(int customerId) throws ClassNotFoundException, SQLException {
        Connection connection = this.getConnection();

        CustomerDTO dto = null;

        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM customer WHERE customer_id=?");
        pstm.setInt(1, customerId);;
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            dto = new CustomerDTO(rst.getInt(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4));
        }

        connection.close();

        return dto;
    }

    @Override
    public CreditOrderDTO readCreditOrder(int orderId) throws ClassNotFoundException, SQLException {
        Connection connection = this.getConnection();

        CreditOrderDTO dto = null;

        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM credit_order WHERE order_id = ?");
        pstm.setInt(1, orderId);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            dto = new CreditOrderDTO(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getString(5));
        }

        connection.close();

        return dto;
    }

    @Override
    public ArrayList<CreditOrderDTO> readCreditOrdersByCustomerName(String customerName) throws ClassNotFoundException, SQLException {

        ArrayList<CreditOrderDTO> al = null;

        Connection connection = this.getConnection();

        PreparedStatement pstm = connection.prepareStatement("SELECT credit_order.* FROM wine_stores.credit_order INNER JOIN customer ON customer.customer_id = credit_order.customer_id WHERE customer_name LIKE ?");
        pstm.setString(1, customerName + "%");

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            if (al == null) {
                al = new ArrayList<>();
            }
            CreditOrderDTO dto = new CreditOrderDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getString(5));
            al.add(dto);
        }

        connection.close();

        return al;

    }

    @Override
    public ArrayList<CreditOrderDTO> readCreditOrdersByDatePeriod(Date fromDate, Date toDate) throws ClassNotFoundException, SQLException {

        ArrayList<CreditOrderDTO> al = null;

        Connection connection = this.getConnection();

        PreparedStatement pstm = connection.prepareStatement("SELECT credit_order.* FROM wine_stores.credit_order WHERE order_date BETWEEN ? AND ?;");
        pstm.setDate(1, new java.sql.Date(fromDate.getTime()));
        pstm.setDate(2, new java.sql.Date(toDate.getTime()));

        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            if (al == null) {
                al = new ArrayList<>();
            }
            CreditOrderDTO dto = new CreditOrderDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getString(5));
            al.add(dto);
        }

        connection.close();

        return al;

    }

    @Override
    public Date readLastDayEnd() throws ClassNotFoundException, SQLException {

        Date lastDayEndDate = null;

        Connection connection = this.getConnection();

        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM day_end ORDER BY date DESC LIMIT 1;");

        if (rst.next()) {
            lastDayEndDate = rst.getDate(1);
        }

        connection.close();

        return lastDayEndDate;

    }

    @Override
    public boolean hasDayEndDone(Date date) throws ClassNotFoundException, SQLException {

        boolean hasDayEndDone;

        Connection connection = this.getConnection();

        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM day_end WHERE date = ?;");
        pstm.setDate(1, new java.sql.Date(date.getTime()));

        hasDayEndDone = pstm.executeQuery().next();

//        if (hasDayEndDone) {
//            Statement stm = connection.createStatement();
//            ResultSet rst = stm.executeQuery("SELECT COUNT(*) FROM (SELECT DISTINCT * FROM wine_stores.day_end GROUP BY date) AS Alias;");
//            if (rst.next() && rst.getInt(1) > 1) {
//                hasDayEndDone = true;
//            }else{
//                hasDayEndDone = false;
//            }
//        }

        connection.close();

        return hasDayEndDone;

    }

//    @Override
//    public ArrayList<Date> readOrderDates(Date from, String toDate) throws ClassNotFoundException, SQLException {
//        
//        ArrayList<Date> al = null;
//        
//        Connection connection = this.getConnection();
//        
//        String sql = "SELECT order_date FROM custom_order WHERE order_date > ? AND  order_date <= ? GROUP BY order_date;";
//        PreparedStatement pstm = connection.prepareStatement(sql);
//        pstm.setTimestamp(1, new java.sql.Timestamp(from.getTime()));
//        pstm.setTimestamp(2, java.sql.Timestamp.valueOf(toDate));
//        ResultSet rst = pstm.executeQuery();
//        while(rst.next()){
//            if (al == null){
//                al = new ArrayList<>();
//            }
//            al.add(rst.getTimestamp(1));
//        }
//        
//        connection.close();
//        
//        return al;
//    }    
//
//    @Override
//    public ArrayList<CustomOrderDTO> readOrdersByDate(Date date) throws ClassNotFoundException, SQLException {
//
//        ArrayList<CustomOrderDTO> al = null;
//        
//        Connection connection = this.getConnection();
//        
//        String sql = "SELECT * FROM custom_order WHERE order_date = ?;";
//        PreparedStatement pstm = connection.prepareStatement(sql);
//        pstm.setTimestamp(1, new java.sql.Timestamp(date.getTime()));
//
//        ResultSet rst = pstm.executeQuery();
//        
//        while(rst.next()){
//            if (al == null){
//                al = new ArrayList<>();
//            }
//            CustomOrderDTO dto = new CustomOrderDTO(rst.getString(1),
//                    rst.getString(2),
//                    rst.getString(3),
//                    rst.getDouble(4),
//                    rst.getBigDecimal(6),
//                    rst.getString(5));
//            al.add(dto);
//        }
//        
//        connection.close();
//        
//        return al;        
//        
//    }
//
//    @Override
//    public ArrayList<OrderItemDetailsDTO> readOrderItemDetails(int orderId) throws ClassNotFoundException, SQLException {
//
//        ArrayList<OrderItemDetailsDTO> al = null;
//        
//        Connection connection = this.getConnection();
//        
//        String sql = "SELECT * FROM order_item_details WHERE order_id=?;";
//        PreparedStatement pstm = connection.prepareStatement(sql);
//        pstm.setInt(1, orderId);
//
//        ResultSet rst = pstm.executeQuery();
//        
//        while(rst.next()){
//            if (al == null){
//                al = new ArrayList<>();
//            }
//            OrderItemDetailsDTO dto = new OrderItemDetailsDTO(
//                    rst.getString(1),
//                    rst.getString(2),
//                    rst.getString(3),
//                    rst.getInt(4),
//                    rst.getDouble(5));
//            al.add(dto);
//        }
//        
//        connection.close();
//        
//        return al;          
//        
//    }
    @Override
    public DayEndDTO readDayEnd(Date date, String itemCode) throws ClassNotFoundException, SQLException {

        DayEndDTO dto = null;

        Connection connection = this.getConnection();

        String sql = "SELECT * FROM wine_stores.day_end WHERE date=? AND item_code=?;";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setDate(1, new java.sql.Date(date.getTime()));
        pstm.setString(2, itemCode);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            dto = new DayEndDTO(rst.getDate(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getInt(4),
                    rst.getInt(5),
                    rst.getBigDecimal(6),
                    rst.getBigDecimal(7));
        }

        connection.close();

        return dto;

    }

    @Override
    public int readGRNQty(Date date, String itemCode) throws ClassNotFoundException, SQLException {

        int qty = 0;

        Connection connection = this.getConnection();

        String sql = "SELECT item_batch_grn_detail.batch_qty FROM grn INNER JOIN batch ON grn.grn_id = batch.grn_id INNER JOIN item_batch_grn_detail ON item_batch_grn_detail.batch_id = batch.batch_id INNER JOIN main_sub_item ON main_sub_item.main_sub_item_id = item_batch_grn_detail.main_sub_item_id WHERE grn_date = ? AND main_sub_item.item_code = ?;";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setDate(1, new java.sql.Date(date.getTime()));
        pstm.setString(2, itemCode);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            qty = rst.getInt(1);
        }

        connection.close();

        return qty;

    }

    @Override
    public ArrayList<ItemDetailsDTO> readAllItemDetails() throws ClassNotFoundException, SQLException {

        ArrayList<ItemDetailsDTO> al = null;
        Connection connection = this.getConnection();

        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM item_details");

        while (rst.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            ItemDetailsDTO dto = new ItemDetailsDTO(rst.getString(1),
                    rst.getInt(2),
                    rst.getDouble(3),
                    rst.getDouble(4));

            al.add(dto);

        }

        connection.close();
        return al;

    }

    @Override
    public int readSalesQty(Date date, String itemCode) throws ClassNotFoundException, SQLException {

        int qty = 0;

        Connection connection = this.getConnection();

        String sql = "SELECT qty FROM custom_order INNER JOIN order_item_details ON custom_order.order_id = order_item_details.order_id WHERE order_date LIKE ? AND item_code=?;";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, (new java.sql.Date(date.getTime())).toString() + "%");
        pstm.setString(2, itemCode);

        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            qty = rst.getInt(1);
        }

        connection.close();

        return qty;

    }

}
