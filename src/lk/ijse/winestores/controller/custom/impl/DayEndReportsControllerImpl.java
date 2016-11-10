/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom.impl;

import java.sql.SQLException;
import java.util.Date;
import lk.ijse.winestores.controller.custom.DayEndReportsController;
import lk.ijse.winestores.service.ServiceFactory;
import lk.ijse.winestores.service.SuperService;
import lk.ijse.winestores.service.custom.DayEndService;

/**
 *
 * @author Ranjith Suranga
 */
public class DayEndReportsControllerImpl implements DayEndReportsController{
    
    // Dependencies
    private DayEndService srvDayEnd;
    
    // Dependency Inejction
    public DayEndReportsControllerImpl() {
        srvDayEnd = (DayEndService) ServiceFactory.getInstance().getService(SuperService.ServiceType.DAYEND);
    }

    @Override
    public boolean finalizeDayEnd(Date date) throws ClassNotFoundException, SQLException  {
        return srvDayEnd.finalizeDayEnd(date);
    }
    
}
