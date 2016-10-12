/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.ItemController;
import lk.ijse.winestores.dao.custom.ItemDetailsDAO;
import lk.ijse.winestores.dao.dto.ItemDetailsDTO;

/**
 *
 * @author Ranjith Suranga
 */
public class Items extends javax.swing.JPanel {

    private DefaultTableModel dtmAllItems;
    private DefaultTableModel dtm;          // Holds the table model
    private SuraTable stbl;                 // Holds the SuraTable Instance   

    private boolean flag;                   // Holds an indicator which table model should set

    /**
     * Creates new form Items
     */
    public Items() {
        initComponents();

        stbl = new SuraTable(tblItems);
        dtm = (DefaultTableModel) tblItems.getModel();
        dtmAllItems = new DefaultTableModel(0, 5);
        tblItems.setAutoCreateColumnsFromModel(false);

        for (int i = 0; i < dtm.getColumnCount(); i++) {

            if (i == 1) {
                continue;
            }
            stbl.setHeaderAlignment(i, SwingConstants.CENTER);

        }

        for (int i = 0; i < dtm.getColumnCount(); i++) {
            if (i == 1) {
                continue;
            }
            stbl.setColumnAlignment(i, SwingConstants.CENTER);

        }

        handleEvents();

        flag = true;
        dtmAllItems.setRowCount(0);
        tblItems.setModel(dtmAllItems);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                getAllItems(ItemDetailsDAO.QueryType.ITEM_CODE, "");
            }
        });
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();

    }

    private void getAllItems(ItemDetailsDAO.QueryType queryType, String queryWord) {

        if (dtmAllItems.getRowCount() > 0) {
            tblItems.setModel(dtmAllItems);
            return;
        }

        ItemController ctrl = (ItemController) ControllerFactory.getInstance().getController(SuperController.ControllerType.ITEM);
        ArrayList<ItemDetailsDTO> itemDetails = null;
        try {
            itemDetails = ctrl.getItemDetails(queryType, queryWord);

            if (itemDetails == null) {
                return;
            }

            for (ItemDetailsDTO itemDetail : itemDetails) {
                Object[] rowData = {itemDetail.getItemCode(), itemDetail.getItemName(), String.valueOf(itemDetail.getQty()), String.valueOf(itemDetail.getSellingPrice()), String.valueOf(itemDetail.getBuyingPrice())};
                if (!flag) {
                    if (tblItems.getModel().equals(dtmAllItems)) {
                        tblItems.setModel(dtm);
                    }
                }
                dtmAllItems.addRow(rowData);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Items.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Items.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void searchItems(ItemDetailsDAO.QueryType queryType, String queryWord) {

        dtm.setRowCount(0);
        tblItems.setModel(dtm);

        ItemController ctrl = (ItemController) ControllerFactory.getInstance().getController(SuperController.ControllerType.ITEM);
        ArrayList<ItemDetailsDTO> itemDetails = null;
        try {
            itemDetails = ctrl.getItemDetails(queryType, queryWord);

            if (itemDetails == null) {
                return;
            }

            for (ItemDetailsDTO itemDetail : itemDetails) {
                Object[] rowData = {itemDetail.getItemCode(), itemDetail.getItemName(), String.valueOf(itemDetail.getQty()), String.valueOf(itemDetail.getSellingPrice()), String.valueOf(itemDetail.getBuyingPrice())};
                dtm.addRow(rowData);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Items.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Items.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void handleEvents() {

        for (Component comp : pnlTop.getComponents()) {

            if (comp instanceof JRadioButton) {
                JRadioButton rdo = (JRadioButton) comp;

                rdo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changeSearchTitle();

                        // Setting the focus to search box
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                txtSearch.requestFocusInWindow();
                            }
                        });
                    }
                });
            }

        }

        // Setting focus to the Search Text Box
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                txtSearch.requestFocusInWindow();
            }
        });

    }

    private void changeSearchTitle() {

        String title = "Enter Item Code to Search the Item";

        if (rdoItemName.isSelected()) {
            title = "Enter Item Name to Search the Item";
        } else if (rdoBarCode.isSelected()) {
            title = "Enter Barcode to Search the Item";
        } else if (rdoSellingPrice.isSelected()) {
            title = "Enter Selling Price to Search the Item";
        }

        lblSearchTitle.setText(title);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btngrp = new javax.swing.ButtonGroup();
        pnlTop = new javax.swing.JPanel();
        rdoItemCode = new javax.swing.JRadioButton();
        rdoItemName = new javax.swing.JRadioButton();
        rdoBarCode = new javax.swing.JRadioButton();
        rdoSellingPrice = new javax.swing.JRadioButton();
        txtSearch = new javax.swing.JTextField();
        lblSearchTitle = new javax.swing.JLabel();
        pnlContainer = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItems = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));

        pnlTop.setBackground(new java.awt.Color(255, 255, 255));

        btngrp.add(rdoItemCode);
        rdoItemCode.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoItemCode.setMnemonic('C');
        rdoItemCode.setSelected(true);
        rdoItemCode.setText("Item Code");
        rdoItemCode.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdoItemCode.setOpaque(false);
        rdoItemCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoItemCodeActionPerformed(evt);
            }
        });

        btngrp.add(rdoItemName);
        rdoItemName.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoItemName.setMnemonic('N');
        rdoItemName.setText("Item Name");
        rdoItemName.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdoItemName.setOpaque(false);
        rdoItemName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoItemNameActionPerformed(evt);
            }
        });

        btngrp.add(rdoBarCode);
        rdoBarCode.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoBarCode.setMnemonic('B');
        rdoBarCode.setText("Barcode");
        rdoBarCode.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdoBarCode.setOpaque(false);

        btngrp.add(rdoSellingPrice);
        rdoSellingPrice.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoSellingPrice.setMnemonic('S');
        rdoSellingPrice.setText("Selling Price");
        rdoSellingPrice.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdoSellingPrice.setOpaque(false);

        txtSearch.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSearchFocusGained(evt);
            }
        });
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        lblSearchTitle.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblSearchTitle.setText("Enter Item Code to Search the Item");

        javax.swing.GroupLayout pnlTopLayout = new javax.swing.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSearch)
                    .addGroup(pnlTopLayout.createSequentialGroup()
                        .addGroup(pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlTopLayout.createSequentialGroup()
                                .addComponent(rdoItemCode)
                                .addGap(18, 18, 18)
                                .addComponent(rdoItemName)
                                .addGap(18, 18, 18)
                                .addComponent(rdoBarCode)
                                .addGap(18, 18, 18)
                                .addComponent(rdoSellingPrice))
                            .addComponent(lblSearchTitle))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlTopLayout.setVerticalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoItemCode)
                    .addComponent(rdoItemName)
                    .addComponent(rdoBarCode)
                    .addComponent(rdoSellingPrice))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(lblSearchTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlContainer.setBackground(new java.awt.Color(233, 236, 242));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234), 2));

        jScrollPane1.setBorder(null);

        tblItems.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        tblItems.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Code", "Item name", "Qty.", "Selling Price", "Buying Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItems.setGridColor(new java.awt.Color(233, 236, 242));
        tblItems.setRowHeight(50);
        tblItems.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblItems.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblItems.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblItems.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblItems);
        if (tblItems.getColumnModel().getColumnCount() > 0) {
            tblItems.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblItems.getColumnModel().getColumn(1).setPreferredWidth(500);
            tblItems.getColumnModel().getColumn(2).setMinWidth(80);
            tblItems.getColumnModel().getColumn(2).setPreferredWidth(80);
            tblItems.getColumnModel().getColumn(2).setMaxWidth(80);
            tblItems.getColumnModel().getColumn(3).setPreferredWidth(120);
            tblItems.getColumnModel().getColumn(4).setPreferredWidth(120);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout pnlContainerLayout = new javax.swing.GroupLayout(pnlContainer);
        pnlContainer.setLayout(pnlContainerLayout);
        pnlContainerLayout.setHorizontalGroup(
            pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlContainerLayout.setVerticalGroup(
            pnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rdoItemCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoItemCodeActionPerformed

    }//GEN-LAST:event_rdoItemCodeActionPerformed

    private void rdoItemNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoItemNameActionPerformed
    }//GEN-LAST:event_rdoItemNameActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        ItemDetailsDAO.QueryType queryType = ItemDetailsDAO.QueryType.ITEM_CODE;
        if (rdoItemName.isSelected()) {
            queryType = ItemDetailsDAO.QueryType.ITEM_NAME;
        } else if (rdoBarCode.isSelected()) {
            queryType = ItemDetailsDAO.QueryType.BARCODE;
        } else if (rdoSellingPrice.isSelected()) {
            queryType = ItemDetailsDAO.QueryType.SELLING_PRICE;
        }
        this.searchItems(queryType, txtSearch.getText());
        flag = txtSearch.getText().trim().isEmpty();
        if (flag) {
            getAllItems(queryType, "");
        }
    }//GEN-LAST:event_txtSearchKeyReleased

    private void txtSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSearchFocusGained
        txtSearch.selectAll();
    }//GEN-LAST:event_txtSearchFocusGained

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btngrp;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSearchTitle;
    private javax.swing.JPanel pnlContainer;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JRadioButton rdoBarCode;
    private javax.swing.JRadioButton rdoItemCode;
    private javax.swing.JRadioButton rdoItemName;
    private javax.swing.JRadioButton rdoSellingPrice;
    private javax.swing.JTable tblItems;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
