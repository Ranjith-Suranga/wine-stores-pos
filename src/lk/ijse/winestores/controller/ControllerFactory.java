/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller;

import lk.ijse.winestores.controller.custom.impl.CustomerControllerImpl;
import lk.ijse.winestores.controller.custom.impl.DayEndReportsControllerImpl;
import lk.ijse.winestores.controller.custom.impl.EmptyBottleControllerImpl;
import lk.ijse.winestores.controller.custom.impl.GRNControllerImpl;
import lk.ijse.winestores.controller.custom.impl.ItemControllerImpl;
import lk.ijse.winestores.controller.custom.impl.MajorCategoryControllerImpl;
import lk.ijse.winestores.controller.custom.impl.NewItemControllerImpl;
import lk.ijse.winestores.controller.custom.impl.QueryControllerImpl;
import lk.ijse.winestores.controller.custom.impl.SalesControllerImpl;
import lk.ijse.winestores.controller.custom.impl.SubCategoryControllerImpl;
import lk.ijse.winestores.controller.custom.impl.SupplierControllerImpl;
import lk.ijse.winestores.controller.custom.impl.SupplierOrderControllerImpl;
import lk.ijse.winestores.controller.custom.impl.SystemSettingsControllerImpl;

/**
 *
 * @author pradin
 */
public class ControllerFactory {

    private static ControllerFactory controllerFactory;

    private ControllerFactory() {
    }

    public static ControllerFactory getInstance() {
        return (null != controllerFactory) ? controllerFactory : (controllerFactory = new ControllerFactory());
    }

    public SuperController getController(SuperController.ControllerType controllerType) {

        switch (controllerType) {
            case MAJOR_CATEGORY:
                return new MajorCategoryControllerImpl();
            case SUB_CATEOGRY:
                return new SubCategoryControllerImpl();
            case ITEM:
                return new ItemControllerImpl();
            case SUPPLIER:
                return new SupplierControllerImpl();
            case GRN:
                return new GRNControllerImpl();
            case SALES:
                return new SalesControllerImpl();
            case NEW_ITEM:
                return new NewItemControllerImpl();
            case SUPPLIER_ORDER:
                return new SupplierOrderControllerImpl();
            case QUERY:
                return new QueryControllerImpl();
            case EMPTY_BOTTLE:
                return new EmptyBottleControllerImpl();
            case CUSTOMER:
                return new CustomerControllerImpl();
            case SYSTEM_SETTINGS:
                return new SystemSettingsControllerImpl();
            case DAYEND_REPORTS:
                return new DayEndReportsControllerImpl();
            default:
                return null;
        }
    }

}
