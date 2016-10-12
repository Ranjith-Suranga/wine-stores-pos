/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.idgenerator;

import java.sql.SQLException;
import java.text.NumberFormat;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.BatchDAO;
import lk.ijse.winestores.dao.custom.GrnDAO;
import lk.ijse.winestores.dao.custom.GrnDetailDAO;
import lk.ijse.winestores.dao.custom.GrnEmptyBottleDetailDAO;
import lk.ijse.winestores.dao.custom.IdPoolDAO;
import lk.ijse.winestores.dao.custom.ItemBatchGrnDetailDAO;
import lk.ijse.winestores.dao.custom.ItemDAO;
import lk.ijse.winestores.dao.custom.MainSubCategoryDAO;
import lk.ijse.winestores.dao.custom.MainSubItemDAO;
import lk.ijse.winestores.dao.custom.MajorCategoryDAO;
import lk.ijse.winestores.dao.custom.SubCategoryDAO;
import lk.ijse.winestores.dao.custom.SupplierDAO;
import lk.ijse.winestores.dao.dto.IdPoolDTO;

/**
 *
 * @author Ranjith Suranga
 */
public class GenerateId {

    public static String getNewId(SuperDAO.DAOType table, String prefix, NumberFormat numberFormat) throws SQLException, ClassNotFoundException {

        IdPoolDAO dao = (IdPoolDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.ID_POOL);
        String oldId = dao.isIDExists(table);
        boolean idExists = false;

        if (oldId != null) {

            // Found old entry...!
            switch (table) {
                case MAJOR_CATEGORY:
                    MajorCategoryDAO majorDAO = (MajorCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAJOR_CATEGORY);
                    idExists = majorDAO.isExists(oldId);
                    break;
                case SUB_CATEGORY:
                    SubCategoryDAO subDAO = (SubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUB_CATEGORY);
                    idExists = subDAO.isExists(oldId);
                    break;
                case MAIN_SUB_CATEGORY:
                    MainSubCategoryDAO mainSubDAO = (MainSubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_CATEGORY);
                    idExists = mainSubDAO.isExists(oldId);
                    break;
                case ITEM:
                    ItemDAO itemDAO = (ItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.ITEM);
                    idExists = itemDAO.isExists(oldId);
                    break;
                case MAIN_SUB_ITEM:
                    MainSubItemDAO mainSubItemDAO = (MainSubItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_ITEM);
                    idExists = mainSubItemDAO.isExists(oldId);
                    break;
                case SUPPLIER:
                    SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUPPLIER);
                    idExists = supplierDAO.isExists(oldId);
                    break;
                case GRN:
                    GrnDAO grnDAO = (GrnDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN);
                    idExists = grnDAO.isExists(oldId);
                    break;
                case GRN_EMPTY_BOTTLE_DETAIL:
                    GrnEmptyBottleDetailDAO grnEmptyBottleDAO = (GrnEmptyBottleDetailDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN_EMPTY_BOTTLE_DETAIL);
                    idExists = grnEmptyBottleDAO.isExists(oldId);
                    break;
                case BATCH:
                    BatchDAO batchDAO = (BatchDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.BATCH);
                    idExists = batchDAO.isExists(oldId);
                    break;
                case GRN_DETAIL:
                    GrnDetailDAO grnDetailDAO = (GrnDetailDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN_DETAIL);
                    idExists = grnDetailDAO.isExists(oldId);
                    break;
            }

            // But before we delete it, let's confirm whether there is an entry in the actual table
            if (idExists) {
                dao.deleteID(new IdPoolDTO(oldId, getTableName(table)));
                return getNewId(table, prefix, numberFormat);
            } else {
                return oldId;
            }
        }

        // No old ids, so we have to generate a new ID...!
        String lastID = null;

