/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom;

import java.io.IOException;
import java.sql.SQLException;
import lk.ijse.winestores.exceptions.WriteFailedException;
import lk.ijse.winestores.service.SuperService;

/**
 *
 * @author Ranjith Suranga
 */
public interface StockTakingService extends SuperService{

    public boolean finishInitialStockTaking() throws ClassNotFoundException, SQLException, IOException, WriteFailedException;
    
}
