/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom.impl;

import java.sql.SQLException;
import lk.ijse.winestores.controller.custom.EmptyBottleController;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.service.ServiceFactory;
import lk.ijse.winestores.service.SuperService;
import lk.ijse.winestores.service.custom.EmptyBottleService;

/**
 *
 * @author Ranjith Suranga
 */
public class EmptyBottleControllerImpl implements EmptyBottleController {

    // Dependencies
    private EmptyBottleService emptyBottleService;

    // Dependency Injection
    public EmptyBottleControllerImpl() {
        emptyBottleService = (EmptyBottleService) ServiceFactory.getInstance().getService(SuperService.ServiceType.EMPTY_BOTTLE);
    }

    @Override
    public boolean saveEmptyBottle(EmptyBottleDTO emptyBottleDTO, String oldEmptyBottleType) throws ClassNotFoundException, SQLException {
        return emptyBottleService.saveEmptyBottle(emptyBottleDTO, oldEmptyBottleType);
    }

    @Override
    public boolean deleteEmptyBottle(String emptyBottleType) throws ClassNotFoundException, SQLException {
        return emptyBottleService.deleteEmptyBottle(emptyBottleType);
    }

}
