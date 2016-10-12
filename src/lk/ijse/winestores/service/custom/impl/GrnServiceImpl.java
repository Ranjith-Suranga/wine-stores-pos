/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom.impl;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.EmptyBottleDAO;
import lk.ijse.winestores.dao.custom.GrnDAO;
import lk.ijse.winestores.dao.custom.GrnEmptyBottleDetailDAO;
import lk.ijse.winestores.dao.custom.MainSubItemDAO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.dao.dto.GrnDTO;
import lk.ijse.winestores.dao.dto.GrnDetailDTO;
import lk.ijse.winestores.dao.dto.GrnEmptyBottleDetailDTO;
import lk.ijse.winestores.service.custom.GrnService;
import lk.ijse.winestores.service.idgenerator.GenerateId;

/**
 *
 * @author Ranjith Suranga
 */
public class GrnServiceImpl implements GrnService {

    @Override
    public String getNewGrnId() throws ClassNotFoundException, SQLException {
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumIntegerDigits(4);
        nf.setMinimumIntegerDigits(4);
        return GenerateId.getNewId(SuperDAO.DAOType.GRN, null, nf);
    }

    @Override
    public ArrayList<EmptyBottleDTO> getAllEmptyBottleTypes() throws ClassNotFoundException, SQLException {
        EmptyBottleDAO dao = (EmptyBottleDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.EMPTY_BOTTLE);
        return dao.readAllBottleTypes();
    }

    @Override
    public int saveGrn(GrnDTO grnDto, ArrayList<GrnEmptyBottleDetailDTO> emptyBottleDetails, ArrayList<GrnDetailDTO> grnItemDetails) throws ClassNotFoundException, SQLException {

        NumberFormat numberFormat;
        numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setMinimumIntegerDigits(5);
        numberFormat.setMaximumIntegerDigits(5);
        numberFormat.setGroupingUsed(false);

        if (emptyBottleDetails != null) {

            String id = GenerateId.genereateUniqueId(SuperDAO.DAOType.GRN_EMPTY_BOTTLE_DETAIL, numberFormat);
            for (GrnEmptyBottleDetailDTO dto : emptyBottleDetails) {
                dto.setId(id);
                id = numberFormat.format(Integer.parseInt(id) + 1);
            }
        }

        String batchId = GenerateId.genereateUniqueId(SuperDAO.DAOType.BATCH, numberFormat);

        String id = GenerateId.genereateUniqueId(SuperDAO.DAOType.GRN_DETAIL, numberFormat);
        for (GrnDetailDTO grnDetail : grnItemDetails) {
            grnDetail.setGrnDetailId(id);
            id = numberFormat.format(Integer.parseInt(id) + 1);

            MainSubItemDAO dao = (MainSubItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_ITEM);
            String mainSubItemId = dao.readId(grnDetail.getItemId());
            grnDetail.setItemId(mainSubItemId);

            grnDetail.setBatchId(batchId);
        }

        GrnDAO dao = (GrnDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN);
        return dao.createGrn(grnDto, emptyBottleDetails, grnItemDetails);

    }

    @Override
    public ArrayList<GrnDTO> getGrns(GrnDAO.QueryType queryType, String queryWord) throws ClassNotFoundException, SQLException {

        GrnDAO dao = (GrnDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN);
        return dao.readGrns(queryType, queryWord);

    }

    @Override
    public boolean isGrnAuthorized(String grnId) throws ClassNotFoundException, SQLException {
        GrnDAO dao = (GrnDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN);
        return dao.readGrnStatus(grnId);
    }

    @Override
    public ArrayList<GrnEmptyBottleDetailDTO> getEmptyBottleDetails(String grnId) throws ClassNotFoundException, SQLException {
        GrnEmptyBottleDetailDAO dao = (GrnEmptyBottleDetailDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN_EMPTY_BOTTLE_DETAIL);
        return dao.readEmptyBottleDetails(grnId);
    }

    @Override
    public ArrayList<GrnDetailDTO> getItemDetails(String grnId, boolean grnAuthorizedStatus) throws ClassNotFoundException, SQLException {
        GrnDAO dao = (GrnDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN);
        return dao.readItemDetails(grnId, grnAuthorizedStatus);
    }

    @Override
    public int removeGrn(String grnId, boolean grnAuthorizedStatus) throws ClassNotFoundException, SQLException {
        GrnDAO dao = (GrnDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN);
        return dao.deleteGrn(grnId, grnAuthorizedStatus);
    }

    @Override
    public int changeGrn(GrnDTO grnDto, ArrayList<GrnEmptyBottleDetailDTO> emptyBottleDetails, ArrayList<GrnDetailDTO> grnItemDetails, boolean grnAuthorizedStatus) throws ClassNotFoundException, SQLException {
        NumberFormat numberFormat;
        numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setMinimumIntegerDigits(5);
        numberFormat.setMaximumIntegerDigits(5);
        numberFormat.setGroupingUsed(false);

        if (emptyBottleDetails != null) {

            String id = GenerateId.genereateUniqueId(SuperDAO.DAOType.GRN_EMPTY_BOTTLE_DETAIL, numberFormat);
            for (GrnEmptyBottleDetailDTO dto : emptyBottleDetails) {
                dto.setId(id);
                id = numberFormat.format(Integer.parseInt(id) + 1);
            }
        }

        String batchId = GenerateId.genereateUniqueId(SuperDAO.DAOType.BATCH, numberFormat);

        String id = GenerateId.genereateUniqueId(SuperDAO.DAOType.GRN_DETAIL, numberFormat);
        for (GrnDetailDTO grnDetail : grnItemDetails) {
            grnDetail.setGrnDetailId(id);
            id = numberFormat.format(Integer.parseInt(id) + 1);

            MainSubItemDAO dao = (MainSubItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_ITEM);
            String mainSubItemId = dao.readId(grnDetail.getItemId());
            grnDetail.setItemId(mainSubItemId);

            grnDetail.setBatchId(batchId);
        }

        GrnDAO dao = (GrnDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN);
        return dao.updateGrn(grnDto, emptyBottleDetails, grnItemDetails, grnAuthorizedStatus);

    }

    @Override
    public int authorizeGrn(String grnId) throws ClassNotFoundException, SQLException {
        GrnDAO dao = (GrnDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN);
        return dao.authorizeGrn(grnId);
    }

}
