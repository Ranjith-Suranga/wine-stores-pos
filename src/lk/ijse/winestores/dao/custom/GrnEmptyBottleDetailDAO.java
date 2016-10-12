/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.dao.dto.GrnEmptyBottleDetailDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface GrnEmptyBottleDetailDAO extends SuperDAO {

    // ID Generator
    public static final String GRN_EMPTY_BOTTLE_CHECK_EXISTENCY_BY_ID = "SELECT * FROM `grn_empty_bottle_detail` WHERE id=? ;";
    public static final String GRN_EMPTY_BOTTLE_READ_LAST_ID_SQL = "SELECT * FROM `grn_empty_bottle_detail` ORDER BY id DESC LIMIT 1;";

    public ArrayList<GrnEmptyBottleDetailDTO> readEmptyBottleDetails(String grnId) throws ClassNotFoundException, SQLException;
}
