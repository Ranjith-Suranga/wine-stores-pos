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
public class IdPoolDTO {
    private String oldId;
    private String tableName;

    public IdPoolDTO() {
    }

    public IdPoolDTO(String oldId, String tableName) {
        this.oldId = oldId;
        this.tableName = tableName;
    }

    /**
     * @return the oldId
     */
    public String getOldId() {
        return oldId;
    }

    /**
     * @param oldId the oldId to set
     */
    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    
}
