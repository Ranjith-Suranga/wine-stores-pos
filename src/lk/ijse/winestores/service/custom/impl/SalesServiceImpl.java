/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.service.custom.impl;

import java.awt.KeyboardFocusManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import lk.ijse.winestores.dao.DAOFactory;
import lk.ijse.winestores.dao.SuperDAO;
import lk.ijse.winestores.dao.custom.ChequeDetailsDAO;
import lk.ijse.winestores.dao.custom.CreditOrderDAO;
import lk.ijse.winestores.dao.custom.CreditOrderEmptyBottleDetailsDAO;
import lk.ijse.winestores.dao.custom.CreditOrderItemDetailsDAO;
import lk.ijse.winestores.dao.custom.CustomOrderDAO;
import lk.ijse.winestores.dao.custom.CustomerDAO;
import lk.ijse.winestores.dao.custom.ItemDetailsDAO;
import lk.ijse.winestores.dao.custom.OrderEmptyBottleDetailsDAO;
import lk.ijse.winestores.dao.custom.OrderItemDetailsDAO;
import lk.ijse.winestores.dao.dto.ChequeDetailsDTO;
import lk.ijse.winestores.dao.dto.CreditOrderDTO;
import lk.ijse.winestores.dao.dto.CreditOrderItemDetailsDTO;
import lk.ijse.winestores.dao.dto.CustomOrderDTO;
import lk.ijse.winestores.dao.dto.OrderEmptyBottleDetailsDTO;
import lk.ijse.winestores.dao.dto.OrderItemDetailsDTO;
import lk.ijse.winestores.resource.ResourceConnection;
import lk.ijse.winestores.resource.ResourceFactory;
import lk.ijse.winestores.service.custom.SalesService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Ranjith Suranga
 */
public class SalesServiceImpl implements SalesService {

    private Connection connection;                                  // For transactions
    private ItemDetailsDAO itemDetailsDAO;

    // For cash orders
    private CustomOrderDAO customOrderDAO;
    private OrderItemDetailsDAO orderItemDetailsDAO;
    private OrderEmptyBottleDetailsDAO orderEmptyBottleDetailsDAO;
    private ChequeDetailsDAO chequeDetailsDAO;

    // For credit orders
    private CreditOrderDAO creditOrderDAO;
    private CreditOrderItemDetailsDAO creditOrderItemDetailsDAO;
    private CreditOrderEmptyBottleDetailsDAO creditOrderEmptyBottleDetailsDAO;
    private CustomerDAO customerDAO;

