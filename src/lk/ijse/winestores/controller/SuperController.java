/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lk.ijse.winestores.controller;

/**
 *
 * @author pradin
 */
public interface SuperController {
    
    public enum ControllerType{
        MAJOR_CATEGORY, SUB_CATEOGRY, ITEM, SUPPLIER, GRN, SALES, NEW_ITEM, SUPPLIER_ORDER, QUERY, EMPTY_BOTTLE,
        CUSTOMER, SYSTEM_SETTINGS, DAYEND_REPORTS
    }
    
}
