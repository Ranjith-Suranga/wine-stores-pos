/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom.impl;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.MainSubCategoryDAO;
import lk.ijse.winestores.dao.custom.MajorCategoryDAO;
import lk.ijse.winestores.dao.custom.SubCategoryDAO;
import lk.ijse.winestores.dao.dto.MainSubCategoryDTO;
import lk.ijse.winestores.dao.dto.MajorCategoryDTO;
import lk.ijse.winestores.dao.dto.SubCategoryDTO;
import lk.ijse.winestores.service.custom.SubCategoryService;
import lk.ijse.winestores.service.idgenerator.GenerateId;

/**
 *
 * @author Ranjith Suranga
 */
public class SubCategoryServiceImpl implements SubCategoryService {

    @Override
    public ArrayList<SubCategoryDTO> getAllSubCategoriesByMajorCategoryName(String MajorCategoryName) throws ClassNotFoundException, SQLException {
        MajorCategoryDAO majorDAO = (MajorCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAJOR_CATEGORY);
        MajorCategoryDTO majorCategory = majorDAO.readMajorCategoryByName(MajorCategoryName);
        if (majorCategory != null) {
            MainSubCategoryDAO dao = (MainSubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_CATEGORY);
            return dao.readAllSubCategoriesByMajorCategoryId(majorCategory.getMainCatId());
        }
        return null;
    }

    @Override
    public int saveSubCateogry(String majorCateogryName, String subCateogryName) throws ClassNotFoundException, SQLException {

        String subCategoryId = null;

        SubCategoryDAO subDAO = (SubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUB_CATEGORY);
        SubCategoryDTO subCategoryDTO = subDAO.readSubCategoryByName(subCateogryName);

        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setMinimumIntegerDigits(2);
        numberFormat.setMaximumIntegerDigits(2);
        numberFormat.setGroupingUsed(false);

        // Check for the existency of the sub category
        if (subCategoryDTO == null) {
            subCategoryId = GenerateId.getNewId(SuperDAO.DAOType.SUB_CATEGORY, null, numberFormat);
            subCategoryDTO = new SubCategoryDTO(subCategoryId, subCateogryName);
            subDAO.createSubCategory(subCategoryDTO);
        } else {
            subCategoryId = subCategoryDTO.getSubCategoryId();
        }

        MajorCategoryDAO majorDAO = (MajorCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAJOR_CATEGORY);
        MajorCategoryDTO majorCategoryDTO = majorDAO.readMajorCategoryByName(majorCateogryName);

        MainSubCategoryDAO dao = (MainSubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_CATEGORY);
        MainSubCategoryDTO dto = new MainSubCategoryDTO(GenerateId.getNewId(SuperDAO.DAOType.MAIN_SUB_CATEGORY, null, numberFormat), majorCategoryDTO.getMainCatId(), subCategoryId);
        return dao.createMainSubCategory(dto);
    }

    @Override
    public ArrayList<SubCategoryDTO> getAllSubCategories() throws ClassNotFoundException, SQLException {
        SubCategoryDAO subDAO = (SubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUB_CATEGORY);
        return subDAO.readAllSubCategories();
    }

    @Override
    public int changeSubCategory(String subCategoryId, String subCategoryNewName) throws ClassNotFoundException, SQLException {
        MainSubCategoryDAO mainSubDAO = (MainSubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAIN_SUB_CATEGORY);
        ArrayList<MajorCategoryDTO> allMajorCategories = mainSubDAO.readAllMajorCategoriesBySubCategoryId(subCategoryId);

        // Checking whether this sub category is also in other major category
        if (allMajorCategories.size() > 1) {
            return -100;    // -100 indicates this can't be edit, because there are dependices...!
        } else {
            SubCategoryDAO subDAO = (SubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUB_CATEGORY);
            SubCategoryDTO dto = new SubCategoryDTO(subCategoryId, subCategoryNewName);
            return subDAO.updateSubCategory(dto);
        }

    }

    @Override
    public boolean removeSubCategory(String majorCategoryName, String subCategoryId) throws ClassNotFoundException, SQLException {
        SubCategoryDAO subDAO = (SubCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.SUB_CATEGORY);
        MajorCategoryDAO majorDAO = (MajorCategoryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.MAJOR_CATEGORY);
        return subDAO.deleteSubCategory(majorDAO.readMajorCategoryByName(majorCategoryName).getMainCatId(), subCategoryId);
    }

}
