/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.GRNController;
import lk.ijse.winestores.controller.custom.SupplierController;
import lk.ijse.winestores.dao.custom.GrnDAO;

/**
 *
 * @author Ranjith Suranga
 */
public class GRNMaster extends javax.swing.JPanel {

    private SuraButton sbtn;                                                    // Holds the SuraButton Instance
    private SuraTable stbl;                                                     // Holds the SuraTable Instance

    private DefaultTableModel dtm;                                              // Holds the default table model

    private ArrayList<GRNController.GRNDetailsModel> currentGrns;               // Holds all the curent GRNS

    /**
     * Creates new form GRN
     */
    public GRNMaster() {
        initComponents();

        sbtn = new SuraButton(this);
        sbtn.convertAllJButtonsToSuraButtons();

        stbl = new SuraTable(tblGRN);
        dtm = (DefaultTableModel) tblGRN.getModel();

        for (int i = 0; i < dtm.getColumnCount(); i++) {

            if (i == 2) {
                continue;
            }
            stbl.setHeaderAlignment(i, SwingConstants.CENTER);

        }

        for (int i = 0; i < dtm.getColumnCount() - 1; i++) {
            if (i == 1) {
                continue;
            }
            stbl.setColumnAlignment(i, SwingConstants.CENTER);

        }

        searchGrns(GrnDAO.QueryType.GRN_ID, "");
        
        handleEvents();

    }

