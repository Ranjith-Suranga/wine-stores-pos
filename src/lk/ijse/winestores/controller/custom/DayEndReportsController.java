/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom;

import java.sql.SQLException;
import java.util.Date;
import lk.ijse.winestores.controller.SuperController;

/**
 *
 * @author Ranjith Suranga
 */
public interface DayEndReportsController extends SuperController{
    
    public boolean finalizeDayEnd(Date date) throws ClassNotFoundException, SQLException ;
    
}
