/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom.impl;

import java.sql.Connection;
import java.sql.SQLException;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.EmptyBottleDAO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.service.custom.EmptyBottleService;

/**
 *
 * @author Ranjith Suranga
 */
public class EmptyBottleServiceImpl implements EmptyBottleService{
    
    // Dependencies
    private EmptyBottleDAO emptyBottleDAO;
    
    // Dependecy Injection

    public EmptyBottleServiceImpl() {
        emptyBottleDAO = (EmptyBottleDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.EMPTY_BOTTLE);
    }

    @Override
    public boolean saveEmptyBottle(EmptyBottleDTO emptyBottleDTO, String oldEmptyBottleType) throws ClassNotFoundException, SQLException {
        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        emptyBottleDAO.setConnection(connection);
        
        boolean success;
        
        // Determines whether we have to do update or new one
        if (oldEmptyBottleType != null){
            // If so, let's update the existing one
            success = emptyBottleDAO.updateEmptyBottle(emptyBottleDTO, oldEmptyBottleType);
        }else{
            // Otherwise, let's create a new empty bottle record
            success = emptyBottleDAO.createEmptyBottle(emptyBottleDTO);
        }
        
        connection.close();
        return success;
    }

    @Override
    public boolean deleteEmptyBottle(String emptyBottleType) throws ClassNotFoundException, SQLException {
        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        emptyBottleDAO.setConnection(connection);
        boolean success = emptyBottleDAO.deleteEmptyBottle(emptyBottleType);
        connection.close();
        return success;
    }
    
}
