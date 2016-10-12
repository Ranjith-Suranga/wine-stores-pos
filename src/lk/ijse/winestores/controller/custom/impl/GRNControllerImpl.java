/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.custom.GRNController;
import lk.ijse.winestores.dao.custom.GrnDAO;
import lk.ijse.winestores.dao.dto.EmptyBottleDTO;
import lk.ijse.winestores.dao.dto.GrnDTO;
import lk.ijse.winestores.dao.dto.GrnDetailDTO;
import lk.ijse.winestores.dao.dto.GrnEmptyBottleDetailDTO;
import lk.ijse.winestores.service.ServiceFactory;
import lk.ijse.winestores.service.SuperService;
import lk.ijse.winestores.service.custom.GrnService;
import lk.ijse.winestores.service.custom.ItemService;

/**
 *
 * @author Ranjith Suranga
 */
public class GRNControllerImpl implements GRNController {

    @Override
    public String getNewGrnId() throws ClassNotFoundException, SQLException {
        GrnService service = (GrnService) ServiceFactory.getInstance().getService(SuperService.ServiceType.GRN);
        return service.getNewGrnId();
    }

    @Override
    public ArrayList<EmptyBottleDTO> getAllEmptyBottleTypes() throws ClassNotFoundException, SQLException {
        GrnService service = (GrnService) ServiceFactory.getInstance().getService(SuperService.ServiceType.GRN);
        return service.getAllEmptyBottleTypes();
    }

    @Override
    public int saveGrn(GRNDetailsModel grnModel, ArrayList<GRNItemModel> items, ArrayList<GRNEmptyBottleDetailModel> emptyBottleDetails) throws ClassNotFoundException, SQLException {

        ArrayList<GrnEmptyBottleDetailDTO> alEmpty = null;

        GrnService service = (GrnService) ServiceFactory.getInstance().getService(SuperService.ServiceType.GRN);

        GrnDTO grnDto = new GrnDTO(grnModel.getGrnId(),
                grnModel.getSupplierId(),
                grnModel.getInvoiceDate(),
                grnModel.getGrnDate(),
                grnModel.getUser(),
                grnModel.getLastEditedTime(),
                grnModel.getInvoiceId()
        );

        // Sometimes, this could be null
        if (emptyBottleDetails != null) {

            alEmpty = new ArrayList<>();

            for (GRNEmptyBottleDetailModel emptyBottle : emptyBottleDetails) {
                GrnEmptyBottleDetailDTO dto = new GrnEmptyBottleDetailDTO(
                        emptyBottle.getId(), // No Id yet..!
                        emptyBottle.getEstimatedBottles(),
                        emptyBottle.getGivenBottles(),
                        emptyBottle.getDueDate(),
                        emptyBottle.getGrnId(),
                        emptyBottle.getBottleType());
                alEmpty.add(dto);
            }
        }

        ArrayList<GrnDetailDTO> grnItems = new ArrayList<>();

        for (GRNItemModel grnItem : items) {
            GrnDetailDTO dto = new GrnDetailDTO(
                    null, // Not yet...!
                    grnItem.getItemCode(), // Need to get main_sub_item_id via this item_code
                    null, // Not yet...!
                    grnItem.getQty(),
                    grnItem.getSellingPrice().toPlainString(),
                    grnItem.getCostPrice().toPlainString(),
                    grnItem.getExpireDate());
            grnItems.add(dto);
        }

        return service.saveGrn(grnDto, alEmpty, grnItems);

    }

    @Override
    public ArrayList<GRNDetailsModel> getGrns(GrnDAO.QueryType queryType, String queryWord) throws ClassNotFoundException, SQLException {

        GrnService service = (GrnService) ServiceFactory.getInstance().getService(SuperService.ServiceType.GRN);
        ArrayList<GrnDTO> grns = service.getGrns(queryType, queryWord);

        if (grns == null) {
            return null;
        }

        ArrayList<GRNDetailsModel> al = new ArrayList<>();

        for (GrnDTO dto : grns) {
            GRNDetailsModel model = new GRNDetailsModel(dto.getGrnId(), dto.getSupplierId(), dto.getInvoiceDate(), dto.getGrnDate(), dto.getWhoMade(), dto.getEditDateTime(), dto.getInvoiceId());
            al.add(model);
        }
        return al;
    }

    @Override
    public boolean isGrnAuthorized(String grnId) throws ClassNotFoundException, SQLException {
        GrnService service = (GrnService) ServiceFactory.getInstance().getService(SuperService.ServiceType.GRN);
        return service.isGrnAuthorized(grnId);
    }

