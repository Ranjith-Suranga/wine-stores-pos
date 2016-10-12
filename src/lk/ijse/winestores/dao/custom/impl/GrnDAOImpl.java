/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.GrnDAO;
import lk.ijse.winestores.dao.custom.IdPoolDAO;
import lk.ijse.winestores.dao.custom.ItemDetailsDAO;
import lk.ijse.winestores.dao.custom.MainSubItemDAO;
import lk.ijse.winestores.dao.dto.GrnDTO;
import lk.ijse.winestores.dao.dto.GrnDetailDTO;
import lk.ijse.winestores.dao.dto.GrnEmptyBottleDetailDTO;
import lk.ijse.winestores.dao.dto.IdPoolDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;
import lk.ijse.winestores.dao.dto.MainSubItemDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.resource.db.DatabaseResourceConnection;
import lk.ijse.winestores.service.idgenerator.GenerateId;

/**
 *
 * @author Ranjith Suranga
 */
public class GrnDAOImpl implements GrnDAO {

    /*
    =============================
        ID Generator Methods
    =============================
     */
    @Override
    public boolean isExists(String queryId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(GRN_CHECK_EXISTENCY_BY_ID);
        pstm.setObject(1, queryId);
        ResultSet rst = pstm.executeQuery();
        boolean result = rst.next();
        con.getConnection().close();
        return result;
    }

