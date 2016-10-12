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
public class EmptyBottleDTO {

    private String bottleType;
    private double cost;

    public EmptyBottleDTO() {
    }

    public EmptyBottleDTO(String bottleType, double cost) {
        this.bottleType = bottleType;
        this.cost = cost;
    }
    
    /**
     * @return the bottleType
     */
    public String getBottleType() {
        return bottleType;
    }

    /**
     * @param bottleType the bottleType to set
     */
    public void setBottleType(String bottleType) {
        this.bottleType = bottleType;
    }

    /**
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(double cost) {
        this.cost = cost;
    }
}
