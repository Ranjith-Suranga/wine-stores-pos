/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import lk.ijse.winestores.dao.SuperDAO;

/**
 *
 * @author Ranjith Suranga
 */
public interface ItemBatchGrnDetailDAO extends SuperDAO{
    
    // ID Generator
    public static final String ITEM_BATCH_GRN_DETAIL_CHECK_EXISTENCY_BY_ID = "SELECT * FROM item_batch_grn_detail WHERE item_batch_grn_detail_id=? ;";
    public static final String ITEM_BATCH_GRN_DETAIL_READ_LAST_ID_SQL = "SELECT * FROM item_batch_grn_detail ORDER BY item_batch_grn_detail_id DESC LIMIT 1;";    
    
}
