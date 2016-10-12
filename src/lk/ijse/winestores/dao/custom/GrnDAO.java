/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.GrnDTO;
import lk.ijse.winestores.dao.dto.GrnDetailDTO;
import lk.ijse.winestores.dao.dto.GrnEmptyBottleDetailDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface GrnDAO extends SuperDAO {
    
    public enum QueryType{
        GRN_ID, GRN_DATE,SUPPLIER_NAME, INVOICE_NUMBER,INVOICE_DATE
    }

    // ID Generator
    public static final String GRN_CHECK_EXISTENCY_BY_ID = "SELECT * FROM grn WHERE grn_id=? ;";
    public static final String GRN_READ_LAST_ID_SQL = "SELECT * FROM grn ORDER BY grn_id DESC LIMIT 1;";
    
    public int createGrn(GrnDTO grnDto, ArrayList<GrnEmptyBottleDetailDTO> emptyBottleDetails, ArrayList<GrnDetailDTO> grnItemDetails)throws ClassNotFoundException, SQLException;
    public boolean readGrnStatus(String grnId)throws ClassNotFoundException, SQLException;
    
    public ArrayList<GrnDTO> readGrns(QueryType queryType, String queryWord)throws ClassNotFoundException, SQLException;
    
    public ArrayList<GrnDetailDTO> readItemDetails(String grnId, boolean grnAuthorizedStatus)throws ClassNotFoundException,SQLException;
    public int deleteGrn(String grnId, boolean grnAuthorizedStatus) throws ClassNotFoundException, SQLException;
    
    public int updateGrn(GrnDTO grnDto, ArrayList<GrnEmptyBottleDetailDTO> emptyBottleDetails, ArrayList<GrnDetailDTO> grnItemDetails, boolean grnAuthorizedStatus)throws ClassNotFoundException, SQLException;
    public int authorizeGrn(String grnId) throws ClassNotFoundException, SQLException;
}
