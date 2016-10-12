/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.custom.GrnDAO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.dao.dto.GrnDTO;
import lk.ijse.winestores.dao.dto.GrnDetailDTO;
import lk.ijse.winestores.dao.dto.GrnEmptyBottleDetailDTO;
import lk.ijse.winestores.service.SuperService;

/**
 *
 * @author Ranjith Suranga
 */
public interface GrnService extends SuperService{
    
    public int saveGrn(GrnDTO grnDto, ArrayList<GrnEmptyBottleDetailDTO> emptyBottleDetails, ArrayList<GrnDetailDTO> grnItemDetails)throws ClassNotFoundException, SQLException;
    public String getNewGrnId()throws ClassNotFoundException, SQLException;
    public ArrayList<EmptyBottleDTO> getAllEmptyBottleTypes() throws ClassNotFoundException, SQLException;
    
    public ArrayList<GrnDTO> getGrns(GrnDAO.QueryType queryType, String queryWord) throws ClassNotFoundException, SQLException;
    public boolean isGrnAuthorized(String grnId)throws ClassNotFoundException, SQLException;  
    
    public ArrayList<GrnEmptyBottleDetailDTO> getEmptyBottleDetails(String grnId) throws ClassNotFoundException, SQLException;
    public ArrayList<GrnDetailDTO> getItemDetails(String grnId, boolean grnAuthorizedStatus)throws ClassNotFoundException,SQLException;
    
    public int removeGrn(String grnId, boolean grnAuthorizedStatus) throws ClassNotFoundException, SQLException;  
    public int changeGrn(GrnDTO grnDto, ArrayList<GrnEmptyBottleDetailDTO> emptyBottleDetails, ArrayList<GrnDetailDTO> grnItemDetails, boolean grnAuthorizedStatus)throws ClassNotFoundException, SQLException;    
    
    public int authorizeGrn(String grnId) throws ClassNotFoundException, SQLException;
}
