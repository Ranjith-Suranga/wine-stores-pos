/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;
import lk.ijse.winestores.service.SuperService;

/**
 *
 * @author Ranjith Suranga
 */
public interface SubCategoryService extends SuperService{
    
    public ArrayList<SubCategoryDTO> getAllSubCategoriesByMajorCategoryName(String MajorCategoryName)throws ClassNotFoundException, SQLException;
    
    public int saveSubCateogry(String majorCateogryName, String subCateogryName) throws ClassNotFoundException, SQLException;
    
    public int changeSubCategory( String subCategoryId , String subCategoryNewName) throws ClassNotFoundException, SQLException;
    
    public ArrayList<SubCategoryDTO> getAllSubCategories()throws ClassNotFoundException, SQLException;
    
    public boolean removeSubCategory(String categoryId) throws ClassNotFoundException, SQLException;
    
}
