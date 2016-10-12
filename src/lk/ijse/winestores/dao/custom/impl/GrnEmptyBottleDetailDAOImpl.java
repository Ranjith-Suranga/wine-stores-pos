/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.custom.GrnEmptyBottleDetailDAO;
import lk.ijse.winestores.dao.dto.GrnEmptyBottleDetailDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.resource.db.DatabaseResourceConnection;

/**
 *
 * @author Ranjith Suranga
 */
public class GrnEmptyBottleDetailDAOImpl implements GrnEmptyBottleDetailDAO {

    /*
    =============================
        ID Generator Methods
    =============================
     */
    @Override
    public boolean isExists(String queryId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(GRN_EMPTY_BOTTLE_CHECK_EXISTENCY_BY_ID);
        pstm.setObject(1, queryId);
        ResultSet rst = pstm.executeQuery();
        boolean result = rst.next();
        con.getConnection().close();
        return result;
    }

    @Override
    public String readLastId() throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement(GRN_EMPTY_BOTTLE_READ_LAST_ID_SQL);
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
    public ArrayList<GrnEmptyBottleDetailDTO> readEmptyBottleDetails(String grnId) throws ClassNotFoundException, SQLException {
        DatabaseResourceConnection con = (DatabaseResourceConnection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE);
        PreparedStatement pstm = con.getConnection().prepareStatement("SELECT * FROM wine_stores.grn_empty_bottle_detail WHERE grn_id = ?;");
        pstm.setObject(1, grnId);
        ResultSet rst = pstm.executeQuery();

        ArrayList<GrnEmptyBottleDetailDTO> al = null;

        while (rst.next()) {
            if (al == null) {
                al = new ArrayList<>();
            }

            GrnEmptyBottleDetailDTO dto = new GrnEmptyBottleDetailDTO(rst.getString(1),
                    rst.getInt(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6));

            al.add(dto);
        }
        con.getConnection().close();
        return al;
    }

}
