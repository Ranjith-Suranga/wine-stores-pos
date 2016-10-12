/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.dto.MainSubCategoryDTO;
import lk.ijse.winestores.dao.dto.MainSubItemDTO;
import lk.ijse.winestores.dao.dto.MajorCategoryDTO;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;

/**
 *
 * @author Ranjith Suranga
 */
public interface MainSubCategoryDAO extends SuperDAO {

    public static final String MAIN_SUB_CATEGORY_INSERTION_SQL = "INSERT INTO main_sub_category VALUES(?,?,?);";
    public static final String MAIN_SUB_CATEGORY_UPDATE_SQL = "UPDATE main_sub_category SET main_sub_category_id=?, main_cat_id=?, sub_category_id=? WHERE main_sub_category_id=?;";
    public static final String MAIN_SUB_CATEGORY_DELETION_SQL = "DELETE FROM main_sub_category WHERE main_sub_category_id=? ;";
    public static final String MAIN_SUB_CATEGORY_CHECK_EXISTENCY_BY_ID = "SELECT * FROM main_sub_category WHERE main_sub_category_id=? ;";
    public static final String READ_ALL_SUB_CATEGORIES_BY_MAJOR_CATEGORY_ID = "SELECT `sub_category`.sub_category_id,`sub_category`.sub_category_name FROM main_sub_category JOIN sub_category ON main_sub_category.sub_category_id = sub_category.sub_category_id WHERE main_sub_category.main_cat_id = ? ORDER BY sub_category_name; ";
    public static final String MAIN_SUB_CATEGORY_READ_LAST_ID_SQL = "SELECT * FROM main_sub_category ORDER BY main_sub_category_id DESC LIMIT 1;";
    public static final String READ_ALL_MAJOR_CATEGORIES_BY_SUB_CATEGORY_ID = "SELECT main_category.main_cat_id, main_category.category_name FROM main_category JOIN main_sub_category ON main_category.main_cat_id = main_sub_category.main_cat_id WHERE main_sub_category.sub_category_id=?";

    public int createMainSubCategory(MainSubCategoryDTO dto) throws ClassNotFoundException, SQLException;

    public int updateMainSubCategory(MainSubCategoryDTO dto) throws ClassNotFoundException, SQLException;

    public int deleteMainSubCategory(MainSubCategoryDTO dto) throws ClassNotFoundException, SQLException;
    
    public MainSubCategoryDTO readMainSubCategory(String mainCategoryId, String subCategoryId) throws ClassNotFoundException, SQLException;

    public ArrayList<SubCategoryDTO> readAllSubCategoriesByMajorCategoryId(String majorCategoryId) throws ClassNotFoundException, SQLException;

    public ArrayList<MajorCategoryDTO> readAllMajorCategoriesBySubCategoryId(String subCategoryId) throws ClassNotFoundException, SQLException;
}
