/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.Connection;
import java.sql.SQLException;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.IdPoolDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface IdPoolDAO extends SuperDAO {

    public static final String TABLE_NAME = "id_pool";
    public static final String ID_POOL_INSERTION_SQL = "INSERT INTO id_pool VALUES(?,?);";
    public static final String ID_POOL_UPDATE_SQL = "UPDATE id_pool SET old_id=?, table_name=? WHERE old_id=?;";
    public static final String ID_POOL_DELETION_SQL = "DELETE FROM id_pool WHERE old_id=? AND table_name=? ;";
    public static final String ID_POOL_CHECK_EXISTENCY_BY_TABLE_NAME = "SELECT * FROM id_pool WHERE table_name=? ;";

    public int createID(IdPoolDTO dto) throws ClassNotFoundException, SQLException;
    
    public int createID(Connection con, IdPoolDTO dto) throws ClassNotFoundException, SQLException;

    public int updateID(IdPoolDTO dto) throws ClassNotFoundException, SQLException;

    public int deleteID(IdPoolDTO dto) throws ClassNotFoundException, SQLException;

    /*
     * This method checks for the ID existency and then return the id, if there is one, otherwise it will return null;
    */
    public String isIDExists(DAOType table) throws ClassNotFoundException, SQLException;

//    public MajorCategoryDTO readAllIDs(String queryId) throws ClassNotFoundException, SQLException;
//    public ArrayList<IdPoolDTO> readAllIds() throws ClassNotFoundException, SQLException;
}
