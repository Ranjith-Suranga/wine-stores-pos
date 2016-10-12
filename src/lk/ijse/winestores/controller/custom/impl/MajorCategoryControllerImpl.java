/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom.impl;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.custom.MajorCategoryController;
import lk.ijse.winestores.dao.dto.MajorCategoryDTO;
import lk.ijse.winestores.service.ServiceFactory;
import lk.ijse.winestores.service.SuperService;
import lk.ijse.winestores.service.custom.MajorCategoryService;

/**
 *
 * @author Ranjith Suranga
 */
public class MajorCategoryControllerImpl implements MajorCategoryController {

    @Override
    public ArrayList<MajorCategoryModel> getAllMajorCategory() throws ClassNotFoundException, SQLException, NullPointerException {
        ArrayList<MajorCategoryModel> al = new ArrayList<>();

        MajorCategoryService service = (MajorCategoryService) ServiceFactory.getInstance().getService(SuperService.ServiceType.MAJOR_CATEGORY);
        ArrayList<MajorCategoryDTO> allMajorCategory = service.getAllMajorCategory();
        
        if (allMajorCategory == null) throw new NullPointerException();

        for (MajorCategoryDTO majorCategoryDTO : allMajorCategory) {
            MajorCategoryModel model = new MajorCategoryModel(majorCategoryDTO.getMainCatId(),majorCategoryDTO.getCategoryName());
            al.add(model);
        }
        
        return al;
    }

    @Override
    public int saveMajorCateogry(String categoryName) throws ClassNotFoundException,MySQLIntegrityConstraintViolationException, SQLException  {
        MajorCategoryDTO dto = new MajorCategoryDTO();
        dto.setCategoryName(categoryName);
        
        MajorCategoryService service = (MajorCategoryService) ServiceFactory.getInstance().getService(SuperService.ServiceType.MAJOR_CATEGORY);
        return service.saveMajorCategory(dto);
    }

    @Override
    public int changeMajorCategory(String categoryId, String categoryName) throws ClassNotFoundException, SQLException {
        MajorCategoryDTO dto = new MajorCategoryDTO(categoryId, categoryName);
        MajorCategoryService service = (MajorCategoryService) ServiceFactory.getInstance().getService(SuperService.ServiceType.MAJOR_CATEGORY);
        return service.changeMajorCategory(dto);
    }

    @Override
    public boolean removeMajorCategory(String categoryId) throws ClassNotFoundException, SQLException {
        
        MajorCategoryService service = (MajorCategoryService) ServiceFactory.getInstance().getService(SuperService.ServiceType.MAJOR_CATEGORY);
        return service.removeMajorCategory(categoryId);
        
    }

    public class MajorCategoryModel {

        private String mainCatId;
        private String categoryName;

        public MajorCategoryModel() {
        }

        public MajorCategoryModel(String mainCatId, String categoryName) {
            this.mainCatId = mainCatId;
            this.categoryName = categoryName;
        }
        
        /**
         * @return the mainCatId
         */
        public String getMainCatId() {
            return mainCatId;
        }

        /**
         * @param mainCatId the mainCatId to set
         */
        public void setMainCatId(String mainCatId) {
            this.mainCatId = mainCatId;
        }

        /**
         * @return the categoryName
         */
        public String getCategoryName() {
            return categoryName;
        }

        /**
         * @param categoryName the categoryName to set
         */
        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }
}
