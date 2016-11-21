/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.dao.dto.ChequeDetailsDTO;
import lk.ijse.winestores.dao.dto.CreditOrderDTO;
import lk.ijse.winestores.dao.dto.CreditOrderItemDetailsDTO;
import lk.ijse.winestores.dao.dto.CustomOrderDTO;
import lk.ijse.winestores.dao.dto.OrderEmptyBottleDetailsDTO;
import lk.ijse.winestores.dao.dto.OrderItemDetailsDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface SalesController extends SuperController{
    
    public boolean saveCashSale(CustomOrderDTO order, ArrayList<OrderItemDetailsDTO> orderItemDetails, ArrayList<OrderEmptyBottleDetailsDTO> orderEmptyBottleDetails, ChequeDetailsDTO chequeDetails) throws ClassNotFoundException, SQLException;
    
    public boolean saveCreditSale(CreditOrderDTO order, ArrayList<CreditOrderItemDetailsDTO> orderItemDetails) throws ClassNotFoundException, SQLException;

    public boolean updateCashSale(CustomOrderDTO customOrder, ArrayList<OrderItemDetailsDTO> orderItemDetails, ArrayList<OrderEmptyBottleDetailsDTO> orderEmptyBottleDetails, ChequeDetailsDTO chequeDetails)throws ClassNotFoundException, SQLException;
    
}
