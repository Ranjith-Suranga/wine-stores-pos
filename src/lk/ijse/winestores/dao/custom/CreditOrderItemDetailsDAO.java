/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.SQLException;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.CreditOrderItemDetailsDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface CreditOrderItemDetailsDAO extends SuperDAO{

    public boolean create(CreditOrderItemDetailsDTO dto)throws ClassNotFoundException, SQLException;
    
}