    @Override
    public String readLastId() throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(GRN_READ_LAST_ID_SQL);
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            String lastId = rst.getString(1);
            con.getConnection().close();
            return lastId;
        } else {
            con.getConnection().close();
            return null;
        }
    }

    @Override
    public int createGrn(GrnDTO grnDto, ArrayList<GrnEmptyBottleDetailDTO> emptyBottleDetails, ArrayList<GrnDetailDTO> grnItemDetails) throws ClassNotFoundException, SQLException {

        DatabaseResourceConnection dbcon = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        Connection con = dbcon.getConnection();
        con.setAutoCommit(false);

        // (1) GRN table
        PreparedStatement pstmGrn = con.prepareStatement("INSERT INTO grn VALUES(?,?,?,?,?,?,?)");
        pstmGrn.setObject(1, grnDto.getGrnId());
        pstmGrn.setObject(2, grnDto.getSupplierId());
        pstmGrn.setObject(3, grnDto.getInvoiceId());
        pstmGrn.setObject(4, grnDto.getInvoiceDate());
        pstmGrn.setObject(5, grnDto.getGrnDate());
        pstmGrn.setObject(6, grnDto.getWhoMade());
        pstmGrn.setObject(7, grnDto.getEditDateTime());
        int result = pstmGrn.executeUpdate();

        if (result > 0) {

            // (2) EmptyBottleDetail table
            if (emptyBottleDetails != null) {

                for (GrnEmptyBottleDetailDTO empty : emptyBottleDetails) {
                    PreparedStatement pstmEmptyBtl = con.prepareStatement("INSERT INTO grn_empty_bottle_detail VALUES(?,?,?,?,?,?)");
                    pstmEmptyBtl.setObject(1, empty.getId());
                    pstmEmptyBtl.setObject(2, empty.getEstimatedBottles());
                    pstmEmptyBtl.setObject(3, empty.getGivenBottles());
                    pstmEmptyBtl.setObject(4, empty.getDueDate());
                    pstmEmptyBtl.setObject(5, grnDto.getGrnId());
                    pstmEmptyBtl.setObject(6, empty.getBottleType());
                    result = pstmEmptyBtl.executeUpdate();
                    if (result <= 0) {
                        break;
                    }
                }

            }

            if (result > 0) {

                // (3) Batch table
                PreparedStatement pstmBatch = con.prepareStatement("INSERT INTO batch VALUES(?,?)");
                pstmBatch.setObject(1, grnItemDetails.get(0).getBatchId());
                pstmBatch.setObject(2, grnDto.getGrnId());
                result = pstmBatch.executeUpdate();

                if (result > 0) {

                    // (4) GrnDetail table
                    for (GrnDetailDTO grnDetail : grnItemDetails) {
                        PreparedStatement pstmGrnDetail = con.prepareStatement("INSERT INTO grn_detail VALUES(?,?,?,?,?,?,?)");
                        pstmGrnDetail.setObject(1, grnDetail.getGrnDetailId());
                        pstmGrnDetail.setObject(2, grnDetail.getItemId());
                        pstmGrnDetail.setObject(3, grnDetail.getBatchId());
                        pstmGrnDetail.setObject(4, grnDetail.getBatchQty());
                        pstmGrnDetail.setObject(5, grnDetail.getSellingPrice());
                        pstmGrnDetail.setObject(6, grnDetail.getBuyingPrice());
                        if (grnDetail.getExpiryDate() != null & !grnDetail.getExpiryDate().isEmpty()) {
                            pstmGrnDetail.setObject(7, grnDetail.getExpiryDate());
                        } else {
                            pstmGrnDetail.setObject(7, null);
                        }
                        result = pstmGrnDetail.executeUpdate();
                        if (result <= 0) {
                            con.rollback();
                            break;
                        } else {

                            // (5) ItemDetails table
                            PreparedStatement pstm = con.prepareStatement("SELECT item_code FROM main_sub_item WHERE main_sub_item_id = ?; ");
                            pstm.setString(1, grnDetail.getItemId());
                            ResultSet rst = pstm.executeQuery();

                            if (rst.next()) {
                                ItemDetailsDAO dao = (ItemDetailsDAO) DAOFactory.getInstance().getDAO(DAOType.ITEM_DETAILS);

//                                if (!dao.isItemDetailsExists(rst.getString(1))) {
//                                    PreparedStatement pstmItemDetails = con.prepareStatement("INSERT INTO item_details VALUES (?,?,?,?)");
//                                    pstmItemDetails.setString(1, rst.getString(1));
//                                    pstmItemDetails.setInt(2, grnDetail.getBatchQty());
//                                    pstmItemDetails.setDouble(3, Double.valueOf(grnDetail.getSellingPrice()));
//                                    pstmItemDetails.setDouble(4, Double.valueOf(grnDetail.getBuyingPrice()));
//                                    result = pstmItemDetails.executeUpdate();
//
//                                    if (result == 0) {
//                                        con.rollback();
//                                        result = 0;
//                                        break;
//                                    }
//                                } else {
//                                    
//                                    // If the same item exists, then let's just change the qty only
//                                    int currentQty = dao.readCurrentQty(rst.getString(1));
//                                    currentQty += grnDetail.getBatchQty();
//                                    result = dao.updateCurrentQty(rst.getString(1), currentQty);
//
//                                    if (result == 0) {
//                                        con.rollback();
//                                        result = 0;
//                                        break;
//                                    }
//                                }
                            } else {
                                // We are in trouble... Don't know how we get into here though...!
                                con.rollback();
                                result = 0;
                                break;
                            }

                        }
                    }

                    if (result > 0) {

                        con.commit();
                        con.setAutoCommit(true);
                    }

                } else {
                    con.rollback();
                }

            } else {
                con.rollback();
            }

        } else {
            con.rollback();
        }

        con.close();
        return result;

    }

    @Override
    public ArrayList<GrnDTO> readGrns(QueryType queryType, String queryWord) throws ClassNotFoundException, SQLException {

        DatabaseResourceConnection dbcon = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        String sql = "SELECT * FROM grn WHERE ";

        switch (queryType) {
            case GRN_DATE:
                sql += "grn_date";
                break;
            case GRN_ID:
                sql += "grn_id";
                break;
            case INVOICE_DATE:
                sql += "invoice_date";
                break;
            case INVOICE_NUMBER:
                sql += "invoice_id";
                break;
            case SUPPLIER_NAME:

                break;
        }
        sql += " LIKE '" + queryWord + "%'";
        if (queryType == QueryType.SUPPLIER_NAME) {
            sql = "SELECT grn.* FROM grn JOIN supplier ON grn.supplier_id = supplier.supplier_id WHERE supplier.name LIKE '" + queryWord + "%';";
        }

        PreparedStatement pstm = dbcon.getConnection().prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();

        ArrayList<GrnDTO> al = null;

        while (rst.next()) {

            if (al == null) {
                al = new ArrayList<>();
            }

            GrnDTO dto = new GrnDTO(rst.getString(1), rst.getString(2), rst.getString(4), rst.getString(5), rst.getString(6), rst.getString(7), rst.getString(3));
            al.add(dto);
        }

        dbcon.getConnection().close();
        return al;

    }

    @Override
    public boolean readGrnStatus(String grnId) throws ClassNotFoundException, SQLException {

        DatabaseResourceConnection dbcon = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = dbcon.getConnection().prepareStatement("SELECT * FROM grn JOIN supplier ON grn.supplier_id = supplier.supplier_id JOIN batch ON grn.grn_id = batch.grn_id JOIN grn_detail ON grn_detail.batch_id = batch.batch_id WHERE grn.grn_id = ?;");
        pstm.setObject(1, grnId);
        ResultSet rst = pstm.executeQuery();

        boolean result = !rst.next();
        dbcon.getConnection().close();
        return result;
    }

    @Override
    public ArrayList<GrnDetailDTO> readItemDetails(String grnId, boolean grnAuthorizedStatus) throws ClassNotFoundException, SQLException {

        DatabaseResourceConnection dbcon = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm;
        if (!grnAuthorizedStatus) {
            pstm = dbcon.getConnection().prepareStatement("SELECT grn_detail.* FROM grn_detail JOIN batch ON grn_detail.batch_id = batch.batch_id JOIN grn ON grn.grn_id = batch.grn_id WHERE grn.grn_id = ?;");
        } else {
            pstm = dbcon.getConnection().prepareStatement("SELECT `item_batch_grn_detail`.* FROM `item_batch_grn_detail` JOIN batch ON `item_batch_grn_detail`.batch_id = batch.batch_id JOIN grn ON grn.grn_id = batch.grn_id WHERE grn.grn_id = ?;");
        }
        pstm.setString(1, grnId);
        ResultSet rst = pstm.executeQuery();

        ArrayList<GrnDetailDTO> al = null;

        while (rst.next()) {
            if (al == null) {
                al = new ArrayList<>();
            }
            GrnDetailDTO dto = new GrnDetailDTO(rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7));

            al.add(dto);
        }

        dbcon.getConnection().close();
        return al;

    }

    @Override
    public int deleteGrn(String grnId, boolean grnAuthorizedStatus) throws ClassNotFoundException, SQLException {

        String batchId = null;
        int result;

        DatabaseResourceConnection dbcon = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        Connection con = dbcon.getConnection();
        con.setAutoCommit(false);

        // First, getting the batch ID...!
        PreparedStatement pstmBatch = con.prepareStatement("SELECT * FROM batch WHERE grn_id=?");
        pstmBatch.setString(1, grnId);
        ResultSet rst = pstmBatch.executeQuery();
        if (rst.next()) {
            batchId = rst.getString(1);
        } else {
            // Which means we are in trouble...!
            con.setAutoCommit(true);
            con.close();
            return -1;
        }

        if (batchId == null) {
            // No batch Id means we are in trouble...!
            con.setAutoCommit(true);
            con.close();
            return -1;
        }

        // First let's delete records from grn_detail or item_batch_grn_detail
        PreparedStatement pstmGrnDetail = null;
        if (!grnAuthorizedStatus) {
            pstmGrnDetail = con.prepareStatement("DELETE FROM grn_detail WHERE batch_id = ?;");
        } else {

            PreparedStatement pstm = con.prepareStatement("SELECT main_sub_item_id,batch_qty FROM item_batch_grn_detail WHERE batch_id=? ");
            pstm.setObject(1, batchId);
            ResultSet rstItemBatchGrnDetails = pstm.executeQuery();

            boolean success = false;        // a temp local variable for holding the update & createion process result

            // To get item code
            MainSubItemDAO mainSubItemDAO = (MainSubItemDAO) DAOFactory.getInstance().getDAO(DAOType.MAIN_SUB_ITEM);

            // To get current qty
            ItemDetailsDAO itemDetailsDAO = (ItemDetailsDAO) DAOFactory.getInstance().getDAO(DAOType.ITEM_DETAILS);

            while (rstItemBatchGrnDetails.next()) {

                // First, let's get item code
                MainSubItemDTO mainSubItemDTO = mainSubItemDAO.readMainSubItemByPK(rstItemBatchGrnDetails.getString(1));
                int currentQty = itemDetailsDAO.readCurrentQty(mainSubItemDTO.getItemCode());

                // Calculate new stock..!
                currentQty = currentQty - rstItemBatchGrnDetails.getInt(2);
                if (currentQty < 0) {
                    currentQty = 0;
                }

                success = itemDetailsDAO.updateCurrentQty(con, mainSubItemDTO.getItemCode(), currentQty);

                if (!success) {
                    // Trouble...!
                    con.rollback();
                    con.setAutoCommit(true);
                    con.close();
                    return -1;
                }
            }

//            Statement stm = con.createStatement();
//            ResultSet rstTemp = stm.executeQuery("SELECT main_sub_item_id,batch_qty FROM item_batch_grn_detail WHERE batch_id='" + batchId + "'");
//            while (rstTemp.next()) {
//                ItemDetailsDAO itemDetailsDAO = (ItemDetailsDAO) DAOFactory.getInstance().getDAO(DAOType.ITEM_DETAILS);
//                Statement stm1 = con.createStatement();
//                ResultSet rst1 = stm.executeQuery("SELECT item_code FROM main_sub_item WHERE main_sub_item_id='" + rstTemp.getString(1) + "'");
//                int currentQty = itemDetailsDAO.readCurrentQty(rst1.getString(1));
//                currentQty = currentQty - rstTemp.getInt(2);
//                if (currentQty < 0) {
//                    currentQty = 0;
//                }
//                PreparedStatement pstmTemp = con.prepareStatement("UPDATE item_details SET qty=?");
//                pstmTemp.setInt(1, currentQty);
//                result = pstmTemp.executeUpdate();
//                if (result == 0) {
//                    // Trouble...!
//                    con.rollback();
//                    con.setAutoCommit(true);
//                    con.close();
//                    return -1;
//                }
//            }
            pstmGrnDetail = con.prepareStatement("DELETE FROM item_batch_grn_detail WHERE batch_id = ?;");
        }
        pstmGrnDetail.setObject(1, batchId);
        result = pstmGrnDetail.executeUpdate();

        if (result > 0) {

            // Then let's delete records from batch table..!
            result = pstmBatch.executeUpdate("DELETE FROM batch WHERE grn_id='" + grnId + "'");

            if (result > 0) {

                // Then let's delete records from grn_empty_bottle detail
                PreparedStatement pstmEmpty = con.prepareStatement("SELECT * FROM grn_empty_bottle_detail WHERE grn_id=?");
                pstmEmpty.setString(1, grnId);
                rst = pstmEmpty.executeQuery();
                if (rst.next()) {
                    result = pstmEmpty.executeUpdate("DELETE FROM grn_empty_bottle_detail WHERE grn_id='" + grnId + "'");
                    if (result <= 0) {
                        // Trouble...!
                        con.rollback();
                        con.setAutoCommit(true);
                        con.close();
                        return -1;
                    }
                }

                // The the last...! Let's delete from grn table
                PreparedStatement pstmGrn = con.prepareStatement("DELETE FROM grn WHERE grn_id=?");
                pstmGrn.setString(1, grnId);

                result = pstmGrn.executeUpdate();

                if (result > 0) {

                    IdPoolDAO dao = (IdPoolDAO) DAOFactory.getInstance().getDAO(DAOType.ID_POOL);
                    IdPoolDTO idDto = new IdPoolDTO(grnId, "grn");
                    result = dao.createID(idDto);

                    if (result == 0) {
                        con.setAutoCommit(true);
                        con.close();
                        return result;
                    }

                    con.commit();
                    con.setAutoCommit(true);
                    con.close();
                    return result;

                } else {

                    con.rollback();
                    con.setAutoCommit(false);
                    con.close();
                    return result;
                }

            } else {
                con.rollback();
            }

        } else {
            con.rollback();
        }

        con.setAutoCommit(false);
        con.close();
        return result;

    }

    @Override
    public int updateGrn(GrnDTO grnDto, ArrayList<GrnEmptyBottleDetailDTO> emptyBottleDetails, ArrayList<GrnDetailDTO> grnItemDetails, boolean grnAuthorizedStatus) throws ClassNotFoundException, SQLException {

        String batchId = null;
        String grnId = grnDto.getGrnId();

        int result;
        DatabaseResourceConnection dbcon = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        Connection con = dbcon.getConnection();
        con.setAutoCommit(false);

        // First Deleteing Process
        //============================================================================================
        // First, getting the batch ID...!
        PreparedStatement pstmBatch = con.prepareStatement("SELECT * FROM batch WHERE grn_id=?");
        pstmBatch.setString(1, grnId);
        ResultSet rst = pstmBatch.executeQuery();
        if (rst.next()) {
            batchId = rst.getString(1);
        } else {
            // Which means we are in trouble...!
            con.setAutoCommit(true);
            con.close();
            return -1;
        }

        if (batchId == null) {
            // No batch Id means we are in trouble...!
            con.setAutoCommit(true);
            con.close();
            return -1;
        }

        // First let's delete records from grn_detail or item_batch_grn_detail
        PreparedStatement pstmGrnDetail = null;
        // Before we are going delete the record, let's just take old qty
        Statement stmGrnDetail = con.createStatement();

        HashMap<String, Integer> oldQtys = new HashMap<>();
        //ArrayList<Integer> oldQtys = new ArrayList<>();
        if (!grnAuthorizedStatus) {
            pstmGrnDetail = con.prepareStatement("DELETE FROM grn_detail WHERE batch_id = ?;");
        } else {
            ResultSet rstTemp = stmGrnDetail.executeQuery("SELECT main_sub_item_id,batch_qty FROM item_batch_grn_detail WHERE batch_id ='" + batchId + "'");
            while (rstTemp.next()) {
                oldQtys.put(rstTemp.getString(1), rstTemp.getInt(2));
                //oldQtys.add(rst.getInt(1));
            }
            pstmGrnDetail = con.prepareStatement("DELETE FROM item_batch_grn_detail WHERE batch_id = ?;");
        }
        pstmGrnDetail.setObject(1, batchId);
        result = pstmGrnDetail.executeUpdate();

        if (result > 0) {

            // Then let's delete records from batch table..!
            result = pstmBatch.executeUpdate("DELETE FROM batch WHERE grn_id='" + grnId + "'");

            if (result > 0) {

                // Then let's delete records from grn_empty_bottle detail
                PreparedStatement pstmEmpty = con.prepareStatement("SELECT * FROM grn_empty_bottle_detail WHERE grn_id=?");
                pstmEmpty.setString(1, grnId);
                rst = pstmEmpty.executeQuery();
                if (rst.next()) {
                    result = pstmEmpty.executeUpdate("DELETE FROM grn_empty_bottle_detail WHERE grn_id='" + grnId + "'");
                    if (result <= 0) {
                        // Trouble...!
                        con.rollback();
                        con.setAutoCommit(true);
                        con.close();
                        return -1;
                    }
                }

                // The the last...! Let's delete from grn table
                PreparedStatement pstmGrn = con.prepareStatement("DELETE FROM grn WHERE grn_id=?");
                pstmGrn.setString(1, grnId);

                result = pstmGrn.executeUpdate();

                if (result > 0) {
                    IdPoolDAO dao = (IdPoolDAO) DAOFactory.getInstance().getDAO(DAOType.ID_POOL);
                    IdPoolDTO idDto = new IdPoolDTO(grnId, "grn");
                    result = dao.createID(idDto);

                    if (result == 0) {
                        con.setAutoCommit(true);
                        con.close();
                        return result;
                    } else {
                        // Second Creating Process
                        //============================================================================================
                        // (1) GRN table
                        PreparedStatement pstmGrn1 = con.prepareStatement("INSERT INTO grn VALUES(?,?,?,?,?,?,?)");
                        pstmGrn1.setObject(1, grnDto.getGrnId());
                        pstmGrn1.setObject(2, grnDto.getSupplierId());
                        pstmGrn1.setObject(3, grnDto.getInvoiceId());
                        pstmGrn1.setObject(4, grnDto.getInvoiceDate());
                        pstmGrn1.setObject(5, grnDto.getGrnDate());
                        pstmGrn1.setObject(6, grnDto.getWhoMade());
                        pstmGrn1.setObject(7, grnDto.getEditDateTime());
                        result = pstmGrn1.executeUpdate();

                        if (result > 0) {

                            // (2) EmptyBottleDetail table
                            if (emptyBottleDetails != null) {

                                for (GrnEmptyBottleDetailDTO empty : emptyBottleDetails) {
                                    PreparedStatement pstmEmptyBtl = con.prepareStatement("INSERT INTO grn_empty_bottle_detail VALUES(?,?,?,?,?,?)");
                                    pstmEmptyBtl.setObject(1, empty.getId());
                                    pstmEmptyBtl.setObject(2, empty.getEstimatedBottles());
                                    pstmEmptyBtl.setObject(3, empty.getGivenBottles());
                                    pstmEmptyBtl.setObject(4, empty.getDueDate());
                                    pstmEmptyBtl.setObject(5, grnDto.getGrnId());
                                    pstmEmptyBtl.setObject(6, empty.getBottleType());
                                    result = pstmEmptyBtl.executeUpdate();
                                    if (result <= 0) {
                                        break;
                                    }
                                }

                            }

                            if (result > 0) {

                                // (3) Batch table
                                pstmBatch = con.prepareStatement("INSERT INTO batch VALUES(?,?)");
                                pstmBatch.setObject(1, grnItemDetails.get(0).getBatchId());
                                pstmBatch.setObject(2, grnDto.getGrnId());
                                result = pstmBatch.executeUpdate();

                                if (result > 0) {

                                    // (4) GrnDetail table
                                    for (GrnDetailDTO grnDetail : grnItemDetails) {
                                        pstmGrnDetail = con.prepareStatement("INSERT INTO grn_detail VALUES(?,?,?,?,?,?,?)");
                                        pstmGrnDetail.setObject(1, grnDetail.getGrnDetailId());
                                        pstmGrnDetail.setObject(2, grnDetail.getItemId());
                                        pstmGrnDetail.setObject(3, grnDetail.getBatchId());
                                        pstmGrnDetail.setObject(4, grnDetail.getBatchQty());
                                        pstmGrnDetail.setObject(5, grnDetail.getSellingPrice());
                                        pstmGrnDetail.setObject(6, grnDetail.getBuyingPrice());
                                        if (grnDetail.getExpiryDate() != null & !grnDetail.getExpiryDate().isEmpty()) {
                                            pstmGrnDetail.setObject(7, grnDetail.getExpiryDate());
                                        } else {
                                            pstmGrnDetail.setObject(7, null);
                                        }
                                        result = pstmGrnDetail.executeUpdate();
                                        if (result <= 0) {
                                            con.rollback();
                                            break;
                                        } else if (grnAuthorizedStatus && oldQtys.get(grnDetail.getItemId()) != null) {
                                            boolean success = false;        // a temp local variable for holding the update & createion process result

                                            // First, let's get item code
                                            MainSubItemDAO mainSubItemDAO = (MainSubItemDAO) DAOFactory.getInstance().getDAO(DAOType.MAIN_SUB_ITEM);
                                            MainSubItemDTO mainSubItemDTO = mainSubItemDAO.readMainSubItemByPK(grnDetail.getItemId());

                                            // Then, let's get the current stock
                                            ItemDetailsDAO itemDetailsDAO = (ItemDetailsDAO) DAOFactory.getInstance().getDAO(DAOType.ITEM_DETAILS);
                                            int currentQty = itemDetailsDAO.readCurrentQty(mainSubItemDTO.getItemCode());

                                            // Calculating new qty...
                                            currentQty = currentQty - oldQtys.get(grnDetail.getItemId());
                                            if (currentQty < 0) {
                                                currentQty = 0;
                                            }

                                            success = itemDetailsDAO.updateCurrentQty(con, mainSubItemDTO.getItemCode(), currentQty);

                                            if (!success) {
                                                con.rollback();
                                                result = 0;
                                                break;
                                            }
                                        } //                                            // (5) ItemDetails table
                                        //                                            PreparedStatement pstm = con.prepareStatement("SELECT item_code FROM main_sub_item WHERE main_sub_item_id = ?; ");
                                        //                                            pstm.setString(1, grnDetail.getItemId());
                                        //                                            ResultSet rstItems = pstm.executeQuery();
                                        //
                                        //                                            if (rstItems.next()) {
                                        //
                                        //                                                Statement stm = con.createStatement();
                                        //                                                ResultSet rstTemp = stm.executeQuery("SELECT main_sub_item_id,batch_qty FROM item_batch_grn_detail WHERE batch_id='" + batchId + "'");
                                        //                                                while (rstTemp.next()) {
                                        //                                                    ItemDetailsDAO itemDetailsDAO = (ItemDetailsDAO) DAOFactory.getInstance().getDAO(DAOType.ITEM_DETAILS);
                                        //                                                    Statement stm1 = con.createStatement();
                                        //                                                    ResultSet rst1 = stm.executeQuery("SELECT item_code FROM main_sub_item WHERE main_sub_item_id='" + rstTemp.getString(1) + "'");
                                        //                                                    int currentQty = itemDetailsDAO.readCurrentQty(rst1.getString(1));
                                        //                                                    currentQty = currentQty - rstTemp.getInt(2);
                                        //                                                    if (currentQty < 0) {
                                        //                                                        currentQty = 0;
                                        //                                                    }
                                        //                                                    PreparedStatement pstmTemp = con.prepareStatement("UPDATE item_details SET qty=?");
                                        //                                                    pstmTemp.setInt(1, currentQty);
                                        //                                                    result = pstmTemp.executeUpdate();
                                        //                                                    if (result == 0) {
                                        //                                                        // Trouble...!
                                        //                                                        con.rollback();
                                        //                                                        result = 0;
                                        //                                                        break;
                                        //                                                    }
                                        //                                                }
                                        //
                                        //                                            } else {
                                        //                                                // We are in trouble... Don't know how we get into here though...!
                                        //                                                con.rollback();
                                        //                                                result = 0;
                                        //                                                break;
                                        //                                            }

                                    }

                                    if (result > 0) {
                                        con.commit();
                                        con.setAutoCommit(true);
                                        con.close();
                                        return result;
                                    }

                                } else {
                                    con.rollback();
                                }

                            } else {
                                con.rollback();
                            }

                        } else {
                            con.rollback();
                        }

                    }
                } else {

                    con.rollback();
                    con.setAutoCommit(false);
                    con.close();
                    return result;
                }

            } else {
                con.rollback();
            }

        } else {
            con.rollback();
        }

        con.setAutoCommit(true);
        con.close();
        return result;

    }

    @Override
    public int authorizeGrn(String grnId) throws ClassNotFoundException, SQLException {

        String batchId = null;

        int result;
        DatabaseResourceConnection dbcon = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        Connection con = dbcon.getConnection();
        con.setAutoCommit(false);

        //============================================================================================
        // First, getting the batch ID...!
        PreparedStatement pstmBatch = con.prepareStatement("SELECT * FROM batch WHERE grn_id=?");
        pstmBatch.setString(1, grnId);
        ResultSet rst = pstmBatch.executeQuery();
        if (rst.next()) {
            batchId = rst.getString(1);
        } else {
            // Which means we are in trouble...!
            con.setAutoCommit(true);
            con.close();
            return -1;
        }

        if (batchId == null) {
            // No batch Id means we are in trouble...!
            con.setAutoCommit(true);
            con.close();
            return -1;
        }

        //===============================================================================================
        // Second, let's copy data from grn_detail to item_batch_grn_detail table
        PreparedStatement pstmGrn = con.prepareStatement("SELECT * FROM grn_detail WHERE batch_id=?");
        pstmGrn.setString(1, batchId);

        rst = pstmGrn.executeQuery();

        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(5);
        nf.setMaximumIntegerDigits(5);
        nf.setGroupingUsed(false);
        String pK = GenerateId.genereateUniqueId(DAOType.ITEM_BATCH_GRN_DETAIL, nf);

        while (rst.next()) {

            PreparedStatement pstmAuthorizedGrn = con.prepareStatement("INSERT INTO item_batch_grn_detail VALUES(?,?,?,?,?,?,?,?)");

            pstmAuthorizedGrn.setObject(1, pK);
            pK = nf.format(Integer.parseInt(pK) + 1);
            pstmAuthorizedGrn.setObject(2, rst.getString(2));
            pstmAuthorizedGrn.setObject(3, rst.getString(3));
            pstmAuthorizedGrn.setObject(4, rst.getInt(4));
            pstmAuthorizedGrn.setObject(5, rst.getString(5));
            pstmAuthorizedGrn.setObject(6, rst.getString(6));
            pstmAuthorizedGrn.setObject(7, rst.getString(7));
            pstmAuthorizedGrn.setObject(8, rst.getInt(4));
            result = pstmAuthorizedGrn.executeUpdate();

            if (result == 0) {

                con.rollback();
                con.setAutoCommit(true);
                con.close();
                return result;

            } else {

                boolean success = false;        // a temp local variable for holding the update & createion process result

                // First, let's get item code
                MainSubItemDAO mainSubItemDAO = (MainSubItemDAO) DAOFactory.getInstance().getDAO(DAOType.MAIN_SUB_ITEM);
                MainSubItemDTO mainSubItemDTO = mainSubItemDAO.readMainSubItemByPK(rst.getString(2));

                // Then, let's check whether there is already record exists in the "item_details" table
                ItemDetailsDAO itemDetailsDAO = (ItemDetailsDAO) DAOFactory.getInstance().getDAO(DAOType.ITEM_DETAILS);
                boolean itemDetailsExists = itemDetailsDAO.isItemDetailsExists(mainSubItemDTO.getItemCode());

                ItemDetailsDTO itemDetailsDTO = new ItemDetailsDTO(mainSubItemDTO.getItemCode(),
                        rst.getInt(4),
                        rst.getDouble(5),
                        rst.getDouble(6));

                if (itemDetailsExists) {
                    // Then we just need to update current entry
                    int currentQty = itemDetailsDAO.readCurrentQty(itemDetailsDTO.getItemCode());
                    itemDetailsDTO.setQty(currentQty + itemDetailsDTO.getQty());
                    success = itemDetailsDAO.updateItemDetails(con, itemDetailsDTO);
                } else {
                    // No entry has been found which means we have to create a new entry
                    success = itemDetailsDAO.createItemDetails(con, itemDetailsDTO);
                }

                if (!success) {
                    con.rollback();
                    con.setAutoCommit(true);
                    con.close();
                    return result;
                }

            }
        }

        //=================================================================================================
        // Finally, Let's delete data from grn_detail
        result = pstmGrn.executeUpdate("DELETE FROM grn_detail WHERE batch_id='" + batchId + "'");
        if (result > 0) {
            con.commit();
            con.setAutoCommit(true);
            con.close();
            return result;
        } else {
            con.rollback();
            con.setAutoCommit(true);
            con.close();
            return result;
        }

    }

}
