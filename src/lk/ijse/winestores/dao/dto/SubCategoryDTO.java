/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.dao.dto;

/**
 *
 * @author Ranjith Suranga
 */
public class SubCategoryDTO {

    private String subCategoryId;
    private String subCategoryName;

    public SubCategoryDTO() {
    }

    public SubCategoryDTO(String subCategoryId, String subCategoryName) {
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
