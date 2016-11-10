/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.DayEndDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface DayEndDAO extends SuperDAO {
    
    public void setConnection(Connection connection);
    
    public boolean finishInitialStockTaking(Date date)throws SQLException;
    
    public boolean writeInitialStockTakingStatus(boolean status) throws IOException ;
    
    public boolean create(DayEndDTO dayEnd) throws SQLException;
    
}
