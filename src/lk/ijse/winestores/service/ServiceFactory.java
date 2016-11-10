/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lk.ijse.winestores.service;

import lk.ijse.winestores.service.custom.DayEndService;
import lk.ijse.winestores.service.custom.impl.CustomerServiceImpl;
import lk.ijse.winestores.service.custom.impl.DayEndServiceImpl;
import lk.ijse.winestores.service.custom.impl.EmptyBottleServiceImpl;
import lk.ijse.winestores.service.custom.impl.GrnServiceImpl;
import lk.ijse.winestores.service.custom.impl.ItemServiceImpl;
import lk.ijse.winestores.service.custom.impl.MajorCategoryServiceImpl;
import lk.ijse.winestores.service.custom.impl.NewItemServiceImpl;
import lk.ijse.winestores.service.custom.impl.QueryServiceImpl;
import lk.ijse.winestores.service.custom.impl.SalesServiceImpl;
import lk.ijse.winestores.service.custom.impl.StockTakingServiceImpl;
import lk.ijse.winestores.service.custom.impl.SubCategoryServiceImpl;
import lk.ijse.winestores.service.custom.impl.SupplierOrderServiceImpl;
import lk.ijse.winestores.service.custom.impl.SupplierServiceImpl;

/**
 *
 * @author pradin
 */
public class ServiceFactory {
    
    private static ServiceFactory serviceFactory;

    private ServiceFactory() {
    }
    
    public static ServiceFactory getInstance(){
        return (null!=serviceFactory)?serviceFactory: (serviceFactory = new ServiceFactory());
    }
    
    public SuperService getService(SuperService.ServiceType serviceType){
        switch(serviceType){
            case MAJOR_CATEGORY:
                return new MajorCategoryServiceImpl();
            case SUB_CATEGORY:
                return new SubCategoryServiceImpl();
            case ITEM:
                return new ItemServiceImpl();
            case SUPPLIER:
                return new SupplierServiceImpl();
            case GRN:
                return new GrnServiceImpl();
            case SALES:
                return new SalesServiceImpl();
            case NEW_ITEM:
                return new NewItemServiceImpl();
            case SUPPLIER_ORDER:
                return new SupplierOrderServiceImpl();
            case QUERY:
                return new QueryServiceImpl();
            case EMPTY_BOTTLE:
                return new EmptyBottleServiceImpl();
            case CUSTOMER:
                return new CustomerServiceImpl();
            case STOCK_TAKING:
                return new StockTakingServiceImpl();
            case DAYEND:
                return new DayEndServiceImpl();
            default:
                return null;
        }
    }
    
}
