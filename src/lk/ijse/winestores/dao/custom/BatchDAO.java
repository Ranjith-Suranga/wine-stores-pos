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
public interface BatchDAO extends SuperDAO{

    // ID Generator
    public static final String BATCH_CHECK_EXISTENCY_BY_ID = "SELECT * FROM batch WHERE batch_id=? ;";
    public static final String BATCH_READ_LAST_ID_SQL = "SELECT * FROM batch ORDER BY batch_id DESC LIMIT 1;";
    
}