    public SalesServiceImpl() {
        try {
            connection = (Connection) ResourceFactory.getInstance().getResourceConnection(ResourceConnection.ResourceConnectionType.SINGELTON_DATABASE_CONNECTION).getConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SalesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SalesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        customOrderDAO = (CustomOrderDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.CUSTOM_ORDER);
        orderItemDetailsDAO = (OrderItemDetailsDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.ORDER_ITEM_DETAILS);
        orderEmptyBottleDetailsDAO = (OrderEmptyBottleDetailsDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.ORDER_EMPTY_BOTTLE_DETAILS);
        chequeDetailsDAO = (ChequeDetailsDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.CHEQUE_DETAILS);
        itemDetailsDAO = (ItemDetailsDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.ITEM_DETAILS);

        creditOrderDAO = (CreditOrderDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.CREDIT_ORDER);
        creditOrderItemDetailsDAO = (CreditOrderItemDetailsDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.CREDIT_ORDER_ITEM_DETAILS);
        creditOrderEmptyBottleDetailsDAO = (CreditOrderEmptyBottleDetailsDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.CREDIT_ORDER_EMPTY_BOTTLE_DETAILS);
        customerDAO = (CustomerDAO) DAOFactory.getInstance().getDAO(SuperDAO.DAOType.CUSTOMER);
    }

    @Override
    public boolean saveCashSale(CustomOrderDTO order, ArrayList<OrderItemDetailsDTO> orderItemDetails, ArrayList<OrderEmptyBottleDetailsDTO> orderEmptyBottleDetails, ChequeDetailsDTO chequeDetails) throws ClassNotFoundException, SQLException {

        // Setting the transaction
        connection.setAutoCommit(false);

        // Saving Order
        String orderID = customOrderDAO.create(order);

        if (orderID == null) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }

        // Saving OrderItemDetails & Updating the Qty
        for (OrderItemDetailsDTO dto : orderItemDetails) {
            dto.setOrderId(orderID);
            boolean success = orderItemDetailsDAO.create(dto);
            int currentQty = itemDetailsDAO.readCurrentQty(dto.getItemCode());
            currentQty = currentQty - dto.getQty();
            itemDetailsDAO.updateCurrentQty(connection, dto.getItemCode(), currentQty);
            if (!success) {
                connection.rollback();
                connection.setAutoCommit(false);
                return false;
            }
        }

        // Saving OrderEmptyBottleDetails
        if (orderEmptyBottleDetails != null) {
            for (OrderEmptyBottleDetailsDTO dto : orderEmptyBottleDetails) {
                if (dto.getQty() != 0) {
                    dto.setOrderId(orderID);
                    boolean success = orderEmptyBottleDetailsDAO.create(dto);
                    if (!success) {
                        connection.rollback();
                        connection.setAutoCommit(false);
                        return false;
                    }
                }
            }
        }

        // Saving ChequeDetails
        if (chequeDetails != null) {
            chequeDetails.setOrderId(orderID);
            boolean success = chequeDetailsDAO.create(chequeDetails);
            if (!success) {
                connection.rollback();
                connection.setAutoCommit(false);
                return false;
            }
        }

        // It's time to commit
        connection.commit();
        // Ending the transaction
        connection.setAutoCommit(true);
        
        // Everything is good, so let's print the bill
        try {
            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/lk/ijse/winestores/reports/CashSaleBill.jasper"));
            
            HashMap<String, Object> parms = new HashMap<>();
            parms.put("orderId", orderID);
            
            JasperPrint filledReport = JasperFillManager.fillReport(compiledReport, parms, connection);
            JasperPrintManager.printReport(filledReport, false);
            
        } catch (JRException ex) {
            Logger.getLogger(SalesServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            
            ImageIcon icon = new ImageIcon(this.getClass().getResource("/lk/ijse/winestores/icons/error_icon.png"));
            JOptionPane.showMessageDialog(KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(),
                    "Sorry, order can not be printed, please check the printer.",
                    "Printing Failed",
                    JOptionPane.ERROR_MESSAGE,
                    icon);            
            
        }
        
        // And returns our success :)
        return true;
    }

    @Override
    public boolean saveCreditSale(CreditOrderDTO order, ArrayList<CreditOrderItemDetailsDTO> orderItemDetails) throws ClassNotFoundException, SQLException {

        // Setting the transaction
        connection.setAutoCommit(false);

        // Saving customer details
//        customerDAO.setConnection(connection);
//        String customerID = customerDAO.create(customerDetails);
//        if (customerID == null) {
//            connection.rollback();
//            connection.setAutoCommit(false);
//            return false;
//        }

        // Saving Order
        String orderID = creditOrderDAO.create(order);

        if (orderID == null) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }

        // Saving OrderItemDetails & Updating the Qty
        for (CreditOrderItemDetailsDTO dto : orderItemDetails) {
            dto.setOrderId(orderID);
            boolean success = creditOrderItemDetailsDAO.create(dto);
            //int currentQty = itemDetailsDAO.readCurrentQty(dto.getItemCode());
            //currentQty = currentQty - dto.getQty();
            //itemDetailsDAO.updateCurrentQty(connection, dto.getItemCode(), currentQty);
            if (!success) {
                connection.rollback();
                connection.setAutoCommit(false);
                return false;
            }
        }

        // Saving OrderEmptyBottleDetails
//        if (orderEmptyBottleDetails != null) {
//            for (CreditOrderEmptyBottleDetailsDTO dto : orderEmptyBottleDetails) {
//                if (dto.getQty() != 0) {
//                    dto.setOrderId(orderID);
//                    boolean success = creditOrderEmptyBottleDetailsDAO.create(dto);
//                    if (!success) {
//                        connection.rollback();
//                        connection.setAutoCommit(false);
//                        return false;
//                    }
//                }
//            }
//        }

        // It's time to commit
        connection.commit();
        // Ending the transaction
        connection.setAutoCommit(true);
        // And returns our success :)
        return true;

    }

}
