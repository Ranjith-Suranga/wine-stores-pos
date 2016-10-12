/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.CustomItemDetailsDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface CustomDAO extends SuperDAO{

    public enum ItemQueryType {
        BARCODE, ITEM_CODE, ITEM_NAME, SELLING_PRICE
    }

    public final String GET_ITEMS_SQL = "SELECT main_sub_item.barcode, item_details.item_code, item.item_name, item_details.qty, item_details.selling_price FROM item_details INNER JOIN main_sub_item ON main_sub_item.item_code = item_details.item_code INNER JOIN item ON item.item_id = main_sub_item.item_id WHERE ";

    public ArrayList<CustomItemDetailsDTO> readItems(ItemQueryType queryType, String queryWord) throws ClassNotFoundException, SQLException;

}
