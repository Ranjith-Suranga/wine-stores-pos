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
public interface GrnDetailDAO extends SuperDAO {
    
    // ID Generator
    public static final String GRN_DETAIL_CHECK_EXISTENCY_BY_ID = "SELECT * FROM grn_detail WHERE grn_detail_id=? ;";
    public static final String GRN_DETAIL_READ_LAST_ID_SQL = "SELECT * FROM grn_detail ORDER BY grn_detail_id DESC LIMIT 1;";
}
