/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.resource;

/**
 *
 * @author pradin
 */
public interface ResourceConnection {

    public enum ResourceConnectionType {
        DATABSE, FILE, SINGELTON_DATABASE_CONNECTION
    }

    public Object getConnection();

}
