/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.controller.custom;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.impl.MajorCategoryControllerImpl;

/**
 *
 * @author Ranjith Suranga
 */
public interface MajorCategoryController extends SuperController {

    public int saveMajorCateogry(String categoryName) throws ClassNotFoundException, MySQLIntegrityConstraintViolationException, SQLException;

    public int changeMajorCategory(String categoryId, String categoryName) throws ClassNotFoundException, SQLException;
    
    public boolean removeMajorCategory(String categoryId)throws ClassNotFoundException, SQLException;
    
    public ArrayList<MajorCategoryControllerImpl.MajorCategoryModel> getAllMajorCategory() throws ClassNotFoundException, SQLException, NullPointerException;

}
