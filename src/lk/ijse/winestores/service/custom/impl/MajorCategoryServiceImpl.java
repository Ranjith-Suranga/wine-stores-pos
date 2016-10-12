/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom.impl;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.MajorCategoryDAO;
import lk.ijse.winestores.dao.dto.MajorCategoryDTO;
import lk.ijse.winestores.service.custom.MajorCategoryService;
import lk.ijse.winestores.service.idgenerator.GenerateId;

/**
 *
 * @author Ranjith Suranga
 */
public class MajorCategoryServiceImpl implements MajorCategoryService {

    @Override
    public int saveMajorCategory(MajorCategoryDTO dto) throws ClassNotFoundException,MySQLIntegrityConstraintViolationException, SQLException {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setMinimumIntegerDigits(2);
        numberFormat.setMaximumIntegerDigits(2);
        numberFormat.setGroupingUsed(false);
        MajorCategoryDAO dao = (MajorCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAJOR_CATEGORY);
        dto.setMainCatId(GenerateId.getNewId(SuperDAO.DAOType.MAJOR_CATEGORY, null, numberFormat));
        return dao.createMajorCategory(dto);
    }

    @Override
    public ArrayList<MajorCategoryDTO> getAllMajorCategory() throws ClassNotFoundException, SQLException {
        MajorCategoryDAO dao = (MajorCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAJOR_CATEGORY);
        ArrayList<MajorCategoryDTO> allMajorCategory = dao.readAllMajorCategory();
        return allMajorCategory;
    }

    @Override
    public int changeMajorCategory(MajorCategoryDTO dto) throws ClassNotFoundException, SQLException {
        MajorCategoryDAO dao = (MajorCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAJOR_CATEGORY);
        return dao.updateMajorCategory(dto);
    }

    @Override
    public boolean removeMajorCategory(String categoryId) throws ClassNotFoundException, SQLException {
        MajorCategoryDAO dao = (MajorCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAJOR_CATEGORY);
        return dao.deleteMajorCategory(categoryId);        
    }

}
