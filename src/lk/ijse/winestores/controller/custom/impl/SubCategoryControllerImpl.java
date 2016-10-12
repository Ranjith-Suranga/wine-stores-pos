/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.custom.SubCategoryController;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;
import lk.ijse.winestores.service.ServiceFactory;
import lk.ijse.winestores.service.SuperService;
import lk.ijse.winestores.service.custom.SubCategoryService;

/**
 *
 * @author Ranjith Suranga
 */
public class SubCategoryControllerImpl implements SubCategoryController {

    @Override
    public ArrayList<SubCategoryModel> getAllSubCategoriesByMajorCategoryName(String majorCategoryName) throws ClassNotFoundException, SQLException {
        SubCategoryService service = (SubCategoryService) ServiceFactory.getInstance().getService(SuperService.ServiceType.SUB_CATEGORY);
        ArrayList<SubCategoryDTO> allSubCategoriesByMajorCategoryName = service.getAllSubCategoriesByMajorCategoryName(majorCategoryName);

        if (allSubCategoriesByMajorCategoryName == null) {
            return null;
        }

        ArrayList<SubCategoryModel> al = new ArrayList<>();

        for (SubCategoryDTO subCategoryDTO : allSubCategoriesByMajorCategoryName) {
            SubCategoryModel model = new SubCategoryModel(subCategoryDTO.getSubCategoryId(), subCategoryDTO.getSubCategoryName());
            al.add(model);
        }

        return al;
    }

    @Override
    public int saveSubCategory(String majorCateogryName, String subCategoryName) throws ClassNotFoundException, SQLException {
        SubCategoryService service = (SubCategoryService) ServiceFactory.getInstance().getService(SuperService.ServiceType.SUB_CATEGORY);
        return service.saveSubCateogry(majorCateogryName, subCategoryName);
    }

    @Override
    public ArrayList<SubCategoryModel> getAllSubCategories() throws ClassNotFoundException, SQLException {
        SubCategoryService service = (SubCategoryService) ServiceFactory.getInstance().getService(SuperService.ServiceType.SUB_CATEGORY);
        ArrayList<SubCategoryDTO> allSubCategories = service.getAllSubCategories();

        if (allSubCategories == null) {
            return null;
        }

        ArrayList<SubCategoryModel> al = new ArrayList<>();

        for (SubCategoryDTO subCategory : allSubCategories) {
            
            SubCategoryModel model = new SubCategoryModel(subCategory.getSubCategoryId(), subCategory.getSubCategoryName());
            al.add(model);

        }
        
        return al;
    }

    @Override
    public int changeSubCategory( String subCategoryId, String subCategoryNewName) throws ClassNotFoundException, SQLException {
        SubCategoryService service = (SubCategoryService) ServiceFactory.getInstance().getService(SuperService.ServiceType.SUB_CATEGORY);
        return service.changeSubCategory(subCategoryId, subCategoryNewName);
    }

    @Override
    public boolean removeSubCategory(String categoryId) throws ClassNotFoundException, SQLException {
        SubCategoryService service = (SubCategoryService) ServiceFactory.getInstance().getService(SuperService.ServiceType.SUB_CATEGORY);
        return service.removeSubCategory(categoryId);
    }

}
