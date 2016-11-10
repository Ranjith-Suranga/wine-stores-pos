/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom.impl;

import java.io.IOException;
import java.sql.SQLException;
import lk.ijse.winestores.controller.custom.SystemSettingsController;
import lk.ijse.winestores.exceptions.WriteFailedException;
import lk.ijse.winestores.service.ServiceFactory;
import lk.ijse.winestores.service.SuperService;
import lk.ijse.winestores.service.custom.StockTakingService;

/**
 *
 * @author Ranjith Suranga
 */
public class SystemSettingsControllerImpl implements SystemSettingsController {

    // Dependencies
    private StockTakingService srvStockTaking;

    // Dependency Injection
    public SystemSettingsControllerImpl() {
        srvStockTaking = (StockTakingService) ServiceFactory.getInstance().getService(SuperService.ServiceType.STOCK_TAKING);
    }

    @Override
    public boolean finishInitialStockTaking() throws ClassNotFoundException, SQLException, IOException, WriteFailedException {
        return srvStockTaking.finishInitialStockTaking();
    }

}
