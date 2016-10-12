/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom;

import java.sql.SQLException;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface EmptyBottleController extends SuperController {

    public boolean saveEmptyBottle(EmptyBottleDTO emptyBottleDTO, String oldEmptyBottleType) throws ClassNotFoundException, SQLException;

    public boolean deleteEmptyBottle(String emptyBottleType) throws ClassNotFoundException, SQLException;

}
