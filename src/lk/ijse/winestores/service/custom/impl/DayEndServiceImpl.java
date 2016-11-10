/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.DayEndDAO;
import lk.ijse.winestores.dao.custom.QueryDAO;
import lk.ijse.winestores.dao.dto.DayEndDTO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.service.custom.DayEndService;

/**
 *
 * @author Ranjith Suranga
 */
public class DayEndServiceImpl implements DayEndService {

    // Dependencis
    private DayEndDAO daoDayEnd;
    private QueryDAO daoQuery;

    // Dependency Injection
    public DayEndServiceImpl() {
        daoDayEnd = (DayEndDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.DAY_END);
        daoQuery = (QueryDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.QUERY);
    }

    @Override
    public boolean finalizeDayEnd(Date date) throws ClassNotFoundException, SQLException  {

        boolean success = false;
        Date lastDayEndDate = daoQuery.readLastDayEnd();
        date = new java.sql.Date(date.getTime());

        Connection connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.DATABSE).getConnection();
        daoDayEnd.setConnection(connection);

        main_loop:
        while (lastDayEndDate.compareTo(date) < 0) {

            ArrayList<ItemDetailsDTO> itemDetails = daoQuery.readAllItemDetails();
            if (itemDetails == null) {
                break;
            }

            Date oldDate = lastDayEndDate;
            lastDayEndDate = new java.sql.Date(lastDayEndDate.getTime() + 24 * 60 * 60 * 1000);            
            
            for (ItemDetailsDTO itemDetail : itemDetails) {
                
                DayEndDTO lastDayEndDTO = null;
                int openingQty = ((lastDayEndDTO=daoQuery.readDayEnd(oldDate, itemDetail.getItemCode()))==null) ? 0 : ( lastDayEndDTO.getOpeningQty() + lastDayEndDTO.getGrnQty() - lastDayEndDTO.getSalesQty());
                
                DayEndDTO dto = new DayEndDTO(lastDayEndDate,
                        itemDetail.getItemCode(),
                        openingQty,
                        daoQuery.readGRNQty(lastDayEndDate, itemDetail.getItemCode()),
                        daoQuery.readSalesQty(lastDayEndDate, itemDetail.getItemCode()),
                        BigDecimal.valueOf(itemDetail.getSellingPrice()).setScale(2),
                        BigDecimal.valueOf(itemDetail.getBuyingPrice()).setScale(2));
                
                success = daoDayEnd.create(dto);
                if (!success){
                    break main_loop;
                }
            }

            //lastDayEndDate = daoQuery.readLastDayEnd();
            //lastDayEndDate = new java.sql.Date(lastDayEndDate.getTime() + 24 * 60 * 60 * 1000);
        }

        connection.close();
        return success;

    }

    public Date finishedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(date) + " 23:59:59";
        sdf.applyPattern("yyyy-MM-dd hh:mm:ss");
        try {
            date = sdf.parse(s);
        } catch (ParseException ex) {
            Logger.getLogger(DayEndServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

}
