/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views.util;

import java.math.BigDecimal;

/**
 *
 * @author Ranjith Suranga
 */
public interface CashTendered {
    
    public void processOrder(BigDecimal cashTendered);
    
}
