/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom;

import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.SuperController;

/**
 *
 * @author Ranjith Suranga
 */
public interface SubCategoryController extends SuperController {

    public ArrayList<SubCategoryModel> getAllSubCategoriesByMajorCategoryName(String majorCategoryName) throws ClassNotFoundException, SQLException;

    public int saveSubCategory(String majorCateogryName, String subCategoryName) throws ClassNotFoundException, SQLException;

    public ArrayList<SubCategoryModel> getAllSubCategories() throws ClassNotFoundException, SQLException;

    public int changeSubCategory(String subCategoryId, String subCategoryNewName) throws ClassNotFoundException, SQLException;

    public boolean removeSubCategory(String majorCategoryName, String subCategoryId) throws ClassNotFoundException, SQLException;

    public class SubCategoryModel {

        private String subCategoryId;
        private String subCategoryName;

        public SubCategoryModel() {
        }

        public SubCategoryModel(String subCategoryId, String subCategoryName) {
            this.subCategoryId = subCategoryId;
            this.subCategoryName = subCategoryName;
        }

        /**
         * @return the subCategoryId
         */
        public String getSubCategoryId() {
            return subCategoryId;
        }

        /**
         * @param subCategoryId the subCategoryId to set
         */
        public void setSubCategoryId(String subCategoryId) {
            this.subCategoryId = subCategoryId;
        }

        /**
         * @return the subCategoryName
         */
        public String getSubCategoryName() {
            return subCategoryName;
        }

        /**
         * @param subCategoryName the subCategoryName to set
         */
        public void setSubCategoryName(String subCategoryName) {
            this.subCategoryName = subCategoryName;
        }

    }

}