    @Override
    public ArrayList<GRNEmptyBottleDetailModel> getEmptyBottleDetails(String grnId) throws ClassNotFoundException, SQLException {
        GrnService service = (GrnService) ServiceFactory.getInstance().getService(SuperService.ServiceType.GRN);
        ArrayList<GrnEmptyBottleDetailDTO> empty = service.getEmptyBottleDetails(grnId);
        if (empty == null) {
            return null;
        }

        ArrayList<GRNEmptyBottleDetailModel> al = new ArrayList<>();
        for (GrnEmptyBottleDetailDTO dto : empty) {
            GRNEmptyBottleDetailModel model = new GRNEmptyBottleDetailModel(dto.getId(),
                    dto.getEstimatedBottles(),
                    dto.getGivenBottles(),
                    dto.getDueDate(),
                    dto.getGrnId(),
                    dto.getBottleType());
            al.add(model);
        }

        return al;

    }

    @Override
    public ArrayList<GRNItemModel> getItemDetails(String grnId, boolean grnAuthorizedStatus) throws ClassNotFoundException, SQLException {
        GrnService service = (GrnService) ServiceFactory.getInstance().getService(SuperService.ServiceType.GRN);
        ArrayList<GrnDetailDTO> items = service.getItemDetails(grnId, grnAuthorizedStatus);
        if (items == null) {
            return null;
        }
        ArrayList<GRNItemModel> al = new ArrayList<>();
        for (GrnDetailDTO dto : items) {

            ItemService itemService = (ItemService) ServiceFactory.getInstance().getService(SuperService.ServiceType.ITEM);
            String[] itemMainDetails = itemService.getItemMainDetails(dto.getItemId());
            
            if (itemMainDetails == null) return null;
            
            GRNItemModel model = new GRNItemModel(
                    itemMainDetails[0],                  // Major Cateogry
                    itemMainDetails[1],                  // Sub Category
                    itemMainDetails[3],
                    itemMainDetails[2],                  // ItemName
                    dto.getBatchQty(),
                    new BigDecimal(dto.getBuyingPrice()),
                    new BigDecimal(dto.getSellingPrice()),
                    dto.getExpiryDate());
            
            al.add(model);
        }
        
        return al;
    }

    @Override
    public int removeGrn(String grnId, boolean grnAuthorizedStatus) throws ClassNotFoundException, SQLException {
        GrnService service = (GrnService) ServiceFactory.getInstance().getService(SuperService.ServiceType.GRN);
        return service.removeGrn(grnId, grnAuthorizedStatus);
    }

    @Override
    public int changeGrn(GRNDetailsModel grnModel, ArrayList<GRNItemModel> items, ArrayList<GRNEmptyBottleDetailModel> emptyBottleDetails, boolean grnAuthorizedStatus) throws ClassNotFoundException, SQLException {
        ArrayList<GrnEmptyBottleDetailDTO> alEmpty = null;

        GrnService service = (GrnService) ServiceFactory.getInstance().getService(SuperService.ServiceType.GRN);

        GrnDTO grnDto = new GrnDTO(grnModel.getGrnId(),
                grnModel.getSupplierId(),
                grnModel.getInvoiceDate(),
                grnModel.getGrnDate(),
                grnModel.getUser(),
                grnModel.getLastEditedTime(),
                grnModel.getInvoiceId()
        );

        // Sometimes, this could be null
        if (emptyBottleDetails != null) {

            alEmpty = new ArrayList<>();

            for (GRNEmptyBottleDetailModel emptyBottle : emptyBottleDetails) {
                GrnEmptyBottleDetailDTO dto = new GrnEmptyBottleDetailDTO(
                        emptyBottle.getId(), // No Id yet..!
                        emptyBottle.getEstimatedBottles(),
                        emptyBottle.getGivenBottles(),
                        emptyBottle.getDueDate(),
                        emptyBottle.getGrnId(),
                        emptyBottle.getBottleType());
                alEmpty.add(dto);
            }
        }

        ArrayList<GrnDetailDTO> grnItems = new ArrayList<>();

        for (GRNItemModel grnItem : items) {
            GrnDetailDTO dto = new GrnDetailDTO(
                    null, // Not yet...!
                    grnItem.getItemCode(), // Need to get main_sub_item_id via this item_code
                    null, // Not yet...!
                    grnItem.getQty(),
                    grnItem.getSellingPrice().toPlainString(),
                    grnItem.getCostPrice().toPlainString(),
                    grnItem.getExpireDate());
            grnItems.add(dto);
        }
        
        return service.changeGrn(grnDto, alEmpty, grnItems, grnAuthorizedStatus);
    }

    @Override
    public int authorizeGrn(String grnId) throws ClassNotFoundException, SQLException {
        GrnService service = (GrnService) ServiceFactory.getInstance().getService(SuperService.ServiceType.GRN);
        return service.authorizeGrn(grnId);
    }

}
