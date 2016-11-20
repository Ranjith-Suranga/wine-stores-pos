/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.winestores.views;

import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import lk.ijse.winestores.controller.ControllerFactory;
import lk.ijse.winestores.controller.SuperController;
import lk.ijse.winestores.controller.custom.SupplierController;
import lk.ijse.winestores.controller.custom.SupplierOrderController;
import lk.ijse.winestores.dao.dto.SupplierOrderDTO;
import lk.ijse.winestores.views.util.FocusHandler;

/**
 *
 * @author Ranjith Suranga
 */
public class SupplierOrders extends javax.swing.JPanel implements FocusHandler{

    private SuraButton sbtn;                                                    // Holds the SuraButton Instance
    private SuraTable stbl;                                                     // Holds the SuraTable Instance

    private DefaultTableModel dtmCurrent;
    private DefaultTableModel dtmAllSupplierOrders;

    private Thread loadAllOrdersThread;

    private SupplierOrderController supplierOrderCtrl;

    /**
     * Creates new form GRN
     */
    public SupplierOrders() {
        initComponents();

        sbtn = new SuraButton(this);
        sbtn.convertAllJButtonsToSuraButtons();

        stbl = new SuraTable(tblSupplierOrder);
        tblSupplierOrder.setAutoCreateColumnsFromModel(false);

        supplierOrderCtrl = (SupplierOrderController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUPPLIER_ORDER);

        for (int i = 0; i < tblSupplierOrder.getColumnCount(); i++) {

            if (i == 2) {
                continue;
            }
            stbl.setHeaderAlignment(i, SwingConstants.CENTER);

        }

        for (int i = 0; i < tblSupplierOrder.getColumnCount() - 1; i++) {
            if (i == 1) {
                continue;
            }
            stbl.setColumnAlignment(i, SwingConstants.CENTER);

        }

        dtmAllSupplierOrders = (DefaultTableModel) tblSupplierOrder.getModel();
        dtmCurrent = new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "", "Order ID", "Supplier", "Order Date", "Order Total"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean[]{
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        loadAllOrdersThread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadAllSupplierOrders();
            }
        });

        loadAllOrdersThread.setPriority(Thread.MIN_PRIORITY);
        loadAllOrdersThread.start();
        handleEvents();

    }

    private void handleEvents() {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtSearch.requestFocusInWindow();
            }
        });

        tblSupplierOrder.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btnView.setEnabled(tblSupplierOrder.getSelectedRow() != -1);
            }
        });

        tblSupplierOrder.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("model")) {

                    btnDelete.setEnabled(false);

                    for (int i = 0; i < tblSupplierOrder.getRowCount(); i++) {

                        if ((boolean) tblSupplierOrder.getValueAt(i, 0) == true) {
                            btnDelete.setEnabled(true);
                            break;
                        }

                    }
                }
            }
        });

        dtmAllSupplierOrders.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (tblSupplierOrder.getRowCount() == 0 || tblSupplierOrder.getSelectedRow() == -1) {
                    btnView.setEnabled(false);
                }

                btnDelete.setEnabled(false);

                for (int i = 0; i < tblSupplierOrder.getRowCount(); i++) {

                    if ((boolean) tblSupplierOrder.getValueAt(i, 0) == true) {
                        btnDelete.setEnabled(true);
                        break;
                    }

                }
            }
        });

        dtmCurrent.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (tblSupplierOrder.getRowCount() == 0 || tblSupplierOrder.getSelectedRow() == -1) {
                    btnView.setEnabled(false);
                }

                btnDelete.setEnabled(false);

                for (int i = 0; i < tblSupplierOrder.getRowCount(); i++) {

                    if ((boolean) tblSupplierOrder.getValueAt(i, 0) == true) {
                        btnDelete.setEnabled(true);
                        break;
                    }

                }
            }
        });

        tblSupplierOrder.getTableHeader().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int column = tblSupplierOrder.columnAtPoint(e.getPoint());
                if (column == -1) {
                    return;
                }

                if (column == 0) {
                    JCheckBox chkTick = (JCheckBox) tblSupplierOrder.getColumnModel().getColumn(0).getHeaderRenderer().getTableCellRendererComponent(tblSupplierOrder, null, false, false, 0, 0);

                    for (int i = 0; i < tblSupplierOrder.getRowCount(); i++) {
                        tblSupplierOrder.setValueAt(chkTick.isSelected(), i, 0);
                    }
                }

            }

        });

    }

    private void loadAllSupplierOrders() {
        if (!tblSupplierOrder.getModel().equals(dtmAllSupplierOrders)) {
            tblSupplierOrder.setModel(dtmAllSupplierOrders);
        }

        try {
            dtmAllSupplierOrders.setRowCount(0);
            ArrayList<SupplierOrderDTO> allSupplierOrders = supplierOrderCtrl.getAllSupplierOrders();

            if (allSupplierOrders != null) {

                JCheckBox chkTick = (JCheckBox) tblSupplierOrder.getColumnModel().getColumn(0).getHeaderRenderer().getTableCellRendererComponent(tblSupplierOrder, null, false, tblSupplierOrder.hasFocus(), 0, 0);
                SupplierController supplierCtrl = (SupplierController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUPPLIER);

                for (SupplierOrderDTO supplierOrder : allSupplierOrders) {
                    Object[] rowData = {
                        chkTick.isSelected(),
                        supplierOrder.getOrderId() + "",
                        supplierCtrl.getSupplierById(supplierOrder.getSupplierId()).getName(),
                        formatDate(supplierOrder.getOrderDate()),
                        supplierOrder.getOrderTotal()
                    };
                    dtmAllSupplierOrders.addRow(rowData);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SupplierOrders.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SupplierOrders.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void searchSupplierOrder(String searchBy, String queryWord) {

        tblSupplierOrder.setModel(dtmCurrent);
        dtmCurrent.setRowCount(0);

        try {

            ArrayList<SupplierOrderDTO> supplierOrders = null;

            switch (searchBy) {
                case "Order Id":

                    try {
                        supplierOrders = supplierOrderCtrl.getSupplierOrdersByOrderId(Integer.parseInt(queryWord));
                    } catch (NumberFormatException ex) {
                        txtSearch.selectAll();
                        txtSearch.requestFocusInWindow();
                    }

                    break;
                case "Order Date":
                    supplierOrders = supplierOrderCtrl.getSupplierOrdersByOrderDate(queryWord);

                    break;
                case "Supplier":
                    supplierOrders = supplierOrderCtrl.getSupplierOrdersBySupplierName(queryWord);
                    break;
            }

            if (supplierOrders != null) {

                JCheckBox chkTick = (JCheckBox) tblSupplierOrder.getColumnModel().getColumn(0).getHeaderRenderer().getTableCellRendererComponent(tblSupplierOrder, null, false, tblSupplierOrder.hasFocus(), 0, 0);
                System.out.println(chkTick.isSelected());
                SupplierController supplierCtrl = (SupplierController) ControllerFactory.getInstance().getController(SuperController.ControllerType.SUPPLIER);

                for (SupplierOrderDTO supplierOrder : supplierOrders) {
                    Object[] rowData = {
                        chkTick.isSelected(),
                        supplierOrder.getOrderId() + "",
                        supplierCtrl.getSupplierById(supplierOrder.getSupplierId()).getName(),
                        formatDate(supplierOrder.getOrderDate()),
                        supplierOrder.getOrderTotal()
                    };
                    dtmCurrent.addRow(rowData);
                }

            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SupplierOrders.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SupplierOrders.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
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
        tblSupplierOrder = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        rdoOrderId = new javax.swing.JRadioButton();
        rdoOrderDate = new javax.swing.JRadioButton();
        rdoSupplier = new javax.swing.JRadioButton();
        lblSearchTitle = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JFormattedTextField();

        jPanel2.setBackground(new java.awt.Color(233, 236, 242));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 225, 234), 2));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);

        tblSupplierOrder.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        tblSupplierOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Order ID", "Supplier", "Order Date", "Order Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSupplierOrder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblSupplierOrder.setGridColor(new java.awt.Color(233, 236, 242));
        tblSupplierOrder.setOpaque(false);
        tblSupplierOrder.setRowHeight(50);
        tblSupplierOrder.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblSupplierOrder.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblSupplierOrder.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblSupplierOrder.getTableHeader().setReorderingAllowed(false);
        tblSupplierOrder.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblSupplierOrderFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblSupplierOrderFocusLost(evt);
            }
        });
        tblSupplierOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSupplierOrderMouseClicked(evt);
            }
        });
        tblSupplierOrder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblSupplierOrderKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblSupplierOrder);
        if (tblSupplierOrder.getColumnModel().getColumnCount() > 0) {
            tblSupplierOrder.getColumnModel().getColumn(0).setMinWidth(40);
            tblSupplierOrder.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblSupplierOrder.getColumnModel().getColumn(0).setMaxWidth(40);
            tblSupplierOrder.getColumnModel().getColumn(1).setPreferredWidth(90);
            tblSupplierOrder.getColumnModel().getColumn(2).setPreferredWidth(550);
            tblSupplierOrder.getColumnModel().getColumn(3).setPreferredWidth(110);
            tblSupplierOrder.getColumnModel().getColumn(4).setPreferredWidth(125);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(220, 225, 234)));

        buttonGroup1.add(rdoOrderId);
        rdoOrderId.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoOrderId.setMnemonic('O');
        rdoOrderId.setSelected(true);
        rdoOrderId.setText("Order ID");
        rdoOrderId.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdoOrderId.setOpaque(false);
        rdoOrderId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoOrderIdActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoOrderDate);
        rdoOrderDate.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        rdoOrderDate.setMnemonic('D');
        rdoOrderDate.setText("Order Date");
        rdoOrderDate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdoOrderDate.setDisplayedMnemonicIndex(6);
        rdoOrderDate.setOpaque(false);
        rdoOrderDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoOrderDateActionPerformed(evt);
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

        lblSearchTitle.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lblSearchTitle.setText("Enter Supplier Order ID to Search");

        btnDelete.setBackground(new java.awt.Color(251, 93, 93));
        btnDelete.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setMnemonic('e');
        btnDelete.setText("Delete");
        btnDelete.setToolTipText("Check one or more supplier order(s) to delete");
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
        btnView.setToolTipText("Click to view/edit selected supplier order");
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
        btnNew.setToolTipText("Click to create a new supplier order");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        jLabel2.setDisplayedMnemonic('L');
        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel2.setLabelFor(tblSupplierOrder);
        jLabel2.setText("List of Supplier Orders");

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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchKeyTyped(evt);
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
                                .addComponent(rdoOrderId)
                                .addGap(18, 18, 18)
                                .addComponent(rdoOrderDate)
                                .addGap(18, 18, 18)
                                .addComponent(rdoSupplier))
                            .addComponent(lblSearchTitle))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDelete, btnNew, btnView});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoOrderId)
                    .addComponent(rdoOrderDate)
                    .addComponent(rdoSupplier))
                .addGap(18, 18, 18)
                .addComponent(lblSearchTitle)
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
        SaveSupplierOrder saveSupplierOrder = new SaveSupplierOrder();
        m.setExtenstion(saveSupplierOrder);
        m.pnlContainer.add(saveSupplierOrder);
        m.pnlContainer.updateUI();
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnNewActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        String searchBy;
        if (rdoOrderId.isSelected()) {
            searchBy = "Order Id";
        } else if (rdoOrderDate.isSelected()) {
            searchBy = "Order Date";
        } else {
            searchBy = "Supplier";
        }
        if (txtSearch.getText().trim().isEmpty()) {
            tblSupplierOrder.setModel(dtmAllSupplierOrders);
        } else {
            searchSupplierOrder(searchBy, txtSearch.getText().trim());
        }

    }//GEN-LAST:event_txtSearchKeyReleased

    private void rdoOrderIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoOrderIdActionPerformed
        lblSearchTitle.setText("Enter Supplier Order ID to Search");
        txtSearch.requestFocusInWindow();
    }//GEN-LAST:event_rdoOrderIdActionPerformed

    private void rdoOrderDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoOrderDateActionPerformed
        lblSearchTitle.setText("Enter Supplier Order Date to Search [YYYY-MM-DD]");
        txtSearch.requestFocusInWindow();
    }//GEN-LAST:event_rdoOrderDateActionPerformed

    private void rdoSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSupplierActionPerformed
        lblSearchTitle.setText("Enter Supplier's Name to Search");
        txtSearch.requestFocusInWindow();
    }//GEN-LAST:event_rdoSupplierActionPerformed

    private void tblSupplierOrderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblSupplierOrderKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {

            if (tblSupplierOrder.getRowCount() > 0) {
                boolean currentStatus = (boolean) tblSupplierOrder.getValueAt(tblSupplierOrder.getSelectedRow(), 0);
                tblSupplierOrder.setValueAt(!currentStatus, tblSupplierOrder.getSelectedRow(), 0);
                evt.consume();
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            
            if (tblSupplierOrder.getRowCount() > 0 && tblSupplierOrder.getSelectedRow() != -1){
                btnView.doClick();
            }
            
        }
    }//GEN-LAST:event_tblSupplierOrderKeyPressed

    private void tblSupplierOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSupplierOrderMouseClicked

    }//GEN-LAST:event_tblSupplierOrderMouseClicked

    private void tblSupplierOrderFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblSupplierOrderFocusLost

    }//GEN-LAST:event_tblSupplierOrderFocusLost

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Main m = (Main) SwingUtilities.getWindowAncestor(this);
        m.pnlContainer.removeAll();
        
        int OrderId = Integer.parseInt(tblSupplierOrder.getValueAt(tblSupplierOrder.getSelectedRow(), 1).toString());
        SaveSupplierOrder saveSupplierOrder = new SaveSupplierOrder(OrderId);
        m.setExtenstion(saveSupplierOrder);
        
        m.pnlContainer.add(saveSupplierOrder);
        m.pnlContainer.updateUI();
        this.setCursor(Cursor.getDefaultCursor());

    }//GEN-LAST:event_btnViewActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed

        tblSupplierOrder.editingCanceled(null);
        for (int i = 0; i < tblSupplierOrder.getRowCount(); i++) {

            boolean deleteStatus = (boolean) tblSupplierOrder.getValueAt(i, 0);
            if (deleteStatus) {

                // Delete the record from the database
                boolean success = false;
                try {
                    success = supplierOrderCtrl.removeSupplierOrder(Integer.parseInt((String) tblSupplierOrder.getValueAt(i, 1)));
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SupplierOrders.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(SupplierOrders.class.getName()).log(Level.SEVERE, null, ex);
                }

                // If delete succeeded, then delete from the table
                if (success) {
                    if (tblSupplierOrder.getModel().equals(dtmAllSupplierOrders)) {
                        dtmAllSupplierOrders.removeRow(i);
                        i--;
                    } else {
                        String orderIdToDelete = (String) tblSupplierOrder.getValueAt(i, 1);
                        dtmCurrent.removeRow(i);
                        i--;
                        // It needs to remove from the all supplier orders list too...
                        for (int j = 0; j < dtmAllSupplierOrders.getRowCount(); j++) {
                            String orderId = (String) dtmAllSupplierOrders.getValueAt(j, 1);

                            if (orderIdToDelete.equals(orderId)) {
                                dtmAllSupplierOrders.removeRow(j);
                                break;
                            }
                        }

                    }
                    txtSearch.requestFocusInWindow();
                }
            }

        }

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSearchFocusGained
        txtSearch.selectAll();
    }//GEN-LAST:event_txtSearchFocusGained

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchKeyPressed

    private void txtSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyTyped
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            if (tblSupplierOrder.getRowCount() > 0) {
                tblSupplierOrder.getSelectionModel().setSelectionInterval(0, 0);
                tblSupplierOrder.requestFocusInWindow();
            }
        } else if (!Character.isDigit(evt.getKeyChar()) && rdoOrderId.isSelected()) {
            if (!(evt.getKeyChar() == KeyEvent.VK_DELETE || evt.getKeyChar() == KeyEvent.VK_BACK_SPACE)) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txtSearchKeyTyped

    private void tblSupplierOrderFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblSupplierOrderFocusGained
        
        if (tblSupplierOrder.getRowCount() > 0 && tblSupplierOrder.getSelectedRowCount()== 0){
            tblSupplierOrder.getSelectionModel().setSelectionInterval(0, 0);
        }
        
    }//GEN-LAST:event_tblSupplierOrderFocusGained

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnView;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSearchTitle;
    private javax.swing.JRadioButton rdoOrderDate;
    private javax.swing.JRadioButton rdoOrderId;
    private javax.swing.JRadioButton rdoSupplier;
    private javax.swing.JTable tblSupplierOrder;
    private javax.swing.JFormattedTextField txtSearch;
    // End of variables declaration//GEN-END:variables

    @Override
    public void initFoucs() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtSearch.requestFocusInWindow();
            }
        });
    }
}
