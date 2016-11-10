/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.exceptions.WriteFailedException;

/**
 *
 * @author Ranjith Suranga
 */
public interface SystemSettingsController extends SuperController{
    
    public boolean finishInitialStockTaking()throws ClassNotFoundException, SQLException, IOException, WriteFailedException;    

}
