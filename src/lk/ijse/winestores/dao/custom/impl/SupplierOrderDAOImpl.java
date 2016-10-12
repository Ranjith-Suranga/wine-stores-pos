/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.custom.SupplierOrderDAO;
import lk.ijse.winestores.dao.custom.SupplierOrderDetailsDAO;
import lk.ijse.winestores.dao.dto.SupplierOrderDTO;
import lk.ijse.winestores.dao.dto.SupplierOrderDetailDTO;

/**
 *
 * @author Ranjith Suranga
 */
public class SupplierOrderDAOImpl implements SupplierOrderDAO {

    // Dependencies
    private Connection connection;
    private SupplierOrderDetailsDAO supplierOrderDetailsDAO;

    // Dependecy Injection
    public SupplierOrderDAOImpl() {
        supplierOrderDetailsDAO = (SupplierOrderDetailsDAO) DAOFactory.getInstance().getDAO(DAOType.SUPPLIER_ORDER_DETAILS);
    }

    // Dependecy Injection
    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
        supplierOrderDetailsDAO.setConnection(connection);
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
    public ArrayList<SupplierOrderDTO> readAllSupplierOrders() throws ClassNotFoundException, SQLException {
        Statement stm = connection.createStatement();
        ResultSet rstOrders = stm.executeQuery("SELECT * FROM supplier_order ORDER BY order_date DESC");
        ArrayList<SupplierOrderDTO> al = null;
        while (rstOrders.next()) {
            if (al == null) {
                al = new ArrayList<>();
            }
            SupplierOrderDTO dto = new SupplierOrderDTO(
                    rstOrders.getInt(1),
                    rstOrders.getString(2),
                    rstOrders.getDate(3),
                    rstOrders.getString(4),
                    rstOrders.getString(5)
            );
            al.add(dto);
        }
        return al;
    }

    @Override
    public ArrayList<SupplierOrderDTO> readSupplierOrdersByOrderId(int orderId) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM supplier_order WHERE order_id LIKE ? ORDER BY order_date DESC");
        pstm.setString(1, orderId + "%");

        ResultSet rstOrders = pstm.executeQuery();
        ArrayList<SupplierOrderDTO> al = null;

        while (rstOrders.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            SupplierOrderDTO dto = new SupplierOrderDTO(
                    rstOrders.getInt(1),
                    rstOrders.getString(2),
                    rstOrders.getDate(3),
                    rstOrders.getString(4),
                    rstOrders.getString(5)
            );
            al.add(dto);
        }
        return al;
    }

    @Override
    public ArrayList<SupplierOrderDTO> readSupplierOrdersByOrderDate(String orderDate) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM supplier_order WHERE order_date LIKE ? ORDER BY order_date DESC");
        pstm.setString(1, orderDate + "%");

        ResultSet rstOrders = pstm.executeQuery();
        ArrayList<SupplierOrderDTO> al = null;

        while (rstOrders.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            SupplierOrderDTO dto = new SupplierOrderDTO(
                    rstOrders.getInt(1),
                    rstOrders.getString(2),
                    rstOrders.getDate(3),
                    rstOrders.getString(4),
                    rstOrders.getString(5)
            );
            al.add(dto);
        }
        return al;

    }

    @Override
    public ArrayList<SupplierOrderDTO> readSupplierOrdersBySupplierId(String supplierId) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM supplier_order WHERE supplier_id=? ORDER BY order_date DESC");
        pstm.setString(1, supplierId);

        ResultSet rstOrders = pstm.executeQuery();
        ArrayList<SupplierOrderDTO> al = null;

        while (rstOrders.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            SupplierOrderDTO dto = new SupplierOrderDTO(
                    rstOrders.getInt(1),
                    rstOrders.getString(2),
                    rstOrders.getDate(3),
                    rstOrders.getString(4),
                    rstOrders.getString(5)
            );
            al.add(dto);
        }
        return al;
    }

    @Override
    public ArrayList<SupplierOrderDTO> readSupplierOrdersBySupplierName(String supplierName) throws ClassNotFoundException, SQLException {
        PreparedStatement pstm = connection.prepareStatement("SELECT wine_stores.supplier_order.* FROM wine_stores.supplier_order INNER JOIN supplier ON supplier_order.supplier_id = supplier.supplier_id WHERE supplier.name LIKE ? ORDER BY order_date DESC");
        pstm.setString(1, supplierName + "%");

        ResultSet rstOrders = pstm.executeQuery();
        ArrayList<SupplierOrderDTO> al = null;

        while (rstOrders.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            SupplierOrderDTO dto = new SupplierOrderDTO(
                    rstOrders.getInt(1),
                    rstOrders.getString(2),
                    rstOrders.getDate(3),
                    rstOrders.getString(4),
                    rstOrders.getString(5)
            );
            al.add(dto);
        }
        return al;
    }

    @Override
    public boolean deleteSupplierOrder(int orderId) throws ClassNotFoundException, SQLException {
        connection.setAutoCommit(false);

        boolean success = supplierOrderDetailsDAO.deleteSupplierOrderDetails(orderId);

        if (!success) {
            connection.setAutoCommit(true);
            return success;
        }

        PreparedStatement pstm = connection.prepareStatement("DELETE FROM supplier_order WHERE order_id=?");
        pstm.setInt(1, orderId);
        int affectedRows = pstm.executeUpdate();

        if (affectedRows != 0) {
            connection.commit();
        } else {
            connection.rollback();
        }

        connection.setAutoCommit(true);
        return (affectedRows != 0);
    }

    @Override
    public int readLastSupplierOrderId() throws ClassNotFoundException, SQLException {
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT order_id FROM wine_stores.supplier_order ORDER BY order_id DESC LIMIT 1");
        return (rst.next()) ? rst.getInt(1) : -1;
    }

    @Override
    public boolean createSupplierOrder(SupplierOrderDTO supplierOrderDTO, ArrayList<SupplierOrderDetailDTO> supplierOrderDetails) throws ClassNotFoundException, SQLException {

        connection.setAutoCommit(false);

        PreparedStatement pstm = connection.prepareStatement("INSERT INTO supplier_order VALUES (?,?,?,?,?)");
        pstm.setInt(1, supplierOrderDTO.getOrderId());
        pstm.setString(2, supplierOrderDTO.getSupplierId());
        pstm.setDate(3, new Date(supplierOrderDTO.getOrderDate().getTime()));
        pstm.setString(4, supplierOrderDTO.getMadeBy());
        pstm.setString(5, supplierOrderDTO.getOrderTotal());
        int affectedRows = pstm.executeUpdate();

        boolean success = (affectedRows != 0);

        if (success) {

            for (SupplierOrderDetailDTO supplierOrderDetail : supplierOrderDetails) {

                success = supplierOrderDetailsDAO.createSupplierOrderDetail(supplierOrderDetail);

                if (!success) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }

            connection.commit();

        }

        connection.setAutoCommit(true);
        return success;
    }

}
