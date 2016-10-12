/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.resource;

import java.sql.SQLException;
import lk.ijse.winestores.resource.db.impl.DatabaseResourceConnectionImpl;
import lk.ijse.winestores.resource.db.impl.DatabaseResourceConnectionSingeltonImpl;

/**
 *
 * @author pradin
 */
public class ResourceFactory {

    private static ResourceFactory resourceFactory;

    private ResourceFactory() {
    }

    public static ResourceFactory getInstance() {
        return (null != resourceFactory) ? resourceFactory : (resourceFactory = new ResourceFactory());
    }

    public ResourceConnection getResourceConnection(ResourceConnection.ResourceConnectionType resourceConnectionType) throws ClassNotFoundException, SQLException  {
        
        switch (resourceConnectionType) {
            case DATABSE:
                return new DatabaseResourceConnectionImpl();
            case FILE:
                break;
            case SINGELTON_DATABASE_CONNECTION:
                return new DatabaseResourceConnectionSingeltonImpl();
        }
        return null;
        
    }

}