    private void handleEvents() {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtSearch.requestFocusInWindow();
            }
        });

        dtm.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (dtm.getRowCount() == 0 || tblGRN.getSelectedRow() == -1) {
                    btnView.setEnabled(false);
                }

                btnDelete.setEnabled(false);

                for (int i = 0; i < dtm.getRowCount(); i++) {

                    if ((boolean) dtm.getValueAt(i, 0) == true) {
                        btnDelete.setEnabled(true);
                        break;
                    }

                }
            }
        });

    }

    private void searchGrns(GrnDAO.QueryType queryType, String queryWord) {

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        GRNController controller = (GRNController) ControllerFactory.getInstance().getController(SuperController.ControllerType.GRN);
        SupplierController supplierController = (SupplierController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUPPLIER);

        dtm.setRowCount(0);

        try {
            currentGrns = controller.getGrns(queryType, queryWord);

            JCheckBox chk = (JCheckBox) tblGRN.getColumnModel().getColumn(0).getHeaderRenderer().getTableCellRendererComponent(tblGRN, null, true, true, 0, 0);

            if (currentGrns != null) {

                for (GRNController.GRNDetailsModel currentGrn : currentGrns) {

                    String supplierName = "";
                    SupplierController.SupplierModel supplierModel = supplierController.getSupplierById(currentGrn.getSupplierId());
                    if (supplierModel != null) {
                        supplierName = supplierModel.getName();
                    }

                    boolean isAuthorized = controller.isGrnAuthorized(currentGrn.getGrnId());
                    Object[] rowData = {chk.isSelected(),
                        currentGrn.getGrnId(),
                        supplierName,
                        currentGrn.getGrnDate(),
                        currentGrn.getInvoiceId(),
                        currentGrn.getInvoiceDate(),
                        isAuthorized ? "Authorized" : "<html><span style='color:rgb(240,173,78);'>Pending</span></html>"
                //supplierController.
                    };

                    dtm.addRow(rowData);
                }

            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GRNMaster.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GRNMaster.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.setCursor(Cursor.getDefaultCursor());
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGRN = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        rdoGrnId = new javax.swing.JRadioButton();
        rdoGrnDate = new javax.swing.JRadioButton();
        rdoSupplier = new javax.swing.JRadioButton();
        rdoInvoiceNumber = new javax.swing.JRadioButton();
        rdoInvoiceDate = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JFormattedTextField();

        jPanel2.setBackground(new java.awt.Color(233, 236, 242));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234), 2));

        jScrollPane1.setBorder(null);

        tblGRN.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        tblGRN.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "GRN ID", "Supplier", "GRN Date", "Invoice Number", "Invoice Date", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGRN.setGridColor(new java.awt.Color(233, 236, 242));
        tblGRN.setRowHeight(50);
        tblGRN.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblGRN.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblGRN.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblGRN.getTableHeader().setReorderingAllowed(false);
        tblGRN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblGRNFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblGRNFocusLost(evt);
            }
        });
        tblGRN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGRNMouseClicked(evt);
            }
        });
        tblGRN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblGRNKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblGRN);
        if (tblGRN.getColumnModel().getColumnCount() > 0) {
            tblGRN.getColumnModel().getColumn(0).setMinWidth(40);
            tblGRN.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblGRN.getColumnModel().getColumn(0).setMaxWidth(40);
            tblGRN.getColumnModel().getColumn(1).setPreferredWidth(90);
            tblGRN.getColumnModel().getColumn(2).setPreferredWidth(550);
            tblGRN.getColumnModel().getColumn(3).setPreferredWidth(110);
            tblGRN.getColumnModel().getColumn(4).setPreferredWidth(125);
            tblGRN.getColumnModel().getColumn(5).setPreferredWidth(110);
            tblGRN.getColumnModel().getColumn(6).setPreferredWidth(110);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1)
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(220, 225, 234)));

        buttonGroup1.add(rdoGrnId);
        rdoGrnId.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoGrnId.setMnemonic('G');
        rdoGrnId.setSelected(true);
        rdoGrnId.setText(" GRN ID");
        rdoGrnId.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdoGrnId.setOpaque(false);
        rdoGrnId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGrnIdActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoGrnDate);
        rdoGrnDate.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoGrnDate.setMnemonic('D');
        rdoGrnDate.setText("GRN Date");
        rdoGrnDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdoGrnDate.setOpaque(false);
        rdoGrnDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGrnDateActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoSupplier);
        rdoSupplier.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoSupplier.setMnemonic('S');
        rdoSupplier.setText("Supplier");
        rdoSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdoSupplier.setOpaque(false);
        rdoSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSupplierActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoInvoiceNumber);
        rdoInvoiceNumber.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoInvoiceNumber.setText("Invoice Number");
        rdoInvoiceNumber.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdoInvoiceNumber.setDisplayedMnemonicIndex(9);
        rdoInvoiceNumber.setOpaque(false);
        rdoInvoiceNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInvoiceNumberActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoInvoiceDate);
        rdoInvoiceDate.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoInvoiceDate.setMnemonic('I');
        rdoInvoiceDate.setText("Invoice Date");
        rdoInvoiceDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdoInvoiceDate.setOpaque(false);
        rdoInvoiceDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInvoiceDateActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel1.setText("Enter Search Criteria to Search GRN");

        btnDelete.setBackground(new java.awt.Color(251, 93, 93));
        btnDelete.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setMnemonic('e');
        btnDelete.setText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnView.setBackground(new java.awt.Color(240, 173, 78));
        btnView.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnView.setForeground(new java.awt.Color(255, 255, 255));
        btnView.setMnemonic('V');
        btnView.setText("View");
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        btnNew.setBackground(new java.awt.Color(72, 158, 231));
        btnNew.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setMnemonic('N');
        btnNew.setText("New");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        jLabel2.setDisplayedMnemonic('L');
        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel2.setLabelFor(tblGRN);
        jLabel2.setText("List of Good Received Notes");

        txtSearch.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSearch)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(rdoGrnId)
                                .addGap(18, 18, 18)
                                .addComponent(rdoGrnDate)
                                .addGap(18, 18, 18)
                                .addComponent(rdoSupplier)
                                .addGap(18, 18, 18)
                                .addComponent(rdoInvoiceNumber)
                                .addGap(18, 18, 18)
                                .addComponent(rdoInvoiceDate))
                            .addComponent(jLabel1))
                        .addGap(0, 63, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDelete, btnNew, btnView});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoGrnId)
                    .addComponent(rdoGrnDate)
                    .addComponent(rdoSupplier)
                    .addComponent(rdoInvoiceNumber)
                    .addComponent(rdoInvoiceDate))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Main m = (Main) SwingUtilities.getWindowAncestor(this);
        m.pnlContainer.removeAll();
        GRN newGrn = new GRN();
        m.pnlContainer.add(newGrn);
        m.pnlContainer.updateUI();
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnNewActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        GrnDAO.QueryType queryType = GrnDAO.QueryType.GRN_ID;
        if (rdoGrnId.isSelected()) {
            queryType = GrnDAO.QueryType.GRN_ID;
        } else if (rdoGrnDate.isSelected()) {
            queryType = GrnDAO.QueryType.GRN_DATE;
        } else if (rdoInvoiceDate.isSelected()) {
            queryType = GrnDAO.QueryType.INVOICE_DATE;
        } else if (rdoInvoiceNumber.isSelected()) {
            queryType = GrnDAO.QueryType.INVOICE_NUMBER;
        } else if (rdoSupplier.isSelected()) {
            queryType = GrnDAO.QueryType.SUPPLIER_NAME;
        }
        this.searchGrns(queryType, txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchKeyReleased

    private void rdoGrnIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGrnIdActionPerformed
        txtSearch.requestFocusInWindow();
    }//GEN-LAST:event_rdoGrnIdActionPerformed

    private void rdoGrnDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGrnDateActionPerformed
        txtSearch.requestFocusInWindow();
    }//GEN-LAST:event_rdoGrnDateActionPerformed

    private void rdoSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSupplierActionPerformed
        txtSearch.requestFocusInWindow();
    }//GEN-LAST:event_rdoSupplierActionPerformed

    private void rdoInvoiceNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInvoiceNumberActionPerformed
        txtSearch.requestFocusInWindow();
    }//GEN-LAST:event_rdoInvoiceNumberActionPerformed

    private void rdoInvoiceDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInvoiceDateActionPerformed
        txtSearch.requestFocusInWindow();
    }//GEN-LAST:event_rdoInvoiceDateActionPerformed

    private void tblGRNKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGRNKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if (dtm.getRowCount() > 0) {
                boolean currentStatus = (boolean) dtm.getValueAt(tblGRN.getSelectedRow(), 0);
                dtm.setValueAt(!currentStatus, tblGRN.getSelectedRow(), 0);

            }
        }
    }//GEN-LAST:event_tblGRNKeyPressed

    private void tblGRNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGRNMouseClicked

        if (tblGRN.getSelectedRow() == -1) {
            return;
        }

        btnView.setEnabled(true);

    }//GEN-LAST:event_tblGRNMouseClicked

    private void tblGRNFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblGRNFocusLost
        // For rare reason
        if (dtm.getRowCount() == 0 || tblGRN.getSelectedRow() == -1) {
            btnView.setEnabled(false);
        }
    }//GEN-LAST:event_tblGRNFocusLost

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Main m = (Main) SwingUtilities.getWindowAncestor(this);
        m.pnlContainer.removeAll();
        GRN newGrn = new GRN(currentGrns.get(tblGRN.getSelectedRow()), dtm.getValueAt(tblGRN.getSelectedRow(), dtm.getColumnCount() -1).toString().contains("Pending") ? false : true );
        m.pnlContainer.add(newGrn);
        m.pnlContainer.updateUI();
        this.setCursor(Cursor.getDefaultCursor());       
        
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        
        // a liitle hack
        tblGRN.editingCanceled(null);
        
        GRNController controller = (GRNController) ControllerFactory.getInstance().getController(SuperController.ControllerType.GRN);
        
        for (int i = 0; i < tblGRN.getRowCount(); i++) {

            boolean deleteStatus = (boolean) dtm.getValueAt(i, 0);
            if (deleteStatus) {
                try {
                    int result = controller.removeGrn(dtm.getValueAt(i, 1).toString(), dtm.getValueAt(i, dtm.getColumnCount() -1).toString().contains("Pending") ? false : true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GRNMaster.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(GRNMaster.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

        GrnDAO.QueryType queryType = GrnDAO.QueryType.GRN_ID;
        if (rdoGrnId.isSelected()) {
            queryType = GrnDAO.QueryType.GRN_ID;
        } else if (rdoGrnDate.isSelected()) {
            queryType = GrnDAO.QueryType.GRN_DATE;
        } else if (rdoInvoiceDate.isSelected()) {
            queryType = GrnDAO.QueryType.INVOICE_DATE;
        } else if (rdoInvoiceNumber.isSelected()) {
            queryType = GrnDAO.QueryType.INVOICE_NUMBER;
        } else if (rdoSupplier.isSelected()) {
            queryType = GrnDAO.QueryType.SUPPLIER_NAME;
        }
        this.searchGrns(queryType, txtSearch.getText().trim());
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblGRNFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblGRNFocusGained
        
        if (dtm.getRowCount() > 0){
            tblGRN.getSelectionModel().setSelectionInterval(0, 0);
        }
        
    }//GEN-LAST:event_tblGRNFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnView;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdoGrnDate;
    private javax.swing.JRadioButton rdoGrnId;
    private javax.swing.JRadioButton rdoInvoiceDate;
    private javax.swing.JRadioButton rdoInvoiceNumber;
    private javax.swing.JRadioButton rdoSupplier;
    private javax.swing.JTable tblGRN;
    private javax.swing.JFormattedTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
