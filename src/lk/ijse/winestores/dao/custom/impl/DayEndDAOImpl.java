/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import lk.ijse.winestores.dao.custom.DayEndDAO;
import lk.ijse.winestores.dao.dto.DayEndDTO;

/**
 *
 * @author Ranjith Suranga
 */
public class DayEndDAOImpl implements DayEndDAO {

    // Dependencies
    private Connection connection;

    // Dependency Injection
    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
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
    public boolean finishInitialStockTaking(Date date) throws SQLException {
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM item_details");
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO day_end VALUES (?,?,?,?,?,?,?)");
        int affectedRows = 0;
        while (rst.next()) {
            pstm.setDate(1, date);
            pstm.setString(2, rst.getString(1));
            pstm.setInt(3, rst.getInt(2));
            pstm.setInt(4, 0);
            pstm.setInt(5, 0);
            pstm.setBigDecimal(6, rst.getBigDecimal(3));
            pstm.setBigDecimal(7, rst.getBigDecimal(4));
            affectedRows = pstm.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }
        }
        return (affectedRows != 0);
    }

    @Override
    public boolean writeInitialStockTakingStatus(boolean status) throws IOException {
        File file = new File("settings/db_settings.txt");
        FileWriter writer = new FileWriter(file, true);
        BufferedWriter bfWriter = new BufferedWriter(writer);
        bfWriter.newLine();
        bfWriter.write(String.valueOf(status));
        bfWriter.flush();
        return true;
    }

    @Override
    public boolean create(DayEndDTO dayEnd) throws SQLException  {

        PreparedStatement pstm = connection.prepareStatement("INSERT INTO day_end VALUES (?,?,?,?,?,?,?)");

        pstm.setDate(1, new Date(dayEnd.getDate().getTime()));
        pstm.setString(2, dayEnd.getItemCode());
        pstm.setInt(3, dayEnd.getOpeningQty());
        pstm.setInt(4, dayEnd.getGrnQty());
        pstm.setInt(5, dayEnd.getSalesQty());
        pstm.setBigDecimal(6, dayEnd.getSellingPrice());
        pstm.setBigDecimal(7, dayEnd.getBuyingPrice());
        
        int affectedRows = pstm.executeUpdate();
        
        return (affectedRows > 0);

    }

}
