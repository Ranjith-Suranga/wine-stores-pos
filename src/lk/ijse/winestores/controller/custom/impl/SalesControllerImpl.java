/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.custom.SalesController;
import lk.ijse.winestores.dao.dto.ChequeDetailsDTO;
import lk.ijse.winestores.dao.dto.CreditOrderDTO;
import lk.ijse.winestores.dao.dto.CreditOrderEmptyBottleDetailsDTO;
import lk.ijse.winestores.dao.dto.CreditOrderItemDetailsDTO;
import lk.ijse.winestores.dao.dto.CustomOrderDTO;
import lk.ijse.winestores.dao.dto.CustomerDTO;
import lk.ijse.winestores.dao.dto.OrderEmptyBottleDetailsDTO;
import lk.ijse.winestores.dao.dto.OrderItemDetailsDTO;
import lk.ijse.winestores.service.ServiceFactory;
import lk.ijse.winestores.service.SuperService;
import lk.ijse.winestores.service.custom.SalesService;

/**
 *
 * @author Ranjith Suranga
 */
public class SalesControllerImpl implements SalesController{
    
    private SalesService salesService;

    public SalesControllerImpl() {
        salesService = (SalesService) ServiceFactory.getInstance().getService(SuperService.ServiceType.SALES);
    }

    @Override
    public boolean saveCashSale(CustomOrderDTO order, ArrayList<OrderItemDetailsDTO> orderItemDetails, ArrayList<OrderEmptyBottleDetailsDTO> orderEmptyBottleDetails, ChequeDetailsDTO chequeDetails) throws ClassNotFoundException, SQLException {
        return salesService.saveCashSale(order, orderItemDetails, orderEmptyBottleDetails, chequeDetails);
    }

    @Override
    public boolean saveCreditSale(CreditOrderDTO order, ArrayList<CreditOrderItemDetailsDTO> orderItemDetails, ArrayList<CreditOrderEmptyBottleDetailsDTO> orderEmptyBottleDetails, CustomerDTO customerDetails) throws ClassNotFoundException, SQLException {
        return salesService.saveCreditSale(order, orderItemDetails, orderEmptyBottleDetails, customerDetails);
    }
    
}
