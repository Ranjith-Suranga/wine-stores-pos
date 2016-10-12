/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.resource.db.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import lk.ijse.winestores.resource.db.DatabaseResourceConnection;

/**
 *
 * @author pradin
 */
public class DatabaseResourceConnectionImpl implements DatabaseResourceConnection {

    private Connection dbConnection;

    public DatabaseResourceConnectionImpl() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        File dbFile = new File("settings/db_settings.txt");
        String mySQLUser = null;
        String mySQLPwd = null;
        try {
            FileReader reader = new FileReader(dbFile);

            BufferedReader br = new BufferedReader(reader);

            String line;
            String txt = "";
            while ((line = br.readLine()) != null) {
                txt = txt + line + "\n";
            }
            
            mySQLUser = txt.split("\\n")[0];
            mySQLPwd = txt.split("\\n")[1];
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseResourceConnectionImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseResourceConnectionImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        dbConnection = DriverManager.getConnection(CONNECTION, mySQLUser, mySQLPwd);
    }

    @Override
    public Connection getConnection() {
        return dbConnection;
    }

}
