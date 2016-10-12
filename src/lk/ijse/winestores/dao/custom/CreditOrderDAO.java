/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.SQLException;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.CreditOrderDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface CreditOrderDAO extends SuperDAO{

    public String create(CreditOrderDTO dto)throws ClassNotFoundException, SQLException;
    
}
