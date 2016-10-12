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
import lk.ijse.winestores.dao.custom.EmptyBottleDAO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.resource.db.DatabaseResourceConnection;

/**
 *
 * @author Ranjith Suranga
 */
public class EmptyBottleDAOImpl implements EmptyBottleDAO {
    
    private Connection connection;
    
    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }    

    @Override
    public ArrayList<EmptyBottleDTO> readAllBottleTypes() throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(READ_ALL_BOTTLE_TYPES_SQL);  
        ResultSet rst = pstm.executeQuery();
        
        ArrayList<EmptyBottleDTO> al = null;
        
        while(rst.next()){
            
            if (al == null){
                al = new ArrayList<>();
            }
            
            EmptyBottleDTO dto = new EmptyBottleDTO(rst.getString(1), Double.parseDouble( rst.getString(2)));
            al.add(dto);
        }
        
        con.getConnection().close();
        return al;
    }

    /*
    ======================================================================
        ID Generator Methods (No need to generate an ID for this table)
    ======================================================================
     */
    @Override
    public boolean isExists(String queryId) throws ClassNotFoundException, SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String readLastId() throws ClassNotFoundException, SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean isEmptyBottleExits(String emptyBottleType) throws ClassNotFoundException, SQLException {
        
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM empty_bottle WHERE bottle_type=?");
        pstm.setString(1, emptyBottleType);
        ResultSet rst = pstm.executeQuery();
        return rst.next();
        
    }    

    @Override
    public boolean createEmptyBottle(EmptyBottleDTO emptyBottleDTO) throws ClassNotFoundException, SQLException {
        
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO empty_bottle VALUES(?,?)");
        pstm.setString(1, emptyBottleDTO.getBottleType());
        pstm.setDouble(2, emptyBottleDTO.getCost());
        int affectedRows = pstm.executeUpdate();
        return (affectedRows != 0);
        
    }

    @Override
    public boolean deleteEmptyBottle(String emptyBottleType) throws ClassNotFoundException, SQLException {

        PreparedStatement pstm = connection.prepareStatement("DELETE FROM empty_bottle WHERE bottle_type=?");
        pstm.setString(1, emptyBottleType);
        int affectedRows = pstm.executeUpdate();
        return (affectedRows != 0);        
        
    }

    @Override
    public boolean updateEmptyBottle(EmptyBottleDTO emptyBottleDTO, String emptyBottleType) throws ClassNotFoundException, SQLException {

        PreparedStatement pstm = connection.prepareStatement("UPDATE empty_bottle SET bottle_type=?, cost=? WHERE bottle_type=?");
        pstm.setString(1, emptyBottleDTO.getBottleType());
        pstm.setDouble(2, emptyBottleDTO.getCost());
        pstm.setString(3, emptyBottleType);
        int affectedRows = pstm.executeUpdate();
        return (affectedRows != 0);        
        
    }



}
