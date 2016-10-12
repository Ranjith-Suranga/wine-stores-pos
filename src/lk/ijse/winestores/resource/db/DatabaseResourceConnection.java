/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lk.ijse.winestores.resource.db;

import java.sql.Connection;
import lk.ijse.winestores.resource.ResourceConnection;

/**
 *
 * @author pradin
 */
public interface DatabaseResourceConnection extends ResourceConnection{
    
    public static final String CONNECTION = "jdbc:mysql://localhost:3306/wine_stores";

    @Override
    public Connection getConnection();
    
}
