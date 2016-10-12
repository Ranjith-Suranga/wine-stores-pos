/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import lk.ijse.winestores.dao.custom.CustomDAO;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.resource.db.DatabaseResourceConnection;

/**
 *
 * @author Ranjith Suranga
 */
public class CustomDAOImpl implements CustomDAO {

    @Override
    public boolean isExists(String queryId) throws ClassNotFoundException, SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String readLastId() throws ClassNotFoundException, SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ArrayList<CustomItemDetailsDTO> readItems(ItemQueryType queryType, String queryWord) throws ClassNotFoundException, SQLException {
        
        DatabaseResourceConnection dbConnection = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        Connection connection = dbConnection.getConnection();
        Statement stm = connection.createStatement();
        String sqlQuery = GET_ITEMS_SQL;
        
        switch (queryType){
            case BARCODE:
                sqlQuery+= "main_sub_item.barcode ='";
                break;
            case ITEM_CODE:
                sqlQuery+= "item_details.item_code LIKE'";
                break;      
            case ITEM_NAME:
                sqlQuery+= "item.item_name LIKE'";
                break;
            case SELLING_PRICE:
                sqlQuery+= "item_details.selling_price LIKE'";
                break;                
        }
        
        if (queryType != ItemQueryType.BARCODE){
            sqlQuery += queryWord + "%'";
        }else{
            sqlQuery += queryWord + "'";
        }
        ResultSet rst = stm.executeQuery(sqlQuery);
        
        ArrayList<CustomItemDetailsDTO> al = null;
        
        while (rst.next()){
            
            if (al == null){
                al = new ArrayList<>();
            }
            
            CustomItemDetailsDTO dto = new CustomItemDetailsDTO(rst.getString(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getDouble(5));
            al.add(dto);
        }
        
        return al;
    }    

}
