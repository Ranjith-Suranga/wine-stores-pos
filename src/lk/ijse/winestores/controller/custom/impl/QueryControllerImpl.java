/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.custom.QueryController;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;
import lk.ijse.winestores.dao.dto.SupplierOrderDTO;
import lk.ijse.winestores.service.ServiceFactory;
import lk.ijse.winestores.service.SuperService;
import lk.ijse.winestores.service.custom.QueryService;

/**
 *
 * @author Ranjith Suranga
 */
public class QueryControllerImpl implements QueryController{
    
    // Dependencies
    private QueryService queryService;

    // Dependecy Injection
    public QueryControllerImpl() {
        queryService = (QueryService) ServiceFactory.getInstance().getService(SuperService.ServiceType.QUERY);
    }
    
    @Override
    public SubCategoryDTO getSubCategoryByItemCode(String itemCode) throws ClassNotFoundException, SQLException {
        return queryService.getSubCategoryByItemCode(itemCode);
    }

    @Override
    public SupplierOrderDTO getSupplierOrder(int orderId) throws ClassNotFoundException, SQLException {
        return queryService.getSupplierOrder(orderId);
    }

    @Override
    public String getItemNameByItemCode(String itemCode) throws ClassNotFoundException, SQLException {
        return queryService.getItemNameByItemCode(itemCode);
    }

    @Override
    public ArrayList<EmptyBottleDTO> getAllEmptyBottles() throws ClassNotFoundException, SQLException {
        return queryService.getAllEmptyBottles();
    }
    
}
