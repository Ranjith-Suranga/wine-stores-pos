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
public class MajorCategoryDTO {
    
    private String mainCatId;
    private String categoryName;

    public MajorCategoryDTO() {
    }

    public MajorCategoryDTO(String mainCatId, String categoryName) {
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
