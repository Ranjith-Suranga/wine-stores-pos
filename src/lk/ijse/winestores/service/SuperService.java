/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service;

/**
 *
 * @author pradin
 */
public interface SuperService {

    public enum ServiceType {
        MAJOR_CATEGORY, SUB_CATEGORY, ITEM, SUPPLIER, GRN, SALES, NEW_ITEM, SUPPLIER_ORDER, QUERY,
        EMPTY_BOTTLE
    }

}
