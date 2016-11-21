/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.SQLException;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.ChequeDetailsDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface ChequeDetailsDAO extends SuperDAO{
    
    public boolean create(ChequeDetailsDTO dto) throws ClassNotFoundException, SQLException;

    public boolean deleteByOrderId(int orderId) throws ClassNotFoundException, SQLException;
    
}
