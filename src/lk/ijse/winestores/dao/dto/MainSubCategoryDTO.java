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
public class MainSubCategoryDTO {
    
    private String mainSubCategoryId;
    private String mainCatId;
    private String subCategoryId;

    public MainSubCategoryDTO() {
    }

    public MainSubCategoryDTO(String mainSubCategoryId, String mainCatId, String subCategoryId) {
        this.mainSubCategoryId = mainSubCategoryId;
        this.mainCatId = mainCatId;
        this.subCategoryId = subCategoryId;
    }

    /**
     * @return the mainSubCategoryId
     */
    public String getMainSubCategoryId() {
        return mainSubCategoryId;
    }

    /**
     * @param mainSubCategoryId the mainSubCategoryId to set
     */
    public void setMainSubCategoryId(String mainSubCategoryId) {
        this.mainSubCategoryId = mainSubCategoryId;
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
    
}
