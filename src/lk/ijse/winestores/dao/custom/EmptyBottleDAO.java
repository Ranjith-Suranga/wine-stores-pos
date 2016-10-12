/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface EmptyBottleDAO extends SuperDAO {
    
    public static String READ_ALL_BOTTLE_TYPES_SQL = "SELECT * FROM `empty_bottle`";
    
    public ArrayList<EmptyBottleDTO> readAllBottleTypes()throws ClassNotFoundException, SQLException;
    
    public void setConnection(Connection connection);
    
    public boolean isEmptyBottleExits(String emptyBottleType)throws ClassNotFoundException, SQLException;
    
    public boolean createEmptyBottle(EmptyBottleDTO emptyBottleDTO) throws ClassNotFoundException, SQLException;
    
    public boolean updateEmptyBottle(EmptyBottleDTO emptyBottleDTO, String emptyBottleType) throws ClassNotFoundException, SQLException;
    
    public boolean deleteEmptyBottle(String emptyBottleType) throws ClassNotFoundException, SQLException;

}