        switch (table) {
            case MAJOR_CATEGORY:
                MajorCategoryDAO majorDAO = (MajorCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAJOR_CATEGORY);
                lastID = majorDAO.readLastId();
                break;
            case SUB_CATEGORY:
                SubCategoryDAO subDAO = (SubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUB_CATEGORY);
                lastID = subDAO.readLastId();
                break;
            case MAIN_SUB_CATEGORY:
                MainSubCategoryDAO mainSubDAO = (MainSubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_CATEGORY);
                lastID = mainSubDAO.readLastId();
                break;
            case ITEM:
                ItemDAO itemDAO = (ItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.ITEM);
                lastID = itemDAO.readLastId();
                break;
            case MAIN_SUB_ITEM:
                MainSubItemDAO mainSubItemDAO = (MainSubItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_ITEM);
                lastID = mainSubItemDAO.readLastId();
                break;
            case SUPPLIER:
                SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUPPLIER);
                lastID = supplierDAO.readLastId();
                break;
            case GRN:
                GrnDAO grnDAO = (GrnDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN);
                lastID = grnDAO.readLastId();
                break;
            case GRN_EMPTY_BOTTLE_DETAIL:
                GrnEmptyBottleDetailDAO grnEmptyBottleDAO = (GrnEmptyBottleDetailDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN_EMPTY_BOTTLE_DETAIL);
                lastID = grnEmptyBottleDAO.readLastId();
                break;
            case BATCH:
                BatchDAO batchDAO = (BatchDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.BATCH);
                lastID = batchDAO.readLastId();
                break;
            case GRN_DETAIL:
                GrnDetailDAO grnDetailDAO = (GrnDetailDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN_DETAIL);
                lastID = grnDetailDAO.readLastId();
                break;
        }

        if (lastID == null) {
            // No single entry in the table, this is the first one...!
            return (prefix != null) ? prefix : "" + numberFormat.format(1);
        }
        int newId;
        if (prefix != null) {
            newId = Integer.parseInt(lastID.split(prefix)[1]) + 1;
            return prefix + numberFormat.format(newId);
        } else {
            newId = Integer.parseInt(lastID) + 1;
            return numberFormat.format(newId);
        }

    }

    public static String genereateUniqueId(SuperDAO.DAOType table, NumberFormat numberFormat) throws ClassNotFoundException, SQLException {

        String lastID = null;

        switch (table) {
            case MAJOR_CATEGORY:
                MajorCategoryDAO majorDAO = (MajorCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAJOR_CATEGORY);
                lastID = majorDAO.readLastId();
                break;
            case SUB_CATEGORY:
                SubCategoryDAO subDAO = (SubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUB_CATEGORY);
                lastID = subDAO.readLastId();
                break;
            case MAIN_SUB_CATEGORY:
                MainSubCategoryDAO mainSubDAO = (MainSubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_CATEGORY);
                lastID = mainSubDAO.readLastId();
                break;
            case ITEM:
                ItemDAO itemDAO = (ItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.ITEM);
                lastID = itemDAO.readLastId();
                break;
            case MAIN_SUB_ITEM:
                MainSubItemDAO mainSubItemDAO = (MainSubItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_ITEM);
                lastID = mainSubItemDAO.readLastId();
                break;
            case SUPPLIER:
                SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUPPLIER);
                lastID = supplierDAO.readLastId();
                break;
            case GRN:
                GrnDAO grnDAO = (GrnDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN);
                lastID = grnDAO.readLastId();
                break;
            case GRN_EMPTY_BOTTLE_DETAIL:
                GrnEmptyBottleDetailDAO grnEmptyBottleDAO = (GrnEmptyBottleDetailDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN_EMPTY_BOTTLE_DETAIL);
                lastID = grnEmptyBottleDAO.readLastId();
                break;
            case BATCH:
                BatchDAO batchDAO = (BatchDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.BATCH);
                lastID = batchDAO.readLastId();
                break;
            case GRN_DETAIL:
                GrnDetailDAO grnDetailDAO = (GrnDetailDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.GRN_DETAIL);
                lastID = grnDetailDAO.readLastId();
                break;
            case ITEM_BATCH_GRN_DETAIL:
                ItemBatchGrnDetailDAO itemBatchGrnDetailDAO = (ItemBatchGrnDetailDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.ITEM_BATCH_GRN_DETAIL);
                lastID = itemBatchGrnDetailDAO.readLastId();
                break;                
        }

        if (lastID == null) {
            // No single entry in the table, this is the first one...!
            return numberFormat.format(1);
        }
        int newId = Integer.parseInt(lastID) + 1;
        return numberFormat.format(newId);
    }

    public static String getTableName(SuperDAO.DAOType table) {
        String tblName;

        switch (table) {
            case MAJOR_CATEGORY:
                tblName = "main_category";
                break;
            case SUB_CATEGORY:
                tblName = "sub_category";
                break;
            case MAIN_SUB_CATEGORY:
                tblName = "main_sub_category";
                break;
            case ITEM:
                tblName = "item";
                break;
            case SUPPLIER:
                tblName = "supplier";
                break;
            case GRN:
                tblName = "grn";
                break;
            case GRN_EMPTY_BOTTLE_DETAIL:
                tblName = "grn_empty_bottle_detail";
                break;
            case BATCH:
                tblName = "batch";
                break;
            case GRN_DETAIL:
                tblName = "grn_detail";
                break;
            default:
                tblName = null;
        }
        return tblName;
    }
}
