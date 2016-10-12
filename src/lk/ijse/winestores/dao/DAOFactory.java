/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao;

import lk.ijse.winestores.dao.custom.impl.BatchDAOImpl;
import lk.ijse.winestores.dao.custom.impl.ChequeDetailsDAOImpl;
import lk.ijse.winestores.dao.custom.impl.CreditOrderDAOImpl;
import lk.ijse.winestores.dao.custom.impl.CreditOrderEmptyBottleDetailsDAOImpl;
import lk.ijse.winestores.dao.custom.impl.CreditOrderItemDetailsDAOImpl;
import lk.ijse.winestores.dao.custom.impl.CustomDAOImpl;
import lk.ijse.winestores.dao.custom.impl.CustomOrderDAOImpl;
import lk.ijse.winestores.dao.custom.impl.CustomerDAOImpl;
import lk.ijse.winestores.dao.custom.impl.EmptyBottleDAOImpl;
import lk.ijse.winestores.dao.custom.impl.GrnDAOImpl;
import lk.ijse.winestores.dao.custom.impl.GrnDetailDAOImpl;
import lk.ijse.winestores.dao.custom.impl.GrnEmptyBottleDetailDAOImpl;
import lk.ijse.winestores.dao.custom.impl.IdPoolDAOImpl;
import lk.ijse.winestores.dao.custom.impl.ItemBatchGrnDetailDAOImpl;
import lk.ijse.winestores.dao.custom.impl.ItemDAOImpl;
import lk.ijse.winestores.dao.custom.impl.ItemDetailsDAOImpl;
import lk.ijse.winestores.dao.custom.impl.MainSubCategoryDAOImpl;
import lk.ijse.winestores.dao.custom.impl.MainSubItemDAOImpl;
import lk.ijse.winestores.dao.custom.impl.MajorCategoryDAOImpl;
import lk.ijse.winestores.dao.custom.impl.OrderEmptyBottleDetailsDAOImpl;
import lk.ijse.winestores.dao.custom.impl.OrderItemDetailsDAOImpl;
import lk.ijse.winestores.dao.custom.impl.PaymentDAOImpl;
import lk.ijse.winestores.dao.custom.impl.QueryDAOImpl;
import lk.ijse.winestores.dao.custom.impl.SubCategoryDAOImpl;
import lk.ijse.winestores.dao.custom.impl.SupplierDAOImpl;
import lk.ijse.winestores.dao.custom.impl.SupplierOrderDAOImpl;
import lk.ijse.winestores.dao.custom.impl.SupplierOrderDetailsDAOImpl;

/**
 *
 * @author pradin
 */
public class DAOFactory {

    private static DAOFactory dAOFactory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return (null != dAOFactory) ? dAOFactory : (dAOFactory = new DAOFactory());
    }

    public SuperDAO getDAO(SuperDAO.DAOType dAOType) {

        switch (dAOType) {
            case MAJOR_CATEGORY:
                return new MajorCategoryDAOImpl();
            case ID_POOL:
                return new IdPoolDAOImpl();
            case SUB_CATEGORY:
                return new SubCategoryDAOImpl();
            case MAIN_SUB_CATEGORY:
                return new MainSubCategoryDAOImpl();
            case ITEM:
                return new ItemDAOImpl();
            case MAIN_SUB_ITEM:
                return new MainSubItemDAOImpl();
            case SUPPLIER:
                return new SupplierDAOImpl();
            case GRN:
                return new GrnDAOImpl();
            case EMPTY_BOTTLE:
                return new EmptyBottleDAOImpl();
            case GRN_EMPTY_BOTTLE_DETAIL:
                return new GrnEmptyBottleDetailDAOImpl();
            case BATCH:
                return new BatchDAOImpl();
            case GRN_DETAIL:
                return new GrnDetailDAOImpl();
            case ITEM_DETAILS:
                return new ItemDetailsDAOImpl();
            case CUSTOM:
                return new CustomDAOImpl();
            case ITEM_BATCH_GRN_DETAIL:
                return new ItemBatchGrnDetailDAOImpl();
            case CHEQUE_DETAILS:
                return new ChequeDetailsDAOImpl();
            case CUSTOM_ORDER:
                return new CustomOrderDAOImpl();
            case ORDER_EMPTY_BOTTLE_DETAILS:
                return new OrderEmptyBottleDetailsDAOImpl();
            case ORDER_ITEM_DETAILS:
                return new OrderItemDetailsDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            case CREDIT_ORDER:
                return new CreditOrderDAOImpl();
            case CREDIT_ORDER_EMPTY_BOTTLE_DETAILS:
                return new CreditOrderEmptyBottleDetailsDAOImpl();
            case CREDIT_ORDER_ITEM_DETAILS:
                return new CreditOrderItemDetailsDAOImpl();
            case CUSTOMER:
                return new CustomerDAOImpl();
            case SUPPLIER_ORDER:
                return new SupplierOrderDAOImpl();
            case QUERY:
                return new QueryDAOImpl();
            case SUPPLIER_ORDER_DETAILS:
                return new SupplierOrderDetailsDAOImpl();
            default:
                return null;
        }

    }

}
