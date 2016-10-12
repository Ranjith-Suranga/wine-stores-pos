/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface SubCategoryDAO extends SuperDAO {
    
    public static final String TABLE_NAME = "sub_category";
    public static final String SUB_CATEGORY_INSERTION_SQL = "INSERT INTO sub_category VALUES(?,?);";
    public static final String SUB_CATEGORY_UPDATE_SQL = "UPDATE sub_category SET sub_category_id=?, sub_category_name=? WHERE sub_category_id=?;";
    public static final String SUB_CATEGORY_DELETION_SQL = "DELETE FROM sub_category WHERE sub_category_id=? ;";
    public static final String SUB_CATEGORY_CHECK_EXISTENCY_BY_ID = "SELECT * FROM sub_category WHERE sub_category_id=? ;";
    public static final String SUB_CATEGORY_READ_LAST_ID_SQL = "SELECT * FROM sub_category ORDER BY sub_category_id DESC LIMIT 1;";
    public static final String SUB_CATEGORY_READ_BY_NAME = "SELECT * FROM sub_category WHERE sub_category_name=?";
    
    public int createSubCategory(SubCategoryDTO dto) throws ClassNotFoundException, SQLException;

    public int updateSubCategory(SubCategoryDTO dto) throws ClassNotFoundException, SQLException;

    public boolean deleteSubCategory(String subCategoryId) throws ClassNotFoundException, SQLException;
    
    public SubCategoryDTO readSubCategoryByName(String subCateogryName) throws ClassNotFoundException, SQLException;
    
    public ArrayList<SubCategoryDTO> readAllSubCategories()throws ClassNotFoundException, SQLException;
}
