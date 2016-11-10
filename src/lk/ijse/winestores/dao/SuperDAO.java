/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao;

import java.sql.SQLException;

/**
 *
 * @author pradin
 */
public interface SuperDAO {

    public enum DAOType {
        MAJOR_CATEGORY, ID_POOL, SUB_CATEGORY, MAIN_SUB_CATEGORY, ITEM, MAIN_SUB_ITEM, SUPPLIER,
        GRN, EMPTY_BOTTLE, GRN_EMPTY_BOTTLE_DETAIL, BATCH, GRN_DETAIL, ITEM_DETAILS, CUSTOM, ITEM_BATCH_GRN_DETAIL,
        PAYMENT, CUSTOM_ORDER, CHEQUE_DETAILS, ORDER_ITEM_DETAILS, ORDER_EMPTY_BOTTLE_DETAILS,
        CUSTOMER, CREDIT_ORDER, CREDIT_ORDER_ITEM_DETAILS, CREDIT_ORDER_EMPTY_BOTTLE_DETAILS,
        SUPPLIER_ORDER, QUERY, SUPPLIER_ORDER_DETAILS, DAY_END
    }

    public static final String READALL_SQL = "SELECT * FROM ";

    public boolean isExists(String queryId) throws ClassNotFoundException, SQLException;

    public String readLastId() throws ClassNotFoundException, SQLException;
;

}
