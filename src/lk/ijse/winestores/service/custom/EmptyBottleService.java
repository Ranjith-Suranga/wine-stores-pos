/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom;

import java.sql.SQLException;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.service.SuperService;

/**
 *
 * @author Ranjith Suranga
 */
public interface EmptyBottleService extends SuperService {

    public boolean saveEmptyBottle(EmptyBottleDTO emptyBottleDTO, String oldEmptyBottleType) throws ClassNotFoundException, SQLException;

    public boolean deleteEmptyBottle(String emptyBottleType) throws ClassNotFoundException, SQLException;

}
