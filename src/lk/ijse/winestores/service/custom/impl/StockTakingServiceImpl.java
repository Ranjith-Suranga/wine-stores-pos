/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.DayEndDAO;
import lk.ijse.winestores.exceptions.WriteFailedException;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.service.custom.StockTakingService;

/**
 *
 * @author Ranjith Suranga
 */
public class StockTakingServiceImpl implements StockTakingService{
    
    // Dependencies
    private DayEndDAO daoDayEnd;
    
    // Dependency Injection
    public StockTakingServiceImpl() {
        daoDayEnd = (DayEndDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.DAY_END);    }
    
    @Override
    public boolean finishInitialStockTaking() throws ClassNotFoundException, SQLException, IOException, WriteFailedException {
        
        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        daoDayEnd.setConnection(connection);
        
        if (daoDayEnd.finishInitialStockTaking(new Date(new java.util.Date().getTime() - (24*60*60*1000)))){
            if (daoDayEnd.writeInitialStockTakingStatus(true)){
                connection.close();
                return true;
            }else{
                connection.close();
                throw new WriteFailedException("Failed to write the status");
            }
        }else{
            connection.close();
            return false;
        }
    }    
    
}
