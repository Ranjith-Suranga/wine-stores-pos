/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.MajorCategoryDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface MajorCategoryDAO extends SuperDAO {

    public static final String TABLE_NAME = "main_category";
    public static final String MAJOR_CATEGORY_INSERTION_SQL = "INSERT INTO main_category VALUES(?,?);";
    public static final String MAJOR_CATEGORY_UPDATE_SQL = "UPDATE main_category SET main_cat_id=?, category_name=? WHERE main_cat_id=?;";
    public static final String MAJOR_CATEGORY_DELETION_SQL = "DELETE FROM main_category WHERE main_cat_id=? ;";
    public static final String MAJOR_CATEGORY_READ_SQL = "SELECT * FROM main_category WHERE main_cat_id=? ;";
//    public static final String MAJOR_CATEGORY_CHECK_EXISTENCY = "SELECT * FROM main_category WHERE ?=? ;";
    public static final String MAJOR_CATEGORY_CHECK_EXISTENCY_BY_ID = "SELECT * FROM main_category WHERE main_cat_id=? ;";
    public static final String MAJOR_CATEGORY_READ_LAST_ID_SQL = "SELECT * FROM main_category ORDER BY main_cat_id DESC LIMIT 1;";

    public int createMajorCategory(MajorCategoryDTO dto) throws ClassNotFoundException,MySQLIntegrityConstraintViolationException, SQLException;

    public int updateMajorCategory(MajorCategoryDTO dto) throws ClassNotFoundException, SQLException;

    public boolean deleteMajorCategory(String categoryId) throws ClassNotFoundException, SQLException;

    public MajorCategoryDTO readMajorCategoryById(String queryId) throws ClassNotFoundException, SQLException;
    
    public MajorCategoryDTO readMajorCategoryByName(String majorCategoryName)throws ClassNotFoundException, SQLException;
    
    public ArrayList<MajorCategoryDTO> readAllMajorCategory() throws ClassNotFoundException, SQLException;

    //public boolean isMajorCategory(String queryId)throws ClassNotFoundException, SQLException;
}
