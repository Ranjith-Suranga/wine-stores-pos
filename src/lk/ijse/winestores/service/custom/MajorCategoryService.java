/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import lk.ijse.winestores.dao.dto.MajorCategoryDTO;
import lk.ijse.winestores.service.SuperService;

/**
 *
 * @author Ranjith Suranga
 */
public interface MajorCategoryService extends SuperService {

    public int saveMajorCategory(MajorCategoryDTO dto) throws ClassNotFoundException, MySQLIntegrityConstraintViolationException, SQLException;

    public int changeMajorCategory(MajorCategoryDTO dto) throws ClassNotFoundException, SQLException;

    public ArrayList<MajorCategoryDTO> getAllMajorCategory() throws ClassNotFoundException, SQLException;
    
    public boolean removeMajorCategory(String categoryId) throws ClassNotFoundException, SQLException;

}
