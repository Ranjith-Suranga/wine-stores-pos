/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.ItemDetailsDAO;
import lk.ijse.winestores.dao.custom.MainSubItemDAO;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.service.custom.NewItemService;

/**
 *
 * @author Ranjith Suranga
 */
public class NewItemServiceImpl implements NewItemService {

    // Dependencies
    private ItemDetailsDAO dao;
    private MainSubItemDAO mainSubItemDAO;

    // Depenedecy Injection
    public NewItemServiceImpl() {
        dao = (ItemDetailsDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.ITEM_DETAILS);
        mainSubItemDAO = (MainSubItemDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_ITEM);
    }

    @Override
    public ArrayList<CustomItemDetailsDTO> getAllItems() throws ClassNotFoundException, SQLException {
        Connection con = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        dao.setConnection(con);
        ArrayList<CustomItemDetailsDTO> allItems = dao.readAllItems();
        con.close();
        return allItems;
    }

    @Override
    public boolean saveItem(ItemDetailsDTO dto) throws ClassNotFoundException, SQLException {
        Connection con = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        dao.setConnection(con);

        boolean success;
        if (dao.isItemDetailsExists(dto.getItemCode())) {
            success = dao.updateItemDetails(con, dto);
        } else {
            success = dao.createItemDetails(con, dto);
        }

        con.close();
        return success;
    }

    @Override
    public int getQty(String itemCode) throws ClassNotFoundException, SQLException {
        Connection con = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        dao.setConnection(con);
        
        int qty = dao.readCurrentQty(itemCode);
        
        con.close();
        
        return qty;
    }

    @Override
    public boolean saveBarCode(String itemCode, String barCode) throws ClassNotFoundException, SQLException {
        
        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        mainSubItemDAO.setConnection(connection);
        boolean success =  mainSubItemDAO.updateBarCode(itemCode, barCode);
        connection.close();
        return success;
    }

}
